package com.mall.test;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-05
 * @Time: 14:37
 */
@Getter
@Setter
@ToString
public class TargetUSer {
    private String pkID;
    private String username;
    private String password;
    private List<String> list;
}
