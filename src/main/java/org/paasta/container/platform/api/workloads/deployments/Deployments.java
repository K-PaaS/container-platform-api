package org.paasta.container.platform.api.workloads.deployments;

import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.workloads.deployments.support.DeploymentsSpec;
import org.paasta.container.platform.api.workloads.deployments.support.DeploymentsStatus;

import java.util.Map;

/**
 * Deployments Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.08
 */
@Data
public class Deployments {
    private String resultCode;
    private String nextActionUrl;

    private CommonMetaData metadata;
    private DeploymentsSpec spec;
    private DeploymentsStatus status;

    private Map<String, Object> source;
    private String sourceTypeYaml;

}
