package tech.wetech.admin3.sys.service.dto;

import tech.wetech.admin3.sys.model.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author cjbi
 */
public record UserinfoDTO(User.Gender gender,Long imageCount, String token, String type, User.State state, Organization organization, Long userId, String username, String avatar,
                          Credential credential, Set<String> permissions, Role roleList, List<Label> labels) implements Serializable {

  public record Credential(String identifier, UserCredential.IdentityType type) {
  }

}
