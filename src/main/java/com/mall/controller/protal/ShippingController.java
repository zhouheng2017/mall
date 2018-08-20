package com.mall.controller.protal;

import com.mall.common.RequestHolder;
import com.mall.common.ServerResponse;
import com.mall.pojo.Shipping;
import com.mall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-06
 * @Time: 20:40
 */
@Controller
@ResponseBody
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private IShippingService shippingService;

    @RequestMapping("/add.do")
    public ServerResponse<String> add(Shipping shipping) {

        return shippingService.add(RequestHolder.getCurrentUser().getId(), shipping);
    }

    @RequestMapping("/delete.do")
    public ServerResponse delete(@RequestParam("shippingId") Integer shippingId) {

        return shippingService.delete(RequestHolder.getCurrentUser().getId(), shippingId);
    }

    @RequestMapping("/update.do")
    public ServerResponse update(Shipping shippingId) {

        return shippingService.update(RequestHolder.getCurrentUser().getId(), shippingId);
    }

    @RequestMapping("/select.do")
    public ServerResponse select(@RequestParam("shippingId") Integer shippingId) {

        return shippingService.select(RequestHolder.getCurrentUser().getId(), shippingId);
    }

    @RequestMapping("/list.do")
    public ServerResponse list(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        return shippingService.list(RequestHolder.getCurrentUser().getId(), pageNum, pageSize);
    }
}





















