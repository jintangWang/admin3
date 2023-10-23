package tech.wetech.admin3.sys.service.dto;

import tech.wetech.admin3.sys.model.Organization;
import tech.wetech.admin3.sys.model.Role;
import tech.wetech.admin3.sys.model.User;
import tech.wetech.admin3.sys.model.UserCredential;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author cjbi
 */
public record UserinfoDTO(String token, User.State state, Organization organization, Long userId, String username, String avatar,
                          Credential credential, Set<String> permissions, List<Role> roleList) implements Serializable {

  public record Credential(String identifier, UserCredential.IdentityType type) {
  }

}
