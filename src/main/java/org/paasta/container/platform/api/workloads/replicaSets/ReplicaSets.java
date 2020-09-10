package org.paasta.container.platform.api.workloads.replicaSets;

import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.common.model.CommonSpec;
import org.paasta.container.platform.api.common.model.CommonStatus;

/**
 * ReplicaSets Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.10
 */
@Data
public class ReplicaSets {

    private String resultCode;
    private CommonMetaData metadata;
    private CommonSpec spec;
    private CommonStatus status;
    private String sourceTypeYaml;

}
