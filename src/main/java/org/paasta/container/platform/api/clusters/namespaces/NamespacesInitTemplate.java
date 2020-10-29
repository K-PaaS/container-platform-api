package org.paasta.container.platform.api.clusters.namespaces;

import lombok.Data;

import java.util.List;

/**
 * Namespaces Init Template Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.10.29
 **/
@Data
public class NamespacesInitTemplate {
    private String name;
    private String nsAdminUserId;
    private List<String> resourceQuotasList;
    private List<String> limitRangesList;
}
