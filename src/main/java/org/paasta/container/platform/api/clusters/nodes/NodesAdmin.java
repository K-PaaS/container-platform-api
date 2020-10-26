package org.paasta.container.platform.api.clusters.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.clusters.nodes.support.NodesAddress;
import org.paasta.container.platform.api.clusters.nodes.support.NodesStatus;
import org.paasta.container.platform.api.clusters.nodes.support.NodesSystemInfo;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.common.model.CommonSpec;

import java.util.List;

/**
 * Nodes Admin Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.01
 */
@Data
public class NodesAdmin {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;

    private String name;
    private String uid;
    private Object labels;
    private Object annotations;
    private String creationTimestamp;

    private String podsCIDR;
    private List<NodesAddress> addresses;
    private NodesSystemInfo info;

    @JsonIgnore
    private CommonMetaData metadata;
    @JsonIgnore
    private CommonSpec spec;
    @JsonIgnore
    private NodesStatus status;

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

    public Object getLabels() {
        return labels = metadata.getLabels();
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

    public String getPodsCIDR() {
        return podsCIDR = spec.getPodCIDR();
    }

    public void setPodsCIDR(String podsCIDR) {
        this.podsCIDR = podsCIDR;
    }

    public List<NodesAddress> getAddresses() {
        return addresses = status.getAddresses();
    }

    public void setAddresses(List<NodesAddress> addresses) {
        this.addresses = addresses;
    }

    public NodesSystemInfo getInfo() {
        return info = status.getNodeInfo();
    }

    public void setInfo(NodesSystemInfo info) {
        this.info = info;
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

    public NodesStatus getStatus() {
        return status;
    }

    public void setStatus(NodesStatus status) {
        this.status = status;
    }
}
