package tech.wetech.admin3.sys.event;

import tech.wetech.admin3.common.DomainEvent;
import tech.wetech.admin3.sys.service.dto.UserinfoDTO;
import tech.wetech.admin3.sys.service.dto.UserinfoDTOV2;

/**
 * @author cjbi
 */
public record UserLoggedIn(UserinfoDTO userinfo, String ip) implements DomainEvent {
}
