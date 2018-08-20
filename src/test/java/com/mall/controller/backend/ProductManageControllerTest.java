package com.mall.controller.backend;

import basic.ApplicationTest;
import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.pojo.Product;
import com.mall.util.JsonMapper;
import com.mall.vo.ProductDetailVo;
import com.mall.vo.ProductListVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-04
 * @Time: 16:02
 */
public class ProductManageControllerTest extends ApplicationTest{

    @Autowired
    private ProductManageController productManageController;

    @Test
    public void list() {
        ServerResponse<PageInfo<ProductListVo>> list = productManageController.list(1, 10);

        System.out.println(JsonMapper.obj2String(list));

    }

    @Test
    public void search() {
        ServerResponse<PageInfo> m = productManageController.search(1, 10, null, "m");
        System.out.println(JsonMapper.obj2String(m));

    }

    @Test
    public void save() {

    }

    @Test
    public void detail() {
        ServerResponse<ProductDetailVo> detail = productManageController.detail(67);
        System.out.println(JsonMapper.obj2String(detail));

    }

    @Test
    public void setStatus() {
        ServerResponse<Product> productServerResponse = productManageController.setStatus(67, 1);
        String string = JsonMapper.obj2String(productServerResponse);
        System.out.println(string);

    }
}