package org.paasta.container.platform.api.events;

import lombok.Data;
import org.paasta.container.platform.api.events.support.EventsAdminItem;

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

    //private String node;
    private List<EventsAdminItem> items;

   /* @JsonIgnore
    private EventsdeprecatedSource deprecatedSource;

    public String getNode() {
        return deprecatedSource.getNode();
    }

    public void setNode(String node) {
        this.node = node;
    }*/
}


