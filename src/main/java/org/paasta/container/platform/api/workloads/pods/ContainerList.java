package org.paasta.container.platform.api.workloads.pods;

import lombok.Data;

import java.util.List;

@Data
public class ContainerList {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private List<Containers> containers;
}
