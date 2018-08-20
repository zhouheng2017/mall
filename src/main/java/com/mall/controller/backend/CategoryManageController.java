package com.mall.controller.backend;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.common.Const;
import com.mall.common.RequestHolder;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.Category;
import com.mall.pojo.User;
import com.mall.service.ICategoryService;
import com.mall.service.IUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-02
 * @Time: 20:47
 */
@Controller
@RequestMapping("/manage/category")
@ResponseBody
public class CategoryManageController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/get_category.do", method = RequestMethod.POST)
    public ServerResponse getCategory(@RequestParam(value = "categoryId", required = false, defaultValue = "0") Integer categoryId, HttpSession session) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        List<Category> categoryList = null;
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()) {

            categoryList = categoryService.getChildCategory(categoryId);
            if (CollectionUtils.isEmpty(categoryList)) {
                return ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(), "未找到该商品");
            }
        } else {

            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
        return ServerResponse.createBySuccess(categoryList);


    }

    @RequestMapping(value = "/add_category.do", method = RequestMethod.POST)
    public ServerResponse addCategory(HttpSession session, @RequestParam(value = "parentId", required = false, defaultValue = "0") Integer parentId, @RequestParam("categoryName") String categoryName) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()) {

            return categoryService.addCategory(parentId, categoryName);
        } else {

            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }


    }

    @RequestMapping("/set_category_name.do")
    public ServerResponse setCategoryName(HttpSession session, @RequestParam("categoryId") Integer categoryId, @RequestParam("categoryName") String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()) {

            return categoryService.setCategoryName(categoryId, categoryName);
        } else {

            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }

    @RequestMapping("/pageHelper")
    public PageInfo<Category> pageHelper(Integer categoryId) {
        PageHelper.startPage(0, 1);

        List<Category> childCategory = categoryService.getChildCategory(categoryId);
        PageInfo<Category> pageInfo = new PageInfo<>(childCategory);

        return pageInfo;

    }

    @RequestMapping("/get_deep_category.do")
    public ServerResponse<List<Integer>> getDeepCategory(@RequestParam(value = "categoryId", required = false, defaultValue = "0") Integer categoryId) {

        User currentUser = RequestHolder.getCurrentUser();
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录");
        }
        if (userService.checkAdminRole(currentUser).isSuccess()) {
            return categoryService.getDeepCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权操作，请联系管理员");
        }
    }
}
