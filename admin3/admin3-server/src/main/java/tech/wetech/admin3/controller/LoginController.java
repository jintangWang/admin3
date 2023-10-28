package tech.wetech.admin3.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.wetech.admin3.common.CollectionUtils;
import tech.wetech.admin3.common.CommonResultStatus;
import tech.wetech.admin3.common.SecurityUtil;
import tech.wetech.admin3.sys.exception.UserException;
import tech.wetech.admin3.sys.model.Label;
import tech.wetech.admin3.sys.model.Organization;
import tech.wetech.admin3.sys.model.User;
import tech.wetech.admin3.sys.model.UserCredential;
import tech.wetech.admin3.sys.repository.UserCredentialRepository;
import tech.wetech.admin3.sys.service.SessionService;
import tech.wetech.admin3.sys.service.UserService;
import tech.wetech.admin3.sys.service.dto.UserinfoDTO;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static tech.wetech.admin3.sys.model.UserCredential.IdentityType.PASSWORD;

/**
 * @author cjbi
 */
@RestController
public class LoginController {

  private final SessionService sessionService;

  private final UserService userService;

  private final UserCredentialRepository userCredentialRepository;

  public LoginController(SessionService sessionService, UserService userService,UserCredentialRepository userCredentialRepository) {
    this.sessionService = sessionService;
    this.userService = userService;
    this.userCredentialRepository = userCredentialRepository;
  }

  @PostMapping("/login")
  private ResponseEntity<UserinfoDTO> login(@RequestBody @Valid LoginRequest request) {
    return ResponseEntity.ok(sessionService.login(request.username(), request.password()));
  }

  @SecurityRequirement(name = "bearerAuth")
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request) {
    String token = request.getHeader("Authorization").replace("Bearer", "").trim();
    sessionService.logout(token);
    return ResponseEntity.ok().build();
  }

  @SecurityRequirement(name = "bearerAuth")
  @GetMapping("/userinfo")
  public ResponseEntity<UserinfoDTO> userInfo(HttpServletRequest request) {
    String token = request.getHeader("Authorization").replace("Bearer", "").trim();
    return ResponseEntity.ok(sessionService.getLoginUserInfo(token));
  }

  @PostMapping("/register")
  public ResponseEntity<UserinfoDTO> register(@RequestBody @Valid LoginRequest request) {
    Organization organization = new Organization();
    organization.setId(1L);
    if(CollectionUtils.isEmpty(request.label())){
      userService.createUser(request.password(),request.username(), null, User.Gender.MALE, User.State.NORMAL, organization, request.type());
    }else{
      userService.createUserLabel(request.password(),request.username(), null, User.Gender.MALE, User.State.NORMAL, organization, request.type(),request.label());
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PostMapping("/updatePassword")
  public ResponseEntity<UserinfoDTO> updatePassword(@RequestBody @Valid PaswordRequest request) {
    User userById = userService.findUserByUserName(request.username());
    UserCredential credential = userCredentialRepository.findCredential(request.username(), PASSWORD)
      .orElseThrow(() -> new UserException(CommonResultStatus.UNAUTHORIZED, "密码不正确"));
    credential.setIdentityType(UserCredential.IdentityType.PASSWORD);
    try {
      credential.setCredential(SecurityUtil.md5(request.username(), request.password));
      HashSet<UserCredential> objects = new HashSet<>();
      objects.add(credential);
      userById.setCredentials(objects);
      userCredentialRepository.update(credential.getCredential(),userById.getId());
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("加密失败：：：：{}", e);
    }
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
  record LoginRequest(@NotBlank String username, @NotBlank String password, String type, Set<Label> label) {
  }

  record PaswordRequest(@NotBlank String username, @NotBlank String password,String userid) {
  }

}
