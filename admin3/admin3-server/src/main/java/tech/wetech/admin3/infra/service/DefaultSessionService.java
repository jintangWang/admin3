package tech.wetech.admin3.infra.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tech.wetech.admin3.common.CommonResultStatus;
import tech.wetech.admin3.common.Constants;
import tech.wetech.admin3.common.DomainEventPublisher;
import tech.wetech.admin3.common.SessionItemHolder;
import tech.wetech.admin3.sys.event.UserLoggedIn;
import tech.wetech.admin3.sys.event.UserLoggedOut;
import tech.wetech.admin3.sys.exception.UserException;
import tech.wetech.admin3.sys.model.Label;
import tech.wetech.admin3.sys.model.Role;
import tech.wetech.admin3.sys.model.User;
import tech.wetech.admin3.sys.model.UserCredential;
import tech.wetech.admin3.sys.repository.UserCredentialRepository;
import tech.wetech.admin3.sys.service.LabelService;
import tech.wetech.admin3.sys.service.RoleService;
import tech.wetech.admin3.sys.service.SessionService;
import tech.wetech.admin3.sys.service.dto.UserinfoDTO;

import java.util.List;
import java.util.UUID;

import static tech.wetech.admin3.sys.model.UserCredential.IdentityType.PASSWORD;

/**
 * @author cjbi
 */
@Service
public class DefaultSessionService implements SessionService {

  private final UserCredentialRepository userCredentialRepository;

  private final SessionManager sessionManager;

  private final RoleService roleService;

  private final LabelService labelService;

  public DefaultSessionService(UserCredentialRepository userCredentialRepository, SessionManager sessionManager, RoleService roleService, LabelService labelService) {
    this.labelService = labelService;
    this.userCredentialRepository = userCredentialRepository;
    this.sessionManager = sessionManager;
    this.roleService = roleService;
  }


  @Override
  public UserinfoDTO login(String username, String password) {
    UserCredential credential = userCredentialRepository.findCredential(username, PASSWORD)
      .orElseThrow(() -> new UserException(CommonResultStatus.UNAUTHORIZED, "密码不正确"));
    if (credential.doCredentialMatch(password)) {
      User user = credential.getUser();
      if (user.isLocked()) {
        throw new UserException(CommonResultStatus.UNAUTHORIZED, "用户已经停用，请与管理员联系");
      }
      String token = UUID.randomUUID().toString().replace("-", "");
      Role roleUsers = roleService.findRoleUsers(user.getId());
      List<Label> labels = labelService.findLabelUsers(user.getId());

      UserinfoDTO userinfoEvent = new UserinfoDTO(user.getGender(),user.getImageCount(),token, user.getType(), user.getState(), user.getOrganization(), user.getId(), user.getUsername(), user.getAvatar(), new UserinfoDTO.Credential(credential.getIdentifier(), credential.getIdentityType()), user.findPermissions(), roleUsers,labels);
      sessionManager.store(token, credential, userinfoEvent);
      SessionItemHolder.setItem(Constants.SESSION_CURRENT_USER, userinfoEvent);
      DomainEventPublisher.instance().publish(new UserLoggedIn(userinfoEvent, getClientIP()));
      return userinfoEvent;
    } else {
      throw new UserException(CommonResultStatus.UNAUTHORIZED, "密码不正确");
    }
  }

  public String getClientIP() {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    String ipAddress = request.getHeader("X-FORWARDED-FOR");
    if (ipAddress == null) {
      ipAddress = request.getRemoteAddr();
    }
    return ipAddress;
  }

  @Override
  public void logout(String token) {
    UserinfoDTO userinfo = (UserinfoDTO) sessionManager.get(token);
    sessionManager.invalidate(token);
    DomainEventPublisher.instance().publish(new UserLoggedOut(userinfo));
  }

  @Override
  public boolean isLogin(String token) {
    return sessionManager.get(token) != null;
  }

  @Override
  public UserinfoDTO getLoginUserInfo(String token) {
    return (UserinfoDTO) sessionManager.get(token);
  }

  @Override
  public void refresh() {
    sessionManager.refresh();
  }
}
