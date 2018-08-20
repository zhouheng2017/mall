package com.mall.test;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-05
 * @Time: 14:18
 */
@ToString
@Getter
@Setter
public class USerDO {
    private String username;
    private String password;
    private String mail;

    private String hah;

    private String id;

    private List<Integer> list;

}
