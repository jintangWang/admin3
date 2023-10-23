package tech.wetech.admin3.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.wetech.admin3.common.authz.RequiresPermissions;
import tech.wetech.admin3.sys.model.Label;
import tech.wetech.admin3.sys.model.Resource;
import tech.wetech.admin3.sys.model.User;
import tech.wetech.admin3.sys.service.LabelService;
import tech.wetech.admin3.sys.service.ResourceService;
import tech.wetech.admin3.sys.service.UserService;
import tech.wetech.admin3.sys.service.dto.LabelUserDTO;
import tech.wetech.admin3.sys.service.dto.PageDTO;

import java.util.List;
import java.util.Set;

/**
 * @author cjbi
 */
@RestController
@RequestMapping("/label")
public class LabelController {

  private final UserService userService;
  private final LabelService labelService;
  private final ResourceService resourceService;

  public LabelController(UserService userService, LabelService labelService, ResourceService resourceService) {
    this.userService = userService;
    this.labelService = labelService;
    this.resourceService = resourceService;
  }

  @GetMapping("/getAll")
  public ResponseEntity<List<Label>> findlabels() {
    return ResponseEntity.ok(labelService.findLabels());
  }

  @GetMapping("/{labelId}/users")
  public ResponseEntity<PageDTO<LabelUserDTO>> findLabelUsers(@PathVariable Long labelId, Pageable pageable) {
    return ResponseEntity.ok(labelService.findLabelUsers(labelId, pageable));
  }

  @PostMapping
  public ResponseEntity<Label> createlabel(@RequestBody @Valid LabelRequest request) {
    return new ResponseEntity<>(labelService.createLabel(request.name(), request.description()), HttpStatus.CREATED);
  }

  @PutMapping("/{labelId}/resources")
  public ResponseEntity<Label> changeResources(@PathVariable Long labelId, @RequestBody @Valid LabelResourceRequest request) {
    Set<Resource> resources = resourceService.findResourceByIds(request.resourceIds());
    return ResponseEntity.ok(labelService.changeResources(labelId, resources));
  }

  @PutMapping("/{labelId}/users")
  public ResponseEntity<Label> changeUsers(@PathVariable Long labelId, @RequestBody @Valid LabelUserRequest request) {
    Set<User> users = userService.findUserByIds(request.userIds());
    return ResponseEntity.ok(labelService.changeUsers(labelId, users));
  }

  @PutMapping("/{labelId}")
  public ResponseEntity<Label> updatelabel(@PathVariable Long labelId, @RequestBody @Valid LabelRequest request) {
    return ResponseEntity.ok(labelService.updateLabel(labelId, request.name(), request.description()));
  }

  @DeleteMapping("/{labelId}")
  public ResponseEntity<Void> deletelabel(@PathVariable Long labelId) {
    labelService.deleteLabelById(labelId);
    return ResponseEntity.noContent().build();
  }

  record LabelUserRequest(Set<Long> userIds) {
  }

  record LabelResourceRequest(Set<Long> resourceIds) {
  }

  record LabelRequest(@NotBlank String name, String description) {
  }

}
