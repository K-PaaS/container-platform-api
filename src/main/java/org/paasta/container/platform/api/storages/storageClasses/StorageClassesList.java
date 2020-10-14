package org.paasta.container.platform.api.storages.storageClasses;

import lombok.Data;
import org.paasta.container.platform.api.storages.persistentVolumeClaims.PersistentVolumeClaims;

import java.util.List;

/**
 * StorageClasses List Model 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.13
 */
@Data
public class StorageClassesList {
    private String resultCode;
    private List<PersistentVolumeClaims> items;
}
