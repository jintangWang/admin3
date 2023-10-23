package tech.wetech.admin3.sys.service;

import org.springframework.stereotype.Service;
import tech.wetech.admin3.sys.service.dto.UserinfoDTO;
import tech.wetech.admin3.sys.service.dto.UserinfoDTOV2;

/**
 * @author cjbi
 */
@Service
public interface SessionService {

  UserinfoDTOV2 login(String username, String password);

  void logout(String token);

  boolean isLogin(String token);

  UserinfoDTO getLoginUserInfo(String token);

  void refresh();

}
