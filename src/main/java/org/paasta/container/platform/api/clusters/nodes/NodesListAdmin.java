package org.paasta.container.platform.api.clusters.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonCondition;
import org.paasta.container.platform.api.common.model.CommonItemMetaData;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.common.model.CommonStatus;

import java.util.List;

/**
 * Nodes List Admin Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.01
 */
@Data
public class NodesListAdmin {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private CommonItemMetaData itemMetaData;
    private List<NodesListAdminItem> items;
}

class NodesListAdminItem {
    private String name;
    private Object labels;
    private String ready;
    private String creationTimestamp;

    @JsonIgnore
    private CommonMetaData metadata;

    @JsonIgnore
    private CommonStatus status;

    public String getName() {
        return name = metadata.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getLabels() {
        return labels = metadata.getLabels();
    }

    public void setLabels(Object labels) {
        this.labels = labels;
    }

    public String getReady() {
        List<CommonCondition> conditions = status.getConditions();
        for (CommonCondition c:conditions) {
            if(c.getType().equals("Ready")) {
                ready = c.getStatus();
            }
        }

        return ready;
    }

    public void setReady(String ready) {
        this.ready = ready;
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

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }
}
