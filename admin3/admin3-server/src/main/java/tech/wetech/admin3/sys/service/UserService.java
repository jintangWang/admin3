package tech.wetech.admin3.sys.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.wetech.admin3.common.*;
import tech.wetech.admin3.sys.event.UserCreated;
import tech.wetech.admin3.sys.event.UserDeleted;
import tech.wetech.admin3.sys.event.UserUpdated;
import tech.wetech.admin3.sys.exception.UserException;
import tech.wetech.admin3.sys.model.*;
import tech.wetech.admin3.sys.repository.UserCredentialRepository;
import tech.wetech.admin3.sys.repository.UserRepository;
import tech.wetech.admin3.sys.service.dto.OrgUserDTO;
import tech.wetech.admin3.sys.service.dto.PageDTO;
import tech.wetech.admin3.sys.service.dto.UserinfoDTO;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static tech.wetech.admin3.common.CommonResultStatus.RECORD_NOT_EXIST;

/**
 * @author cjbi
 */
@Service
public class UserService {

  private final UserRepository userRepository;

  UserCredentialRepository userCredentialRepository;

  public UserService(UserRepository userRepository,  UserCredentialRepository userCredentialRepository) {
    this.userRepository = userRepository;
    this.userCredentialRepository = userCredentialRepository;
  }

  @Transactional
  public User createUserLabel(String password,String username, String avatar, User.Gender gender, User.State state, Organization organization, String type, Set<Label> labels) {
    User user = new User();
    user.setUsername(username);
    user.setAvatar(avatar);
    user.setGender(gender);
    user.setState(state);
    user.setCreatedTime(LocalDateTime.now());
    user.setOrganization(organization);
    user.setType(type);
    user.setLabels(labels);
    UserCredential userCredential = new UserCredential();
    userCredential.setIdentityType(UserCredential.IdentityType.PASSWORD);
    userCredential.setIdentifier(username);
    try {
      userCredential.setCredential(SecurityUtil.md5(username, password));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    HashSet<UserCredential> objects = new HashSet<>();
    objects.add(userCredential);
    userCredential.setUser(user);
    user.setCredentials(objects);

    Role role = new Role();
    role.setId(3L);
    role.setAvailable(true);
    user.setRoles(role);
    user = userRepository.save(user);
    userCredentialRepository.save(userCredential);
    DomainEventPublisher.instance().publish(new UserCreated(user));
    return user;
  }

  @Transactional
  public User createUser(String password,String username, String avatar, User.Gender gender, User.State state, Organization organization, String type) {
    User user = new User();
    user.setUsername(username);
    user.setAvatar(avatar);
    user.setGender(gender);
    user.setState(state);
    user.setCreatedTime(LocalDateTime.now());
    user.setOrganization(organization);
    user.setType(type);

    Role role = new Role();
    role.setId(3L);
    role.setAvailable(true);
    user.setRoles(role);
    UserCredential userCredential = new UserCredential();
    userCredential.setIdentityType(UserCredential.IdentityType.PASSWORD);
    userCredential.setIdentifier(username);
    userCredential.setUser(user);
    try {
      userCredential.setCredential(SecurityUtil.md5(username, password));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    HashSet<UserCredential> objects = new HashSet<>();
    objects.add(userCredential);
    user.setCredentials(objects);
    user = userRepository.save(user);
    userCredentialRepository.save(userCredential);
    DomainEventPublisher.instance().publish(new UserCreated(user));
    return user;
  }

  public Set<User> findUserByIds(Set<Long> userIds) {
    return userRepository.findByIds(userIds);
  }

  public User findUserById(Long userId) {
    return userRepository.findById(userId)
      .orElseThrow(() -> new BusinessException(RECORD_NOT_EXIST));
  }

  public User findUserByUserName(String userName) {
    return userRepository.finduserByName(userName).get(0);
  }

  public PageDTO<OrgUserDTO> findOrgUsers(Pageable pageable, String username, User.State state, Organization organization) {
    Page<User> page = userRepository.findOrgUsers(pageable, username, state, organization, organization.makeSelfAsParentIds());
    return new PageDTO<>(page.getContent().stream().map(u ->
        new OrgUserDTO(u.getId(), u.getUsername(), u.getAvatar(), u.getGender(), u.getState(), u.getOrgFullName(), u.getCreatedTime()))
      .collect(Collectors.toList()), page.getTotalElements());
  }

  public boolean existsUsers(Organization organization) {
    String orgParentIds = organization.makeSelfAsParentIds();
    return userRepository.countOrgUsers(organization, orgParentIds) > 0;
  }


  @Transactional
  public User updateUser(String type,Long userId, String avatar, User.Gender gender, User.State state, Organization organization,Set<Label> labels) {
    User user = findUserById(userId);
    user.setAvatar(avatar);
    user.setGender(gender);
    user.setState(state);
    user.setOrganization(organization);
    user.setType(type);
    user.setLabels(labels);
    user = userRepository.save(user);
    DomainEventPublisher.instance().publish(new UserUpdated(user));
    return user;
  }

  @Transactional
  public User disableUser(Long userId) {
    UserinfoDTO userInfo = (UserinfoDTO) SessionItemHolder.getItem(Constants.SESSION_CURRENT_USER);
    if (Objects.equals(userInfo.userId(), userId)) {
      throw new UserException(CommonResultStatus.PARAM_ERROR, "不能禁用自己");
    }
    User user = findUserById(userId);
    user.setState(User.State.LOCKED);
    return userRepository.save(user);
  }

  @Transactional
  public User enableUser(Long userId) {
    User user = findUserById(userId);
    user.setState(User.State.NORMAL);
    return userRepository.save(user);
  }

  public PageDTO<User> findUsers(Pageable pageable, User user) {
    Page<User> page = userRepository.findAll(Example.of(user), pageable);
    return new PageDTO<>(page.getContent(), page.getTotalElements());
  }

  @Transactional
  public void delete(Long userId) {
    User user = findUserById(userId);
    userRepository.delete(user);
    DomainEventPublisher.instance().publish(new UserDeleted(user));
  }

  public List<User> findUsersByName(String name) {
    return userRepository.finduserByName(name);
  }

  public void updateUserCount(Long userId) {
    User user = findUserById(userId);
    user.setImageCount(user.getImageCount()+1);
    user = userRepository.save(user);
  }

  public void updateUser(User userById) {
    User user = findUserById(userById.getId());
    user.setCredentials(userById.getCredentials());
    user = userRepository.save(user);
  }
}
