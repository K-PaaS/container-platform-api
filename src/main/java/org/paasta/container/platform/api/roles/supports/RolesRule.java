package org.paasta.container.platform.api.roles.supports;

import lombok.Data;

import java.util.List;

/**
 * Roles Rule 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.10.13
 */
@Data
public class RolesRule {

    private List apiGroups;
    private List nonResourceURLs;
    private List resourceNames;
    private List resources;
    private List verbs;

}
