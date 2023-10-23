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
import tech.wetech.admin3.common.SecurityUtil;
import tech.wetech.admin3.sys.model.Organization;
import tech.wetech.admin3.sys.model.User;
import tech.wetech.admin3.sys.model.UserCredential;
import tech.wetech.admin3.sys.service.SessionService;
import tech.wetech.admin3.sys.service.UserService;
import tech.wetech.admin3.sys.service.dto.UserinfoDTO;

import java.security.NoSuchAlgorithmException;

/**
 * @author cjbi
 */
@RestController
public class LoginController {

  private final SessionService sessionService;

  private final UserService userService;

  public LoginController(SessionService sessionService, UserService userService) {
    this.sessionService = sessionService;
    this.userService = userService;
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

  @SecurityRequirement(name = "bearerAuth")
  @GetMapping("/register")
  public ResponseEntity<UserinfoDTO> register(@RequestBody @Valid LoginRequest request) {
    Organization organization = new Organization();
    organization.setId(1L);
    userService.createUser(request.username(), null, User.Gender.MALE, User.State.NORMAL, organization, request.type);
    UserCredential userCredential = new UserCredential();
    userCredential.setIdentifier(request.username());
    userCredential.setIdentityType(UserCredential.IdentityType.PASSWORD);
    try {
      userCredential.setCredential(SecurityUtil.md5(request.username(), request.password));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("加密失败：：：：{}", e);
    }
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  record LoginRequest(@NotBlank String username, @NotBlank String password,String type) {
  }


}
