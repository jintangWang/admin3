package tech.wetech.admin3.sys.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.wetech.admin3.common.BusinessException;
import tech.wetech.admin3.common.CollectionUtils;
import tech.wetech.admin3.common.CommonResultStatus;
import tech.wetech.admin3.common.DomainEventPublisher;
import tech.wetech.admin3.sys.event.RoleCreated;
import tech.wetech.admin3.sys.event.RoleDeleted;
import tech.wetech.admin3.sys.event.RoleUpdated;
import tech.wetech.admin3.sys.model.Resource;
import tech.wetech.admin3.sys.model.Role;
import tech.wetech.admin3.sys.model.User;
import tech.wetech.admin3.sys.repository.RoleRepository;
import tech.wetech.admin3.sys.repository.UserRepository;
import tech.wetech.admin3.sys.service.dto.PageDTO;
import tech.wetech.admin3.sys.service.dto.RoleDTO;
import tech.wetech.admin3.sys.service.dto.RoleUserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author cjbi
 */
@Service
public class RoleService {

  private final RoleRepository roleRepository;

  private final UserRepository userRepository;

  public RoleService(RoleRepository roleRepository,UserRepository userRepository) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
  }

  public List<RoleDTO> findRoles() {
    return roleRepository.findAll().stream().map(role -> {
          Set<Resource> resources = role.getResources();
          return new RoleDTO(
              role.getId(),
              role.getName(),
              role.getDescription(),
              role.getAvailable(),
            CollectionUtils.isEmpty(resources)?new ArrayList<>():
              resources.stream().map(Resource::getId).toList());
        }
      )
      .toList();
  }

  public Role findRoleById(Long roleId) {
    return roleRepository.findById(roleId)
      .orElseThrow(() -> new BusinessException(CommonResultStatus.RECORD_NOT_EXIST));
  }

  @Transactional
  public Role createRole(String name, String description) {
    Role role = new Role();
    role.setName(name);
    role.setDescription(description);
    role = roleRepository.save(role);
    DomainEventPublisher.instance().publish(new RoleCreated(role));
    return role;
  }

  public Role changeResources(Long roleId, Set<Resource> resources) {
    Role role = findRoleById(roleId);
    role.setResources(resources);
    role = roleRepository.save(role);
    DomainEventPublisher.instance().publish(new RoleUpdated(role));
    return role;
  }

  public Role changeUsers(Long roleId, Set<User> users) {
    Optional<Role> byId = roleRepository.findById(roleId);
    users.forEach(p->userRepository.updateRole(roleId,p.getId()));
    return byId.get();
  }

  @Transactional
  public Role updateRole(Long roleId, String name, String description) {
    Role role = findRoleById(roleId);
    role.setName(name);
    role.setDescription(description);
    role = roleRepository.save(role);
    DomainEventPublisher.instance().publish(new RoleUpdated(role));
    return role;
  }

  @Transactional
  public void deleteRoleById(Long roleId) {
    Role role = findRoleById(roleId);
    roleRepository.delete(role);
    DomainEventPublisher.instance().publish(new RoleDeleted(role));
  }

  public PageDTO<RoleUserDTO> findRoleUsers(Long roleId, Pageable pageable) {
    Page<User> page = roleRepository.findRoleUsers(roleId, pageable);
    return new PageDTO<>(page.getContent().stream()
      .map(u -> new RoleUserDTO(u.getId(), u.getUsername(), u.getAvatar(), u.getGender(), u.getState(), u.getRoles(), u.getCreatedTime()))
      .collect(Collectors.toList()),
      page.getTotalElements());
  }

  public Role findRoleUsers(Long userById) {
    Role roles = userRepository.findRoleById(userById);
    return roles;
  }

}
