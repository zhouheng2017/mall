package com.mall.pojo;

import lombok.*;
import org.hibernate.validator.constraints.Email;

import java.io.Serializable;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    public static final Long serialVersionUID = 1L;
    private Integer id;

    private String username;

    private String password;

    @Email
    private String email;

    private String phone;

    private String question;

    private String answer;

    private Integer role;

    private Date createTime;

    private Date updateTime;

}