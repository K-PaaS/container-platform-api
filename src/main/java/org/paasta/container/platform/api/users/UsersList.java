package org.paasta.container.platform.api.users;

import lombok.Data;

import java.util.List;

/**
 *
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.22
 **/
@Data
public class UsersList {
    private String resultCode;
    private String resultMessage;

    private List<Users> items;
}
