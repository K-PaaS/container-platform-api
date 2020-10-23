package org.paasta.container.platform.api.customServices;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.common.model.CommonSpec;
import org.paasta.container.platform.api.common.model.CommonStatus;

/**
 * CustomServices Admin Model 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.09.10
 */
public class CustomServicesAdmin {

    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;

    //Details
    private String name;
    private String uid;
    private String namespace;
    private Object labels;
    private Object annotations;
    private String creationTimestamp;

    //Resource Info
    private String type;
    private String clusterIP;
    private String sessionAffinity;
    private Object selector;

    private String sourceTypeYaml;


    @JsonIgnore
    private CommonMetaData metadata;
    @JsonIgnore
    private CommonSpec spec;
    @JsonIgnore
    private CommonStatus status;


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

    public String getType() {
        return spec.getType();
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClusterIP() {
        return spec.getClusterIP();
    }

    public void setClusterIP(String clusterIP) {
        this.clusterIP = clusterIP;
    }

    public String getSessionAffinity() {
        return spec.getSessionAffinity();
    }

    public void setSessionAffinity(String sessionAffinity) {
        this.sessionAffinity = sessionAffinity;
    }

    public Object getSelector() {
        return spec.getSelector();
    }

    public void setSelector(Object selector) {
        this.selector = selector;
    }

    public String getSourceTypeYaml() {
        return sourceTypeYaml;
    }

    public void setSourceTypeYaml(String sourceTypeYaml) {
        this.sourceTypeYaml = sourceTypeYaml;
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
}
