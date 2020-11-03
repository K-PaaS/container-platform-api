package org.paasta.container.platform.api.storages.persistentVolumes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonItemMetaData;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.storages.persistentVolumes.support.ObjectReference;
import org.paasta.container.platform.api.storages.persistentVolumes.support.PersistentVolumesSpec;
import org.paasta.container.platform.api.storages.persistentVolumes.support.PersistentVolumesStatus;

import java.util.List;
import java.util.Map;

/**
 * PersistentVolumes List Admin Model 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.19
 */
@Data
public class PersistentVolumesListAdmin {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private Map metadata;
    private CommonItemMetaData itemMetaData;
    private List<PersistentVolumesListAdminItem> items;
}

class PersistentVolumesListAdminItem {
    private String name;
    private Object capacity;
    private String accessMode;
    private String persistentVolumeStatus;
    private ObjectReference claim;
    private String creationTimestamp;

    @JsonIgnore
    private CommonMetaData metadata;
    @JsonIgnore
    private PersistentVolumesSpec spec;
    @JsonIgnore
    private PersistentVolumesStatus status;

    public String getName() {
        return metadata.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getCapacity() {
        return spec.getCapacity();
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public List<String> getAccessMode() {
        return spec.getAccessModes();
    }

    public void setAccessMode(String accessMode) {
        this.accessMode = accessMode;
    }

    public String getPersistentVolumeStatus() {
        return status.getPhase();
    }

    public void setPersistentVolumeStatus(String persistentVolumeStatus) {
        this.persistentVolumeStatus = persistentVolumeStatus;
    }

    public ObjectReference getClaim() {
        return spec.getClaimRef();
    }

    public void setClaim(ObjectReference claim) {
        this.claim = claim;
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

    public PersistentVolumesSpec getSpec() {
        return spec;
    }

    public void setSpec(PersistentVolumesSpec spec) {
        this.spec = spec;
    }

    public PersistentVolumesStatus getStatus() {
        return status;
    }

    public void setStatus(PersistentVolumesStatus status) {
        this.status = status;
    }
}
