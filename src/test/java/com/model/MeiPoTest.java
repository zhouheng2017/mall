package com.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-21
 * @Time: 13:19
 */
public class MeiPoTest {

    @Test
    public void invoke() {
        Person person = new ZhouHeng();
        Person person1 = (Person) new MeiPo().getInstance(person);
        System.out.println(person1.getClass());

        person1.findLove();
    }
}