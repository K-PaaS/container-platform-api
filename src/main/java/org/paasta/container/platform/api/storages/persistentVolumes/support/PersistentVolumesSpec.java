package org.paasta.container.platform.api.storages.persistentVolumes.support;

import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonLabelSelector;
import org.paasta.container.platform.api.common.model.CommonResourceRequirement;
import org.paasta.container.platform.api.common.model.CommonTypedLocalObjectReference;

import java.util.List;
import java.util.Map;

/**
 * PersistentVolumes Spec Model 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.19
 */
@Data
public class PersistentVolumesSpec {
    private List<String> accessModes;
    private String volumeName;
    private String storageClassName;
    private String volumeMode;
    private CommonTypedLocalObjectReference dataSource;
    private CommonResourceRequirement resources;
    private CommonLabelSelector selector;
    private ObjectReference claimRef;
    private Object capacity;
    private String persistentVolumeReclaimPolicy;
    private HostPathVolumeSource hostPath;
}
