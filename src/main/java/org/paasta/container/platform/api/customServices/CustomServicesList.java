package org.paasta.container.platform.api.customServices;

import lombok.Data;

import java.util.List;

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
    private List<CustomServices> items;

}
