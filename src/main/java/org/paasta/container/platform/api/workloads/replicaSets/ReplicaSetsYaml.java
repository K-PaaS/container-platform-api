package org.paasta.container.platform.api.workloads.replicaSets;

import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.common.model.CommonSpec;
import org.paasta.container.platform.api.common.model.CommonStatus;

import java.util.Map;

/**
 * ReplicaSets Model 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.09.10
 */
@Data
public class ReplicaSetsYaml {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private String sourceTypeYaml;

}
