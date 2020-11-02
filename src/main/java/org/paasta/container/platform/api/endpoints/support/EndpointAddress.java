package org.paasta.container.platform.api.endpoints.support;

import lombok.Data;

@Data
public class EndpointAddress {

    private String hostname;
    private String ip;
    private String nodeName;

}
