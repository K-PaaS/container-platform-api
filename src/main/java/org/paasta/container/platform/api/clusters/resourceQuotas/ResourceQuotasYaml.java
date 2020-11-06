package org.paasta.container.platform.api.clusters.resourceQuotas;

import lombok.Data;
import org.paasta.container.platform.api.clusters.resourceQuotas.support.ResourceQuotasSpec;
import org.paasta.container.platform.api.clusters.resourceQuotas.support.ResourceQuotasStatus;
import org.paasta.container.platform.api.common.model.CommonMetaData;

/**
 * ResourceQuotas Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.03
 */
@Data
public class ResourceQuotasYaml {
  private String resultCode;
  private String resultMessage;
  private Integer httpStatusCode;
  private String detailMessage;
  private String sourceTypeYaml;

}

