package tech.wetech.admin3.sys.service.dto;

import jakarta.persistence.*;
import tech.wetech.admin3.sys.model.Label;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author cjbi
 */
public record ImageDTO(Long id,String title,String overview, String url,LocalDateTime createtime,boolean isVip,String posterPath,Set<Label> labels){

}
