package org.paasta.container.platform.api.common.model;

import lombok.Data;

@Data
public class ContainerStatus {
    private String name;
    private Object state;
    private Object lastState;
    private String ready;
    private Double restartCount;
    private String image;
    private String imageID;
    private String started;
}
