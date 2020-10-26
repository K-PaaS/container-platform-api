package org.paasta.container.platform.api.workloads.deployments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonItemMetaData;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.common.model.CommonSpec;
import org.paasta.container.platform.api.common.model.CommonStatus;
import org.paasta.container.platform.api.workloads.deployments.support.DeploymentsSpec;
import org.paasta.container.platform.api.workloads.deployments.support.DeploymentsStatus;

import java.util.List;
import java.util.Map;

/**
 * Deployments List Admin Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.08
 */
@Data
public class DeploymentsListAdmin {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private CommonItemMetaData itemMetaData;
    private List<DeploymentsListAdminItem> items;
}

class DeploymentsListAdminItem {
    private String name;
    private String namespace;
    private int runningPods;
    private int totalPods;
    private String images;
    private String creationTimestamp;

    @JsonIgnore
    private CommonMetaData metadata;
    @JsonIgnore
    private DeploymentsSpec spec;
    @JsonIgnore
    private DeploymentsStatus status;

    public String getName() {
        return name = metadata.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace = metadata.getNamespace();
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public int getRunningPods() {
        return runningPods = status.getAvailableReplicas();
    }

    public void setRunningPods(int runningPods) {
        this.runningPods = runningPods;
    }

    public int getTotalPods() {
        return totalPods = status.getReplicas();
    }

    public void setTotalPods(int totalPods) {
        this.totalPods = totalPods;
    }

    public String getImages() {
        return images = spec.getTemplate().getSpec().getContainers().get(0).getImage();
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getCreationTimestamp() {
        return creationTimestamp = metadata.getCreationTimestamp();
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

    public DeploymentsSpec getSpec() {
        return spec;
    }

    public void setSpec(DeploymentsSpec spec) {
        this.spec = spec;
    }

    public DeploymentsStatus getStatus() {
        return status;
    }

    public void setStatus(DeploymentsStatus status) {
        this.status = status;
    }
}
