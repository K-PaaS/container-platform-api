package org.paasta.container.platform.api.workloads.deployments;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Deployments List Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.08
 */
@Data
public class DeploymentsList {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private Map metadata;
    private List<Deployments> items;
}
