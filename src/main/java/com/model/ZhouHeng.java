package com.model;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-21
 * @Time: 13:02
 */
public class ZhouHeng implements Person {
    private String name="Zhouheng";

    private String sex="男";

    @Override
    public void findLove() {
        System.out.println("zhouheng");
        System.out.println("要求");

    }

    @Override
    public String sex() {
        return sex;
    }

    @Override
    public String name() {
        return name;
    }
}
