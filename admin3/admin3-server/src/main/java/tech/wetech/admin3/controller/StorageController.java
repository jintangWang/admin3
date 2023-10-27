package tech.wetech.admin3.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.wetech.admin3.common.authz.RequiresPermissions;
import tech.wetech.admin3.sys.model.Image;
import tech.wetech.admin3.sys.model.StorageConfig;
import tech.wetech.admin3.sys.model.StorageConfig.Type;
import tech.wetech.admin3.sys.model.StorageFile;
import tech.wetech.admin3.sys.model.User;
import tech.wetech.admin3.sys.service.ImageService;
import tech.wetech.admin3.sys.service.StorageService;
import tech.wetech.admin3.sys.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cjbi
 */
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/storage")
public class StorageController {

  private final StorageService storageService;

  private final ImageService imageService;

  private final UserService userService;

  public StorageController(StorageService storageService, ImageService imageService, UserService userService) {
    this.storageService = storageService;
    this.imageService = imageService;
    this.userService = userService;
  }

  @GetMapping("/configs")
  @RequiresPermissions("storage:view")
  public ResponseEntity<List<StorageConfig>> findConfigList() {
    List<StorageConfig> configList = storageService.findConfigList();
    return ResponseEntity.ok(configList);
  }

  @PostMapping("/configs")
  @RequiresPermissions("storage:create")
  public ResponseEntity<StorageConfig> createStorageConfig(@RequestBody StorageConfigRequest request) {
    StorageConfig config = storageService.createConfig(
      request.name(),
      request.type(),
      request.endpoint(),
      request.accessKey(),
      request.secretKey(),
      request.bucketName(),
      request.address(),
      request.storagePath()
    );
    return new ResponseEntity<>(config, HttpStatus.CREATED);
  }

  @PutMapping("/configs/{id}")
  @RequiresPermissions("storage:update")
  public ResponseEntity<StorageConfig> updateStorageConfig(@PathVariable("id") Long id, @RequestBody StorageConfigRequest request) {
    StorageConfig config = storageService.updateConfig(
      id,
      request.name(),
      request.type(),
      request.endpoint(),
      request.accessKey(),
      request.secretKey(),
      request.bucketName(),
      request.address(),
      request.storagePath()
    );
    return ResponseEntity.ok(config);
  }

  @DeleteMapping("/configs/{id}")
  @RequiresPermissions("storage:delete")
  public ResponseEntity<Void> deleteStorageConfig(@PathVariable Long id) {
    StorageConfig config = storageService.getConfig(id);
    storageService.deleteConfig(config);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/configs/{id}:markAsDefault")
  @RequiresPermissions("storage:markAsDefault")
  public ResponseEntity<Void> markAsDefaultStorageConfig(@PathVariable Long id) {
    StorageConfig config = storageService.getConfig(id);
    storageService.markAsDefault(config);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/upload")
  public ResponseEntity<List<UploadResponse>> upload(@RequestParam(value = "storageId", required = false) String storageId,
                                                     @RequestParam("files") MultipartFile[] files) throws IOException {
    List<UploadResponse> responses = new ArrayList<>();
    for (MultipartFile file : files) {
      String originalFilename = file.getOriginalFilename();
      String url = storageService.store(storageId, file.getInputStream(), file.getSize(), file.getContentType(), originalFilename);
      responses.add(new UploadResponse(url));
    }
    return ResponseEntity.ok(responses);
  }


  @PostMapping("/upload/imageSave")
  public ResponseEntity<Image> upload(@RequestParam(value = "storageId", required = false) String storageId,
                                      @RequestBody UploadResponseByUser uploadResponseByUser) {
    User user = uploadResponseByUser.user();
    Image image = uploadResponseByUser.image();
    Image result = imageService.createImage(image.getTitle(), image.getOverview(), image.getUrl(), image.getPosterPath(), image.getLabels());
    userService.updateUserCount(user.getId());
    return ResponseEntity.ok(result);
  }

  @GetMapping("/fetch/{key:.+}")
  public ResponseEntity<Resource> fetch(@PathVariable String key) {
    if (key == null) {
      return ResponseEntity.notFound().build();
    }
    if (key.contains("../")) {
      return ResponseEntity.badRequest().build();
    }
    StorageFile storageFile = storageService.getByKey(key);
    if (storageFile == null) {
      return ResponseEntity.notFound().build();
    }
    String type = storageFile.getType();
    MediaType mediaType = MediaType.parseMediaType(type);
    Resource resource = storageService.loadAsResource(key);
    if (resource == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().contentType(mediaType).body(resource);
  }

  @GetMapping("/download/{key:.+}")
  public ResponseEntity<Resource> download(@PathVariable String key) {
    if (key == null) {
      return ResponseEntity.notFound().build();
    }
    if (key.contains("../")) {
      return ResponseEntity.badRequest().build();
    }
    StorageFile storageFile = storageService.getByKey(key);
    if (storageFile == null) {
      return ResponseEntity.notFound().build();
    }
    String type = storageFile.getType();
    MediaType mediaType = MediaType.parseMediaType(type);
    Resource file = storageService.loadAsResource(key);
    if (file == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().contentType(mediaType)
      .header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + storageFile.getName() + "\"")
      .body(file);
  }


  @DeleteMapping("/files/{key:.+}")
  public ResponseEntity<Void> delete(@PathVariable String key) {
    storageService.delete(key);
    return ResponseEntity.noContent().build();
  }

  record StorageConfigRequest(String name,
                              Type type,
                              String endpoint,
                              String accessKey,
                              String secretKey,
                              String bucketName,
                              String address,
                              String storagePath
  ) {

  }

  record UploadResponse(String url) {
  }

  record UploadResponseByUser(Image image, User user) {
  }

}
