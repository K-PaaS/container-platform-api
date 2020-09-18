package org.paasta.container.platform.api.endpoints;

import lombok.Data;

import java.util.List;

/**
 * Endpoints List Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.17
 */
@Data
public class EndpointsList {

    private String resultCode;
    private List<Endpoints> items;

}
