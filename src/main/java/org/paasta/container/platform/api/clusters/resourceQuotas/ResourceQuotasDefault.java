package org.paasta.container.platform.api.clusters.resourceQuotas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ResourceQuotasDefault Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.10.26
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceQuotasDefault {
    @JsonIgnore
    private String id;

    private String name;

    @JsonIgnore
    private String requestCpu;

    @JsonIgnore
    private String requestMemory;

    @JsonIgnore
    private String limitCpu;

    @JsonIgnore
    private String limitMemory;

    private String status;

    public ResourceQuotasDefault(String name, String status) {
        this.name = name;
        this.status = status;
    }
}
