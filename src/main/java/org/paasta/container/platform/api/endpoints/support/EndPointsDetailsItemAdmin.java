package org.paasta.container.platform.api.endpoints.support;

import lombok.Data;

import java.util.List;

@Data
public class EndPointsDetailsItemAdmin {

    private String host;
    private String nodes;
    private String ready;
    private List<EndpointPort> ports;
}
