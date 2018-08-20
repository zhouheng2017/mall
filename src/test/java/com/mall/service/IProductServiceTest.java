package com.mall.service;

import basic.ApplicationTest;
import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.util.JsonMapper;
import com.mall.vo.ProductListVo;
import javafx.application.Application;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-05
 * @Time: 11:33
 */
public class IProductServiceTest extends ApplicationTest{

    @Autowired
    private IProductService productService;

    @Test
    public void listAll() {
        ServerResponse<PageInfo<ProductListVo>> pageInfoServerResponse = productService.listAll(100001, "m", 1, 10, "price_desc");
        System.out.println(JsonMapper.obj2String(pageInfoServerResponse));

    }
}