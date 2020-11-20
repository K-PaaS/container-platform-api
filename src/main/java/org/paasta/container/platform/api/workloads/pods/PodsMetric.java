package org.paasta.container.platform.api.workloads.pods;

import lombok.Data;

import java.util.List;

@Data
public class PodsMetric {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private List<PodsUsage> items;
    private String kind;

}
