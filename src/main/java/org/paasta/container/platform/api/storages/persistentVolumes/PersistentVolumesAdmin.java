package org.paasta.container.platform.api.storages.persistentVolumes;

import static org.paasta.container.platform.api.common.Constants.NULL_REPLACE_TEXT;
import static org.paasta.container.platform.api.common.Constants.PERSISTENT_VOLUME_TYPE;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.paasta.container.platform.api.common.CommonUtils;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.storages.persistentVolumes.support.ObjectReference;
import org.paasta.container.platform.api.storages.persistentVolumes.support.PersistentVolumesSpec;
import org.paasta.container.platform.api.storages.persistentVolumes.support.PersistentVolumesStatus;

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
        if (spec.getHostPath() == null) {
            return NULL_REPLACE_TEXT;
        } else {
            String path = spec.getHostPath().getPath();
            String type = spec.getHostPath().getType();

            if ((StringUtils.isNotEmpty(path)) && (StringUtils.isNotEmpty(type)) || type.equals("")) {
                spec.getHostPath().setType(PERSISTENT_VOLUME_TYPE);
            }
            return spec.getHostPath();
        }
    }

    public Object getCapacity() {
        return CommonUtils.procReplaceNullValue(spec.getCapacity());
    }

    public String getName() {
        return metadata.getName();
    }

    public String getUid() {
        return metadata.getUid();
    }

    public Object getLabels() {
        return CommonUtils.procReplaceNullValue(metadata.getLabels());
    }

    public Object getAnnotations() {
        return CommonUtils.procReplaceNullValue(metadata.getAnnotations());
    }

    public String getCreationTimestamp() {
        return metadata.getCreationTimestamp();
    }

    public String getPersistentVolumeStatus() {
        return status.getPhase();
    }

    public ObjectReference getClaim() {
        return CommonUtils.procReplaceNullValue(spec.getClaimRef());
    }

    public String getReturnPolicy() {
        return spec.getPersistentVolumeReclaimPolicy();
    }

    public String getStorageClasses() {
        return spec.getStorageClassName();
    }

    public List<String> getAccessMode() {
        return spec.getAccessModes();
    }
}
