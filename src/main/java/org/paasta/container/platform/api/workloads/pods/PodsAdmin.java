package org.paasta.container.platform.api.workloads.pods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.common.model.*;
import org.paasta.container.platform.api.workloads.pods.support.Volume;

import java.util.List;

/**
 * Pods Admin Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.09
 */
@Data
public class PodsAdmin {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;

    private String name;
    private String uid;
    private String namespace;
    private Object labels;
    private Object annotations;
    private String creationTimestamp;

    private String nodes;
    private String podStatus;
    private String ip;
    private String qosClass;
    private int restarts;
    private String controllers;
    private String volumes;
    private String containersName;
    private String containersImage;


    @JsonIgnore
    private CommonMetaData metadata;
    @JsonIgnore
    private CommonSpec spec;
    @JsonIgnore
    private CommonStatus status;

    public int getRestarts() {

        int restarts = (int) Math.floor(status.getContainerStatuses().get(0).getRestartCount());

        return restarts;
    }

    public void setRestarts(int restarts) {
        this.restarts = restarts;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public String getName() {
        return metadata.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return metadata.getUid();
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNamespace() {
        return metadata.getNamespace();
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Object getLabels() {
        return metadata.getLabels();
    }

    public void setLabels(Object labels) {
        this.labels = labels;
    }

    public Object getAnnotations() {
        return metadata.getAnnotations();
    }

    public void setAnnotations(Object annotations) {
        this.annotations = annotations;
    }

    public String getCreationTimestamp() {
        return metadata.getCreationTimestamp();
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getNodes() {
        return spec.getNodeName();
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public String getIp() {
        return status.getPodIP();
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getQosClass() {
        return status.getQosClass();
    }

    public void setQosClass(String qosClass) {
        this.qosClass = qosClass;
    }

    public List<CommonOwnerReferences> getControllers() {
        return metadata.getOwnerReferences();
    }

    public void setControllers(String controllers) {
        this.controllers = controllers;
    }

    public List<Volume> getVolumes() {
        return spec.getVolumes();
    }

    public void setVolumes(String volumes) {
        this.volumes = volumes;
    }

    public String getContainersName() {
        return spec.getContainers().get(0).getName();
    }

    public void setContainersName(String containersName) {
        this.containersName = containersName;
    }

    public String getContainersImage() {
        return spec.getContainers().get(0).getImage();
    }

    public void setContainersImage(String containersImage) {
        this.containersImage = containersImage;
    }

    public CommonMetaData getMetadata() {
        return metadata;
    }

    public void setMetadata(CommonMetaData metadata) {
        this.metadata = metadata;
    }

    public CommonSpec getSpec() {
        return spec;
    }

    public void setSpec(CommonSpec spec) {
        this.spec = spec;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }

    public String getPodStatus() { return status.getPhase(); }

    public void setPodStatus(String podStatus) { this.podStatus = podStatus; }

}
