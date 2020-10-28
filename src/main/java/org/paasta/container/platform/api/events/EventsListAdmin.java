package org.paasta.container.platform.api.events;

import lombok.Data;

import java.util.List;

/**
 * Events List Admin Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.17
 */
@Data
public class EventsListAdmin {

    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private List<Events> items;

}

class EventsListAdminItem {
    private String message;
    private String source;
    private String subObject;
    private String count;
    private String firstSeen;
    private String lastSeen;

}
