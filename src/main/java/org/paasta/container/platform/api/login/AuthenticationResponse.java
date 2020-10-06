package org.paasta.container.platform.api.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.paasta.container.platform.api.common.model.ResultStatus;

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
public class AuthenticationResponse extends ResultStatus {


    private String userId;
    private String token;

    public AuthenticationResponse(String resultCode, String resultMessage, int httpStatusCode, String detailMessage,
                                  String nextActionUrl, String userId, String token) {
        super(resultCode, resultMessage, httpStatusCode, detailMessage, nextActionUrl);
        this.userId = userId;
        this.token = token;
    }

}
