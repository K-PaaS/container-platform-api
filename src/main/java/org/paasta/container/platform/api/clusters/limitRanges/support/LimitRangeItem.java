package org.paasta.container.platform.api.clusters.limitRanges.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LimitRangeItem {

    @JsonProperty("default")
    private Object LimitRange_default;
    private Object defaultRequest;
    private Object min;
    private Object max;
    private Object maxLimitRequestRatio;
    private String type;
}
