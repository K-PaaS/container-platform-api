package org.paasta.container.platform.api.storages.persistentVolumes;

import lombok.Data;
import org.paasta.container.platform.api.storages.persistentVolumeClaims.PersistentVolumeClaims;

import java.util.List;
import java.util.Map;

/**
 * PersistentVolumes List Model 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.19
 */
@Data
public class PersistentVolumesList {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private Map metadata;
    private List<PersistentVolumes> items;
}
