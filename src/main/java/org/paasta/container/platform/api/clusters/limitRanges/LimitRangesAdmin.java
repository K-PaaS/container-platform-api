package org.paasta.container.platform.api.clusters.limitRanges;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.paasta.container.platform.api.clusters.limitRanges.support.LimitRangeItem;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.common.model.CommonSpec;
import java.util.List;

/**
 * LimitRanges Admin Model 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.10.22
 */
public class LimitRangesAdmin {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private String nextActionUrl;

    private String name;
    private String creationTimestamp;
    private List<LimitRangeItem> limits;
    private String sourceTypeYaml;

    @JsonIgnore
    private CommonMetaData metadata;
    @JsonIgnore
    private CommonSpec spec;

    public LimitRangesAdmin(String sourceTypeYaml) {
        this.sourceTypeYaml = sourceTypeYaml;
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

    public String getNextActionUrl() {
        return nextActionUrl;
    }

    public void setNextActionUrl(String nextActionUrl) {
        this.nextActionUrl = nextActionUrl;
    }

    // LimitRanges
    public String getName() {
        return metadata.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationTimestamp() {
        return metadata.getCreationTimestamp();
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public List<LimitRangeItem> getLimits() { return spec.getLimits(); }

    public void setLimits(List<LimitRangeItem> limits) { this.limits = limits; }

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
}
