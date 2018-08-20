package com.mall.test;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dozer.Mapping;

import java.util.List;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-05
 * @Time: 14:36
 */
@ToString
@Getter
@Setter
public class UserSource {

    private String id;

    private String hah;

    private List<Integer> list;
}
