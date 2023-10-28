package tech.wetech.admin3.sys.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.wetech.admin3.sys.model.Image;
import tech.wetech.admin3.sys.model.Label;
import tech.wetech.admin3.sys.model.Role;
import tech.wetech.admin3.sys.model.User;

import java.util.List;
import java.util.Set;

/**
 * @author cjbi
 */
@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

  @Query("select distinct u FROM User u join u.roles r where r.id=:labelId")
  Page<User> findLabelUsers(Long labelId, Pageable pageable);

  @Query("select distinct r FROM Label r join r.users u where u.id=:userId")
  List<Label> labelById(Long userId);

  @Query("select distinct r FROM Label r join r.images i where i.id=:imageId")
  Set<Label> findLabelByImages(Long imageId);

}
