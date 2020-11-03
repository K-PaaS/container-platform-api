package org.paasta.container.platform.api.storages.storageClasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonItemMetaData;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.common.model.CommonSpec;
import org.paasta.container.platform.api.common.model.CommonStatus;

import java.util.List;

/**
 * StorageClasses List Admin Model 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.13
 */
@Data
public class StorageClassesListAdmin {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private CommonItemMetaData itemMetaData;
    private List<StorageClassesListAdminItem> items;
}

class StorageClassesListAdminItem {
    private String name;
    private String provisioner;
    private Object parameters;
    private String creationTimestamp;

    @JsonIgnore
    private CommonMetaData metadata;
    @JsonIgnore
    private CommonSpec spec;
    @JsonIgnore
    private CommonStatus status;

    public String getName() {
        return metadata.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvisioner() {
        return provisioner;
    }

    public void setProvisioner(String provisioner) {
        this.provisioner = provisioner;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
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

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }
}
