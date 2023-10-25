package tech.wetech.admin3.sys.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.wetech.admin3.common.BusinessException;
import tech.wetech.admin3.common.CollectionUtils;
import tech.wetech.admin3.common.CommonResultStatus;
import tech.wetech.admin3.sys.model.Image;
import tech.wetech.admin3.sys.model.Label;
import tech.wetech.admin3.sys.repository.ImageRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author cjbi
 */
@Service
public class ImageService {

  private final ImageRepository imageRepository;

  public ImageService(ImageRepository imageRepository) {
    this.imageRepository = imageRepository;
  }

  public Set<Image> findImageByIds(Set<Long> ImageIds) {
    return imageRepository.findByIds(ImageIds);
  }

  public Image findImageById(Long ImageId) {
    return imageRepository.findById(ImageId)
      .orElseThrow(() -> new BusinessException(CommonResultStatus.RECORD_NOT_EXIST));
  }

  @Transactional
  public Image createImage(String title, String overview, String url, String posterPath, Set<Label> labels) {
    Image image = new Image();
    image.setTitle(title);
    image.setCreatetime(LocalDateTime.now());
    image.setUrl(url);
    image.setOverview(overview);
    image.setPosterPath(posterPath);
    if(!CollectionUtils.isEmpty(labels)){
      image.setLabels(labels);
    }
    image = imageRepository.save(image);
    return image;
  }

  @Transactional
  public Image updateImage(Long imageId, String title, String overview, String url,String posterPath, Set<Label> labels) {
    Image image = findImageById(imageId);
    image.setTitle(title);
    image.setUrl(url);
    image.setOverview(overview);
    image.setPosterPath(posterPath);
    if(!CollectionUtils.isEmpty(labels)){
      image.setLabels(labels);
    }
    image = imageRepository.save(image);
    return image;
  }

  @Transactional
  public void deleteImageById(Long ImageId) {
    Image image = findImageById(ImageId);
    imageRepository.delete(image);
  }


  public List<Image> findImages(Pageable pageable,List<Long> labelIds) {
    return imageRepository.findImages(pageable,labelIds);
  }
}
