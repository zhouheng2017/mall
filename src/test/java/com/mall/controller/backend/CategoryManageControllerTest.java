package com.mall.controller.backend;

import basic.ApplicationTest;
import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.pojo.Category;
import com.mall.service.ICategoryService;
import com.mall.util.JsonMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-03
 * @Time: 21:16
 */
public class CategoryManageControllerTest extends ApplicationTest {
    @Autowired
    private CategoryManageController categoryManageController;

    @Autowired
    private ICategoryService categoryService;

    @Test
    public void pageHelper() {
        PageInfo<Category> pageInfo = categoryManageController.pageHelper(0);
        System.out.println(pageInfo);
    }

    @Test
    public void getCategory() {
    }

    @Test
    public void addCategory() {
    }

    @Test
    public void setCategoryName() {
    }

    @Test
    public void getDeepCategory() {
        ServerResponse<List<Integer>> deepCategory = categoryService.getDeepCategory(100001);
        System.out.println(JsonMapper.obj2String(deepCategory));
    }
}