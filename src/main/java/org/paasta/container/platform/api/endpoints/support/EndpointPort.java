package org.paasta.container.platform.api.endpoints.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class EndpointPort {

    @JsonIgnore
    private String appProtocol;
    private String name;
    private Integer port;
    private String protocol;
}
