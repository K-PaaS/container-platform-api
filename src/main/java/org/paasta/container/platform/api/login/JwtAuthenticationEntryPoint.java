package org.paasta.container.platform.api.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.model.CommonStatusCode;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Jwt Authentication EntryPoint 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.09.28
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String detailMessage = "Token authentication failed";

        final Exception exception = (Exception) request.getAttribute("exception");

        if (exception != null) {

            if (exception.getMessage().equals("TOKEN_EXPIRED")) {
                detailMessage = "Access Token has Expired";
            } else if (exception.getMessage().equals("INVALID_CREDENTIALS")) {
                detailMessage = "Invalid Credentials";
            }

        }

        ResultStatus resultStatus = new ResultStatus
                (Constants.RESULT_STATUS_FAIL, "TOKEN_FAILED", CommonStatusCode.INTERNAL_SERVER_ERROR.getCode(), detailMessage);

        OutputStream out = response.getOutputStream();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> result = mapper.convertValue(resultStatus, Map.class);
        mapper.writeValue(out, result);
        out.flush();


    }

}