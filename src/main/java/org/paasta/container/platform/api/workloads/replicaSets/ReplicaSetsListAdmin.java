package org.paasta.container.platform.api.workloads.replicaSets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.common.model.CommonSpec;
import org.paasta.container.platform.api.common.model.CommonStatus;

import java.util.List;
import java.util.Map;

/**
 * ReplicaSets List Model 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.09.10
 */
@Data
public class ReplicaSetsListAdmin {

    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;

    private Map metadata;
    private List<ReplicaSetsListAdminItem> items;

}

class ReplicaSetsListAdminItem {
    private String name;
    private String namesapce;
    private String pods;
    private String image;
    private String creationTimestamp;

    @JsonIgnore
    private CommonMetaData metadata;
    @JsonIgnore
    private CommonSpec spec;
    @JsonIgnore
    private CommonStatus status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespaces() {
        return namesapce;
    }

    public void setNamespaces(String namesapce) {
        this.namesapce = namesapce;
    }

    public String getPods() {
        return pods;
    }

    public void setPods(String pods) {
        this.pods = pods;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
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

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }
}
