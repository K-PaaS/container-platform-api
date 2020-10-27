package org.paasta.container.platform.api.clusters.resourceQuotas;

import lombok.Data;

import java.util.List;

/**
 * ResourceQuotasDefaultList Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.10.26
 **/
@Data
public class ResourceQuotasDefaultList {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;

    private List<ResourceQuotasDefault> items;
}
