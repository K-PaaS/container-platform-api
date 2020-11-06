package org.paasta.container.platform.api.events.support;

import lombok.Data;

@Data
public class EventSource {
    private String component="";
    private String host="";
}
