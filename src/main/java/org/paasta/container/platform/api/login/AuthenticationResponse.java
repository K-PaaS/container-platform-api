package org.paasta.container.platform.api.login;

import com.google.gson.JsonArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.paasta.container.platform.api.common.model.ResultStatus;

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
public class AuthenticationResponse extends ResultStatus {


    private String userId;
    private String token;
    private List namespace;

    public AuthenticationResponse(String resultCode, String resultMessage, int httpStatusCode, String detailMessage,
                                  String nextActionUrl, String userId, String token, List namespace) {
        super(resultCode, resultMessage, httpStatusCode, detailMessage, nextActionUrl);
        this.userId = userId;
        this.token = token;
        this.namespace = namespace ;
    }

}
