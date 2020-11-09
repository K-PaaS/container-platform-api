package org.paasta.container.platform.api.workloads.pods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonItemMetaData;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.common.model.CommonSpec;
import org.paasta.container.platform.api.workloads.pods.support.PodsStatus;

import java.util.List;
import java.util.Map;

/**
 * Pods List Admin Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.09
 */
@Data
public class PodsListAdmin {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private Map metadata;
    private CommonItemMetaData itemMetaData;
    private List<PodsListAdminList> items;
}

class PodsListAdminList {
    private String name;
    private String namespace;
    private Object labels;
    private String nodes;
    private String podStatus;
    private Integer restarts;
    private String creationTimestamp;

    @JsonIgnore
    private CommonMetaData metadata;
    @JsonIgnore
    private CommonSpec spec;
    @JsonIgnore
    private PodsStatus status;


    public String getName() {
        return metadata.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return metadata.getNamespace();
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNodes() {
        return spec.getNodeName();
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public String getPodStatus() {
        return status.getPhase();
    }

    public void setPodStatus(String podStatus) {
        this.podStatus = podStatus;
    }

    public Integer getRestarts() {
        return status.getContainerStatuses().get(0).getRestartCount();
    }

    public void setRestarts(Integer restarts) {
        this.restarts = restarts;
    }

    public String getCreationTimestamp() {
        return metadata.getCreationTimestamp();
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public CommonMetaData getMetadata() {
        return metadata;
    }

    public void setMetadata(CommonMetaData metadata) {
        this.metadata = metadata;
    }

    public CommonSpec getSpec() {
        return spec;
    }

    public void setSpec(CommonSpec spec) {
        this.spec = spec;
    }

    public PodsStatus getStatus() {
        return status;
    }

    public void setStatus(PodsStatus status) {
        this.status = status;
    }

    public Object getLabels() {
        return metadata.getLabels();
    }

    public void setLabels(Object labels) {
        this.labels = labels;
    }
}
