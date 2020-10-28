package org.paasta.container.platform.api.storages.persistentVolumeClaims;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.storages.persistentVolumeClaims.support.PersistentVolumeClaimsSpec;
import org.paasta.container.platform.api.storages.persistentVolumeClaims.support.PersistentVolumeClaimsStatus;

import java.util.List;
import java.util.Map;

/**
 * PersistentVolumeClaims Admin Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.18
 */
@Data
public class PersistentVolumeClaimsAdmin {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;

    private String name;
    private String uid;
    private String namespace;
    private Object labels;
    private Object annotations;
    private String creationTimestamp;

    private String persistentVolumeClaimStatus;
    private String storageClasses;
    private String capacity;
    private String accessMode;

    @JsonIgnore
    private CommonMetaData metadata;
    @JsonIgnore
    private PersistentVolumeClaimsSpec spec;
    @JsonIgnore
    private PersistentVolumeClaimsStatus status;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public String getName() {
        return metadata.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return metadata.getUid();
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNamespace() {
        return metadata.getNamespace();
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Object getLabels() {
        return metadata.getLabels();
    }

    public void setLabels(Object labels) {
        this.labels = labels;
    }

    public Object getAnnotations() {
        return metadata.getAnnotations();
    }

    public void setAnnotations(Object annotations) {
        this.annotations = annotations;
    }

    public String getCreationTimestamp() {
        return metadata.getCreationTimestamp();
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getPersistentVolumeClaimStatus() {
        return status.getPhase();
    }

    public void setPersistentVolumeClaimStatus(String persistentVolumeClaimStatus) {
        this.persistentVolumeClaimStatus = persistentVolumeClaimStatus;
    }

    public String getStorageClasses() {
        return spec.getStorageClassName();
    }

    public void setStorageClasses(String storageClasses) {
        this.storageClasses = storageClasses;
    }

    public Map<String, Object> getCapacity() {
        return status.getCapacity();
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
