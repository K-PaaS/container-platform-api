package org.paasta.container.platform.api.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.login.support.loginMetaDataItem;

import java.util.List;

/**
 * Authentication Response Model 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.09.28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AuthenticationResponse extends ResultStatus {


    private String userId;
    private String token;
    private List<loginMetaDataItem> loginMetaData;
    private String clusterName;

    public AuthenticationResponse(String resultCode, String resultMessage, int httpStatusCode, String detailMessage) {
        super(resultCode, resultMessage, httpStatusCode, detailMessage);
    }

    public AuthenticationResponse(String resultCode, String resultMessage, int httpStatusCode, String detailMessage,
                                  String nextActionUrl, String userId, String token, List loginMetaData, String clusterName) {
        super(resultCode, resultMessage, httpStatusCode, detailMessage, nextActionUrl);
        this.userId = userId;
        this.token = token;
        this.loginMetaData = loginMetaData ;
        this.clusterName = clusterName;
    }

}
