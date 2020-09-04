package org.paasta.container.platform.api.clusters.nodes;

import lombok.Data;
import org.paasta.container.platform.api.clusters.nodes.support.NodesStatus;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.common.model.CommonSpec;

/**
 * Nodes Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.01
 */
@Data
public class Nodes {
    private String resultCode;

    private CommonMetaData metadata;
    private CommonSpec spec;
    private NodesStatus status;
}
