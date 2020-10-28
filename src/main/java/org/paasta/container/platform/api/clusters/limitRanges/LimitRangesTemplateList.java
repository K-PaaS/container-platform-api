package org.paasta.container.platform.api.clusters.limitRanges;

import lombok.Data;
import org.paasta.container.platform.api.clusters.limitRanges.support.LimitRangesItem;

import java.util.List;

/**
 * LimitRangesTemplateList Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.10.28
 **/
@Data
public class LimitRangesTemplateList {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;

    private List<LimitRangesTemplateItem> items;
}

@Data
class LimitRangesTemplateItem {
    private String name;
    private List<LimitRangesItem> limits;

}