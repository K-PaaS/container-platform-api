package org.paasta.container.platform.api.workloads.pods;

import lombok.Data;

@Data
public class ContainerUsage {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private String cpu;
    private String memory;
}
