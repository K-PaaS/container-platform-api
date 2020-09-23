package org.paasta.container.platform.api.adminToken;

import lombok.Data;

/**
 * Admin Token Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.08.31
 */
@Data
public class AdminToken {

    private String tokenName;
    private String tokenValue;
    private String resultCode;
    private int statusCode;
    private String resultMessage;
}
