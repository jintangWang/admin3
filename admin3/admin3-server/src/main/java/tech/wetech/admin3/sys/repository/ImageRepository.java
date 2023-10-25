package tech.wetech.admin3.sys.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.wetech.admin3.sys.model.Image;
import tech.wetech.admin3.sys.model.Resource;

import java.util.List;
import java.util.Set;

/**
 * @author cjbi
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

  @Query("from Image where id in (:imageIds)")
  Set<Image> findByIds(Set<Long> imageIds);

  @Query("select distinct r FROM Image r join r.labels u where u.id in(:labelIds) ORDER BY ?#{#pageable}")
  Page<Image> findImages(Pageable pageable, List<Long> labelIds);
}
