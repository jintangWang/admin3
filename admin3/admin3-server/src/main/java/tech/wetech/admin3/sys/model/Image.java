package tech.wetech.admin3.sys.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 资源
 *
 * @author cjbi
 */
@Entity
public class Image extends BaseEntity {

  private String title;

  private String overview;

  private String url;
  private LocalDateTime createtime;

  private boolean isVip = true;

  private String posterPath;


  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
  @JoinTable(name = "label_image",
    joinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id"))
  private Set<Label> labels = new LinkedHashSet<>();


  public String getPosterPath() {
    return posterPath;
  }

  public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public LocalDateTime getCreatetime() {
    return createtime;
  }

  public void setCreatetime(LocalDateTime createtime) {
    this.createtime = createtime;
  }

  public boolean getIsVip() {
    return isVip;
  }

  public void setIsVip(boolean isVip) {
    this.isVip = isVip;
  }

  public boolean isVip() {
    return isVip;
  }

  public void setVip(boolean vip) {
    isVip = vip;
  }

  public Set<Label> getLabels() {
    return labels;
  }

  public void setLabels(Set<Label> labels) {
    this.labels = labels;
  }
}
