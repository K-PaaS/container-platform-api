package org.paasta.container.platform.api.adminToken;

import org.paasta.container.platform.api.common.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin Token Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.08.31
 */
@RestController
public class AdminTokenController {

    private final RestTemplateService adminTokenService;

    /**
     * Instantiates a AdminToken controller
     *
     * @param adminTokenService the adminToken Service
     */
    @Autowired
    public AdminTokenController(RestTemplateService adminTokenService) {
        this.adminTokenService = adminTokenService;
    }

    /**
     * AdminToken 상세 조회(Get AdminToken detail)
     *
     * @return the adminToken detail
     */
    @GetMapping(value = "/adminToken")
    public AdminToken getAdminToken(){
        AdminToken adminTokenValue = adminTokenService.getAdminToken();
        return adminTokenValue;
    }
}
