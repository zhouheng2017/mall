package com.mall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mall.common.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.pojo.Category;
import com.mall.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-02
 * @Time: 20:52
 */
@Service
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getChildCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            log.info("未找到当亲分类的子分类");
            return Lists.newArrayList();


        }

        return categoryList;
    }

    @Override
    public ServerResponse<String> addCategory(Integer parentId, String categoryName) {
        if (parentId == null|| StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加商品参数错误");
        }
        Category category = Category.builder().parentId(parentId).name(categoryName).status(true).createTime(new Date())
                .updateTime(new Date()).build();
        int count = categoryMapper.insertSelective(category);
        if (count > 0) {
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    @Override
    public ServerResponse setCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }

        Category before = categoryMapper.selectByPrimaryKey(categoryId);
        if (before == null) {
            return ServerResponse.createByErrorMessage("更新商品类名失败");
        }

        Category category = Category.builder().id(before.getId()).createTime(before.getCreateTime()).updateTime(new Date()).name(categoryName).status(before.getStatus()).parentId(before.getParentId()).build();

        int result = categoryMapper.updateByPrimaryKeySelective(category);
        if (result > 0) {
            return ServerResponse.createBySuccess("更新商品类名成功");
        }

        return ServerResponse.createByErrorMessage("更新商品类名失败");
    }

    @Override
    public ServerResponse<List<Integer>> getDeepCategory(Integer categoryId) {
        if (categoryId == null) {
            return ServerResponse.createByErrorMessage("参数错误，品类id不可以为空");
        }
        Set<Category> categorySet = Sets.newHashSet();
        Set<Category> children = findChildren(categorySet, categoryId);
        List<Integer> list = Lists.newArrayList();

        for (Category child : children) {

            list.add(child.getId());
        }




        return ServerResponse.createBySuccess(list);
    }


    private Set<Category> findChildren(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        List<Category> childrenCategoryList = categoryMapper.selectByParentId(categoryId);
        if (CollectionUtils.isNotEmpty(childrenCategoryList)) {
            for (Category category1 : childrenCategoryList) {
                findChildren(categorySet, category1.getId());
            }
        }
        return categorySet;
    }
}
