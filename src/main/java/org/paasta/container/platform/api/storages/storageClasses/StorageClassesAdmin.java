package org.paasta.container.platform.api.storages.storageClasses;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import org.paasta.container.platform.api.common.CommonUtils;
import org.paasta.container.platform.api.common.model.CommonMetaData;

/**
 * StorageClasses Admin Model 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.13
 */
@Data
public class StorageClassesAdmin {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;

    private String name;
    private String uid;
    private Object labels;
    private Object annotations;
    private String creationTimestamp;

    private String provisioner;
    private Object parameters;

    @JsonIgnore
    private CommonMetaData metadata;

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
}
