package com.mall.controller.protal;

import com.google.common.base.Splitter;
import com.mall.common.Const;
import com.mall.common.RequestHolder;
import com.mall.common.ServerResponse;
import com.mall.service.ICartService;
import com.mall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-05
 * @Time: 13:36
 */


@Controller
@ResponseBody
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ICartService cartService;

    @RequestMapping("/list.do")
    public ServerResponse<CartVo> list() {
        Integer userId = RequestHolder.getCurrentUser().getId();

        return cartService.list(userId);

    }

    @RequestMapping("/add.do")
    public ServerResponse<CartVo> add(@RequestParam("productId") Integer productId, @RequestParam("count") Integer count) {

        Integer userId = RequestHolder.getCurrentUser().getId();

        return cartService.addProduct(userId, productId, count);
    }


    @RequestMapping("/update.do")
    public ServerResponse<CartVo> update(@RequestParam("productId") Integer productId, @RequestParam("count") Integer count) {
        return cartService.update(RequestHolder.getCurrentUser().getId(), productId, count);
    }

    @RequestMapping("/delete.do")
    public ServerResponse<CartVo> delete(@RequestParam("productIds") String productIds) {
        List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(productIds);
        List<Integer> productIdList = strList.stream().map(str -> Integer.parseInt(str)).collect(Collectors.toList());

        return cartService.delete(RequestHolder.getCurrentUser().getId(), productIdList);
    }

    @RequestMapping("/select.do")
    public ServerResponse<CartVo> select(@RequestParam("productId") Integer productId) {
        return cartService.selectOrUnSelect(RequestHolder.getCurrentUser().getId(), productId, Const.Cart.CHECKED);
    }

    @RequestMapping("/un_select.do")
    public ServerResponse<CartVo> unSelect(@RequestParam("productId") Integer productId) {
        return cartService.selectOrUnSelect(RequestHolder.getCurrentUser().getId(), productId, Const.Cart.UN_CHECKED);
    }

    @RequestMapping("/selectAll.do")
    public ServerResponse<CartVo> selectAll() {
        return cartService.selectOrUnSelect(RequestHolder.getCurrentUser().getId(), null, Const.Cart.CHECKED);
    }

    @RequestMapping("/un_selectAll.do")
    public ServerResponse<CartVo> unSelectAll() {
        return cartService.selectOrUnSelect(RequestHolder.getCurrentUser().getId(), null, Const.Cart.UN_CHECKED);
    }

    @RequestMapping("/get_cartService_product_count")
    public ServerResponse<Integer> getCount() {
        return cartService.getCartProduct(RequestHolder.getCurrentUser().getId());
    }
}
