package tech.wetech.admin3.sys.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.wetech.admin3.common.BusinessException;
import tech.wetech.admin3.common.CommonResultStatus;
import tech.wetech.admin3.sys.model.Label;
import tech.wetech.admin3.sys.model.Resource;
import tech.wetech.admin3.sys.model.User;
import tech.wetech.admin3.sys.repository.LabelRepository;
import tech.wetech.admin3.sys.service.dto.LabelUserDTO;
import tech.wetech.admin3.sys.service.dto.PageDTO;
import tech.wetech.admin3.sys.service.dto.RoleUserDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author cjbi
 */
@Service
public class LabelService {

  private final LabelRepository labelRepository;

  public LabelService(LabelRepository labelRepository) {
    this.labelRepository = labelRepository;
  }

  public List<Label> findLabels() {
    return labelRepository.findAll();
  }

  public Label findLabelById(Long labelId) {
    return labelRepository.findById(labelId)
      .orElseThrow(() -> new BusinessException(CommonResultStatus.RECORD_NOT_EXIST));
  }

  @Transactional
  public Label createLabel(String name, String description) {
    Label label = new Label();
    label.setName(name);
    label.setDescription(description);
    label = labelRepository.save(label);
    return label;
  }

  public Label changeResources(Long LabelId, Set<Resource> resources) {
    Label label = findLabelById(LabelId);
    label.setResources(resources);
    label = labelRepository.save(label);
    return label;
  }

  public Label changeUsers(Long labelId, Set<User> users) {
    Label label = findLabelById(labelId);
    label.setUsers(users);
    label = labelRepository.save(label);
    return label;
  }

  @Transactional
  public Label updateLabel(Long labelId, String name, String description) {
    Label label = findLabelById(labelId);
    label.setName(name);
    label.setDescription(description);
    label = labelRepository.save(label);
    return label;
  }

  @Transactional
  public void deleteLabelById(Long labelId) {
    Label label = findLabelById(labelId);
    labelRepository.delete(label);
  }

  public PageDTO<LabelUserDTO> findLabelUsers(Long labelId, Pageable pageable) {
    Page<User> page = labelRepository.findLabelUsers(labelId, pageable);
    return new PageDTO<>(page.getContent().stream()
      .map(u -> new LabelUserDTO(u.getId(), u.getUsername(), u.getAvatar(), u.getGender(), u.getState(), u.getLabels(), u.getCreatedTime()))
      .collect(Collectors.toList()),
      page.getTotalElements());
  }

  public List<Label> findLabelUsers(Long userById) {
    List<Label> labels = labelRepository.labelById(userById);
    return labels;
  }

}
