package org.paasta.container.platform.api.events;

import lombok.Data;

import java.util.List;

/**
 * Events List Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.17
 */
@Data
public class EventsList {

    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private List<Events> items;

}
