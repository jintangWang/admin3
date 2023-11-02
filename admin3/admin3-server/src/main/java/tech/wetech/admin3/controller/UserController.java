package tech.wetech.admin3.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.wetech.admin3.common.CollectionUtils;
import tech.wetech.admin3.common.CommonResultStatus;
import tech.wetech.admin3.common.authz.RequiresPermissions;
import tech.wetech.admin3.sys.exception.UserException;
import tech.wetech.admin3.sys.model.*;
import tech.wetech.admin3.sys.repository.UserCredentialRepository;
import tech.wetech.admin3.sys.service.LabelService;
import tech.wetech.admin3.sys.service.OrganizationService;
import tech.wetech.admin3.sys.service.RoleService;
import tech.wetech.admin3.sys.service.UserService;
import tech.wetech.admin3.sys.service.dto.PageDTO;
import tech.wetech.admin3.sys.service.dto.UserinfoDTO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static tech.wetech.admin3.sys.model.UserCredential.IdentityType.PASSWORD;

/**
 * @author cjbi
 */
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/users")
public class UserController {

  private final OrganizationService organizationService;
  private final UserService userService;

  private final LabelService labelService;

  @Autowired
  private UserCredentialRepository userCredentialRepository;

  @Autowired
  private RoleService roleService;

  public UserController(LabelService labelService,OrganizationService organizationService, UserService userService) {
    this.organizationService = organizationService;
    this.userService = userService;
    this.labelService = labelService;
  }

  @RequiresPermissions("user:view")
  @GetMapping
  public ResponseEntity<PageDTO<User>> findUsers(Pageable pageable, User user) {
    return ResponseEntity.ok(userService.findUsers(pageable, user));
  }

  @GetMapping("/validate/{name}")
  public ResponseEntity<List<User>> findUsers(@PathVariable("name") String name) {
    return ResponseEntity.ok(userService.findUsersByName(name));
  }

  @GetMapping("/userById/{id}")
  public ResponseEntity<UserinfoDTO> findUsersById(@PathVariable("id") Long id) {
    User user = userService.findUserById(id);
    UserCredential credential = userCredentialRepository.findCredential(user.getUsername(), PASSWORD)
      .orElseThrow(() -> new UserException(CommonResultStatus.UNAUTHORIZED, "密码不正确"));    List<Label> labelUsers = labelService.findLabelUsers(id);
    Role roleUsers = roleService.findRoleUsers(user.getId());
    return ResponseEntity.ok(new UserinfoDTO(user.getGender(),user.getImageCount(),null, user.getType(), user.getState(), user.getOrganization(), user.getId(), user.getUsername(), user.getAvatar(), new UserinfoDTO.Credential(credential.getIdentifier(), credential.getIdentityType()), user.findPermissions(), roleUsers,labelUsers));
  }

  @RequiresPermissions("user:create")
  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest request) {
    Organization organization = organizationService.findOrganization(request.organizationId());
    User user = null;
    if (CollectionUtils.isEmpty(request.label())) {
      user = userService.createUser("123456",request.username(), null, User.Gender.MALE, User.State.NORMAL, organization, null);
    } else {
      user = userService.createUserLabel("123456",request.username(), null, User.Gender.MALE, User.State.NORMAL, organization, null, request.label());
    }
    return new ResponseEntity<>(user,HttpStatus.CREATED);
  }

  @RequiresPermissions("user:update")
  @PutMapping("/{userId}")
  public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody @Valid UpdateUserRequest request) {
    Organization organization = organizationService.findOrganization(request.organizationId());
    return ResponseEntity.ok(userService.updateUser(request.type(), userId, request.avatar(), request.gender(), User.State.NORMAL, organization,request.label()));
  }

  @RequiresPermissions("user:update")
  @PostMapping("/{userId}:disable")
  public ResponseEntity<User> disableUser(@PathVariable Long userId) {
    return ResponseEntity.ok(userService.disableUser(userId));
  }

  @RequiresPermissions("user:update")
  @PostMapping("/{userId}:enable")
  public ResponseEntity<User> enableUser(@PathVariable Long userId) {
    return ResponseEntity.ok(userService.enableUser(userId));
  }

  @RequiresPermissions("user:delete")
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  record CreateUserRequest(@NotBlank String username, @NotNull User.Gender gender,
                           @NotBlank String avatar, Long organizationId, Set<Label> label) {
  }

  record UpdateUserRequest(@NotNull User.Gender gender,
                           @NotBlank String avatar, Long organizationId, String type, Set<Label> label) {
  }

}
