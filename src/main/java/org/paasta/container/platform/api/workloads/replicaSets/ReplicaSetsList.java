package org.paasta.container.platform.api.workloads.replicaSets;

import lombok.Data;

import java.util.List;

/**
 * ReplicaSets Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.10
 */
@Data
public class ReplicaSetsList {

    private String resultCode;
    private List<ReplicaSets> items;

}
