package org.paasta.container.platform.api.managements.resourceQuotas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.common.model.CommonSpec;
import org.paasta.container.platform.api.common.model.CommonStatus;
import org.paasta.container.platform.api.managements.resourceQuotas.support.ResourceQuotasSpec;
import org.paasta.container.platform.api.managements.resourceQuotas.support.ResourceQuotasStatus;

import java.lang.reflect.Array;

/**
 * ResourceQuota Model 클래스
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
  private String apiVersion;

  private String name;
  private Array scopes;
  private String creationTimestamp;

  private String kind;

  @JsonIgnore
  private CommonMetaData metadata;
  @JsonIgnore
  private CommonSpec spec;
  @JsonIgnore
  private CommonStatus status;

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

  public String getApiVersion() {
    return apiVersion;
  }

  public void setApiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Array getScopes() {
    return scopes;
  }

  public void setScopes(Array scopes) {
    this.scopes = scopes;
  }

  public String getCreationTimestamp() {
    return creationTimestamp;
  }

  public void setCreationTimestamp(String creationTimestamp) {
    this.creationTimestamp = creationTimestamp;
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
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

