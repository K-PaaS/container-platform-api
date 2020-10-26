package org.paasta.container.platform.api.clusters.limitRanges.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * LimitRanges Item Model 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.10.22
 */
@Data
public class LimitRangesItem {

    @JsonProperty("default")
    private Object LimitRange_default;
    private Object defaultRequest;
    private Object min;
    private Object max;
    private Object maxLimitRequestRatio;
    private String type;
}
