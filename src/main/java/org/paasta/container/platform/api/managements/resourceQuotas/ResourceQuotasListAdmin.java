package org.paasta.container.platform.api.managements.resourceQuotas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.common.model.CommonSpec;
import org.paasta.container.platform.api.common.model.CommonStatus;

import java.util.List;

/**
 * ResourceQuotaList Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.03
 */
@Data
public class ResourceQuotasListAdmin {

  private String resultCode;
  private String resultMessage;
  private Integer httpStatusCode;
  private String detailMessage;
  private List<ResourceQuotasListAdminItem> items;

}

class ResourceQuotasListAdminItem {
  private String name;
  private String creationTimestamp;

  @JsonIgnore
  private CommonMetaData metadata;
  @JsonIgnore
  private CommonSpec spec;
  @JsonIgnore
  private CommonStatus status;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCreationTimestamp() {
    return creationTimestamp;
  }

  public void setCreationTimestamp(String creationTimestamp) {
    this.creationTimestamp = creationTimestamp;
  }

  public CommonMetaData getMetadata() {
    return metadata;
  }

  public void setMetadata(CommonMetaData metadata) {
    this.metadata = metadata;
  }

  public CommonSpec getSpec() {
    return spec;
  }

  public void setSpec(CommonSpec spec) {
    this.spec = spec;
  }

  public CommonStatus getStatus() {
    return status;
  }

  public void setStatus(CommonStatus status) {
    this.status = status;
  }
}

