package tech.wetech.admin3.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.wetech.admin3.sys.model.Image;
import tech.wetech.admin3.sys.model.Label;
import tech.wetech.admin3.sys.repository.LabelRepository;
import tech.wetech.admin3.sys.service.ImageService;
import tech.wetech.admin3.sys.service.dto.ImageDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author cjbi
 */
@RestController
@RequestMapping("/images")
public class ImageController {

  private final ImageService imageService;

  private final LabelRepository labelRepository;

  public ImageController(ImageService imageService,LabelRepository labelRepository) {
    this.imageService = imageService;
    this.labelRepository = labelRepository;
  }


  @PostMapping
  public ResponseEntity<Image> createImage(@RequestBody @Valid ImageRequest request) {
    return new ResponseEntity<>(imageService.createImage(request.title(), request.overview(), request.url(), request.posterPath(), request.labels(),false), HttpStatus.CREATED);
  }

  @PutMapping("/{imageId}")
  public ResponseEntity<Image> updateImage(@PathVariable Long imageId, @RequestBody @Valid ImageRequest request) {
    return ResponseEntity.ok(imageService.updateImage(imageId, request.title(), request.overview(), request.url(), request.posterPath(), request.labels()));
  }

  @DeleteMapping("/{ImageId}")
  public ResponseEntity<Void> deleteImage(@PathVariable Long ImageId) {
    imageService.deleteImageById(ImageId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/getAllByLabelIds")
  public ResponseEntity<List<ImageDTO>> findlabels(@RequestBody List<Long> labelIds) {
    List<Image> images = imageService.findImages(labelIds);
    List<ImageDTO> result = new ArrayList<>();
    for (Image image : images) {
      Set<Label> labelByImages = labelRepository.findLabelByImages(image.getId());
      ImageDTO imageDTO = new ImageDTO(image.getId(),image.getTitle(),image.getOverview(),image.getUrl(),image.getCreatetime(),image.getIsVip(),image.getPosterPath(),labelByImages);
      result.add(imageDTO);
    }
    return ResponseEntity.ok(result);
  }


  record ImageRequest(@NotBlank String title, String overview, String url, String posterPath, Set<Label> labels) {

  }

}
