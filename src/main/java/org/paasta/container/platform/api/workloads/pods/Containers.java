package org.paasta.container.platform.api.workloads.pods;

import lombok.Data;

@Data
public class Containers {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private String name;
    private ContainerUsage usage;
}
