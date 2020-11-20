package org.paasta.container.platform.api.workloads.pods;

import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonMetaData;

import java.util.List;

@Data
public class PodsUsage {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private CommonMetaData metadata;
    private List<Containers> containers;
}
