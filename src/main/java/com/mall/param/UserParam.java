package com.mall.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-07-30
 * @Time: 16:06
 */
@Getter
@Setter
@ToString

public class UserParam {
    private Integer id;

    private String username;

    private String password;

    @Email
    private String email;

    private String phone;

    private String question;

    private String answer;

    private Integer role;

}
