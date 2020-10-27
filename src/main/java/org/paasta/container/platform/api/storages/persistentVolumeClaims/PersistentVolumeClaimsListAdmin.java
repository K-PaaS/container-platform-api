package org.paasta.container.platform.api.storages.persistentVolumeClaims;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.storages.persistentVolumeClaims.support.PersistentVolumeClaimsSpec;
import org.paasta.container.platform.api.storages.persistentVolumeClaims.support.PersistentVolumeClaimsStatus;

import java.util.List;
import java.util.Map;

/**
 * PersistentVolumeClaims List Admin Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.18
 */
@Data
public class PersistentVolumeClaimsListAdmin {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private Map metadata;
    private List<PersistentVolumeClaimsListAdminItem> items;

}

class PersistentVolumeClaimsListAdminItem {

    private String name;
    private String namespace;
    private String persistentVolumeClaimStatus;
    private String volume;
    private String capacity;
    private String creationTimestamp;

    @JsonIgnore
    private CommonMetaData metadata;
    @JsonIgnore
    private PersistentVolumeClaimsSpec spec;
    @JsonIgnore
    private PersistentVolumeClaimsStatus status;

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

    public String getPersistentVolumeClaimStatus() {
        return status.getPhase();
    }

    public void setPersistentVolumeClaimStatus(String persistentVolumeClaimStatus) {
        this.persistentVolumeClaimStatus = persistentVolumeClaimStatus;
    }

    public String getVolume() {
        return spec.getVolumeName();
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Map<String, Object> getCapacity() {
        return status.getCapacity();
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
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

    public PersistentVolumeClaimsSpec getSpec() {
        return spec;
    }

    public void setSpec(PersistentVolumeClaimsSpec spec) {
        this.spec = spec;
    }

    public PersistentVolumeClaimsStatus getStatus() {
        return status;
    }

    public void setStatus(PersistentVolumeClaimsStatus status) {
        this.status = status;
    }
}
