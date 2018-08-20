package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.Category;

import java.util.List;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-02
 * @Time: 20:52
 */
public interface ICategoryService {

    List<Category> getChildCategory(Integer categoryId);

    ServerResponse<String> addCategory(Integer parentId, String categoryName);

    ServerResponse setCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Integer>> getDeepCategory(Integer categoryId);

}
