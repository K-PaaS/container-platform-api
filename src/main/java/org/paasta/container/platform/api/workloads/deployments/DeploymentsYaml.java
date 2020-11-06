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
public class DeploymentsYaml {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;

    private String sourceTypeYaml;

}
