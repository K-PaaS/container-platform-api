package org.paasta.container.platform.api.clusters.limitRanges.support;

import com.google.gson.annotations.SerializedName;
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

    @SerializedName("default")
    private Object defaultLimit;
    private Object defaultRequest;
    private Object min;
    private Object max;
    //private Object maxLimitRequestRatio;
    private String type;
    private String resource;
}
