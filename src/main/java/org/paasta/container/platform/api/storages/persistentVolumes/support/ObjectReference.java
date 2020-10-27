package org.paasta.container.platform.api.storages.persistentVolumes.support;

import lombok.Data;
/**
 * Object Reference Model 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.19
 */

@Data
public class ObjectReference {
    private String name;
    private String namespace;
   /* private String apiVersion;
    private String fieldPath;
    private String kind;
    private String resourceVersion;
    private String uid; */
}
