package org.paasta.container.platform.api.clusters.nodes;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodes List Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.01
 */
@Data
public class NodesList {
    private String resultCode;

    private List<Nodes> items = new ArrayList<>();
}
