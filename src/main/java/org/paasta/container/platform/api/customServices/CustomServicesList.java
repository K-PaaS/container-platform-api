package org.paasta.container.platform.api.customServices;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Custom Services List Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.10
 */
@Data
public class CustomServicesList {

    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private Map metadata;
    private List<CustomServices> items;

}
