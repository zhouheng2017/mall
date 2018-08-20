package com.mall.controller.protal;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.service.IProductService;
import com.mall.vo.ProductDetailVo;
import com.mall.vo.ProductListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-04
 * @Time: 17:45
 */
@Controller
@RequestMapping("/product")
@ResponseBody
public class ProductController {

    @Autowired
    private IProductService productService;


    @RequestMapping(value = "/detail.do", method = RequestMethod.POST)
    public ServerResponse<ProductDetailVo> detail(@RequestParam("productId") Integer productId) {

        return productService.getProductDetail(productId);
    }
    @RequestMapping(value = "/detail.do2")
    public String detail2() {

        return "String";
    }

    @RequestMapping("/list.do")
    public ServerResponse<PageInfo<ProductListVo>> listAll(@RequestParam("categoryId") Integer categoryId,
                                                           @RequestParam("keyword") String keyword,
                                                           @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                           @RequestParam(value = "orderBy", required = false, defaultValue = "") String orderBy) {

        return productService.listAll(categoryId, keyword, pageNum, pageSize, orderBy);

    }
}
