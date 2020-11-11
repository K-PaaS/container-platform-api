package org.paasta.container.platform.api.storages.persistentVolumes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.storages.persistentVolumes.support.ObjectReference;
import org.paasta.container.platform.api.storages.persistentVolumes.support.PersistentVolumesSpec;
import org.paasta.container.platform.api.storages.persistentVolumes.support.PersistentVolumesStatus;

import java.util.List;

/**
 * PersistentVolumes Admin Model 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.19
 */
@Data
public class PersistentVolumesAdmin {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;

    private String name;
    private String uid;
    private Object labels;
    private Object annotations;
    private String creationTimestamp;

    private String persistentVolumeStatus;
    private String claim;
    private String returnPolicy;
    private String storageClasses;
    private String accessMode;

    private Object source;
    private Object capacity;


    @JsonIgnore
    private CommonMetaData metadata;
    @JsonIgnore
    private PersistentVolumesSpec spec;
    @JsonIgnore
    private PersistentVolumesStatus status;

    public Object getSource() {
        return spec.getHostPath();
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public Object getCapacity() {
        return spec.getCapacity();
    }

    public void setCapacity(Object capacity) {
        this.capacity = capacity;
    }

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

    public String getPersistentVolumeStatus() {
        return status.getPhase();
    }

    public void setPersistentVolumeStatus(String persistentVolumeStatus) {
        this.persistentVolumeStatus = persistentVolumeStatus;
    }

    public ObjectReference getClaim() {
        return spec.getClaimRef();
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    public String getReturnPolicy() {
        return spec.getPersistentVolumeReclaimPolicy();
    }

    public void setReturnPolicy(String returnPolicy) {
        this.returnPolicy = returnPolicy;
    }

    public String getStorageClasses() {
        return spec.getStorageClassName();
    }

    public void setStorageClasses(String storageClasses) {
        this.storageClasses = storageClasses;
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

    public PersistentVolumesSpec getSpec() {
        return spec;
    }

    public void setSpec(PersistentVolumesSpec spec) {
        this.spec = spec;
    }

    public String getStatus() {
        return status.getPhase();
    }

    public void setStatus(PersistentVolumesStatus status) {
        this.status = status;
    }
}
