package org.paasta.container.platform.api.clusters.resourceQuotas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.clusters.resourceQuotas.support.ResourceQuotasSpec;
import org.paasta.container.platform.api.clusters.resourceQuotas.support.ResourceQuotasStatus;
import org.paasta.container.platform.api.common.model.CommonMetaData;

import java.util.List;

/**
 * ResourceQuotas Admin Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.03
 */
@Data
public class ResourceQuotasAdmin {
  private String resultCode;
  private String resultMessage;
  private Integer httpStatusCode;
  private String detailMessage;

  private String name;
  private List<String> scopes;
  private String creationTimestamp;
  private ResourceQuotasStatus status;

  @JsonIgnore
  private CommonMetaData metadata;
  @JsonIgnore
  private ResourceQuotasSpec spec;

  public String getResultCode() {
    return resultCode;
  }

  public void setResultCode(String resultCode) {
    this.resultCode = resultCode;
  }

  public String getResultMessage() {
    return resultMessage;
  }

  public void setResultMessage(String resultMessage) {
    this.resultMessage = resultMessage;
  }

  public Integer getHttpStatusCode() {
    return httpStatusCode;
  }

  public void setHttpStatusCode(Integer httpStatusCode) {
    this.httpStatusCode = httpStatusCode;
  }

  public String getDetailMessage() {
    return detailMessage;
  }

  public void setDetailMessage(String detailMessage) {
    this.detailMessage = detailMessage;
  }

  public String getName() {
    return name = metadata.getName();
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getScopes() {
    return scopes = spec.getScopes();
  }

  public void setScopes(List<String> scopes) {
    this.scopes = scopes;
  }

  public String getCreationTimestamp() {
    return creationTimestamp = metadata.getCreationTimestamp();
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

  public ResourceQuotasSpec getSpec() {
    return spec;
  }

  public void setSpec(ResourceQuotasSpec spec) {
    this.spec = spec;
  }

  public ResourceQuotasStatus getStatus() {
    return status;
  }

  public void setStatus(ResourceQuotasStatus status) {
    this.status = status;
  }
}

