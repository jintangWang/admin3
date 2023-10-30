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

  @Query("select distinct r FROM Image r left join r.labels u where r.isVip =:isvip and u.id in(:labelIds)")
  List<Image> findImages(List<Long> labelIds,boolean isvip);

  @Query("select distinct r FROM Image r left join r.labels u where r.isVip =:isvip and u.id is null")
  List<Image> findImagesLableIsNull(boolean isvip);
}
