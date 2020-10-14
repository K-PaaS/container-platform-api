package org.paasta.container.platform.api.workloads.deployments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.workloads.deployments.support.DeploymentsSpec;
import org.paasta.container.platform.api.workloads.deployments.support.DeploymentsStatus;
import org.paasta.container.platform.api.workloads.deployments.support.DeploymentsStrategy;

/**
 * @author hrjin
 * @version 1.0
 * @since 2020.10.11
 **/
public class DeploymentsAdmin {
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
    private DeploymentsStrategy strategy;
    private int minReadySeconds;
    private int revisionHistoryLimit;
    private Object selector;
    private int updated;
    private int total;
    private int available;

    @JsonIgnore
    private CommonMetaData metadata;

    @JsonIgnore
    private DeploymentsSpec spec;

    @JsonIgnore
    private DeploymentsStatus status;

    public CommonMetaData getMetadata() {
        return metadata;
    }

    public DeploymentsSpec getSpec() {
        return spec;
    }

    public DeploymentsStatus getStatus() {
        return status;
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
        return name = metadata.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid = metadata.getUid();
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNamespace() {
        return namespace = metadata.getNamespace();
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Object getLabels() {
        return labels = spec.getTemplate().getMetadata().getLabels();
    }

    public void setLabels(Object labels) {
        this.labels = labels;
    }

    public Object getAnnotations() {
        return annotations = metadata.getAnnotations();
    }

    public void setAnnotations(Object annotations) {
        this.annotations = annotations;
    }

    public String getCreationTimestamp() {
        return creationTimestamp = metadata.getCreationTimestamp();
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public DeploymentsStrategy getStrategy() {
        return strategy = spec.getStrategy();
    }

    public void setStrategy(DeploymentsStrategy strategy) {
        this.strategy = strategy;
    }

    public int getMinReadySeconds() {
        return minReadySeconds;
    }

    public void setMinReadySeconds(int minReadySeconds) {
        this.minReadySeconds = minReadySeconds;
    }

    public int getRevisionHistoryLimit() {
        return revisionHistoryLimit = spec.getRevisionHistoryLimit();
    }

    public void setRevisionHistoryLimit(int revisionHistoryLimit) {
        this.revisionHistoryLimit = revisionHistoryLimit;
    }

    public Object getSelector() {
        return selector = spec.getSelector();
    }

    public void setSelector(Object selector) {
        this.selector = selector;
    }

    public int getUpdated() {
        return updated = status.getUpdatedReplicas();
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getTotal() {
        return total = status.getReplicas();
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAvailable() {
        return available = status.getAvailableReplicas();
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
