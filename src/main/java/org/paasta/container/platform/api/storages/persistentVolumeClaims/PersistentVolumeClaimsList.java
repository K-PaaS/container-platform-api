package org.paasta.container.platform.api.storages.persistentVolumeClaims;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * PersistentVolumeClaims List Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.18
 */
@Data
public class PersistentVolumeClaimsList {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private Map metadata;
    private List<PersistentVolumeClaims> items;

}
