package org.paasta.container.platform.api.endpoints.support;

import lombok.Data;

import java.util.List;

@Data
public class EndpointSubset {

    private List<EndpointAddress> addresses;
    private List<EndpointAddress> notReadyAddresses;
    private List<EndpointPort> ports;
}
