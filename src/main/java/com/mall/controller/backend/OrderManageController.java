package com.mall.controller.backend;

import com.mall.common.RequestHolder;
import com.mall.common.ServerResponse;
import com.mall.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-18
 * @Time: 16:50
 */
@RequestMapping("/manage/order")
@ResponseBody
@Controller
public class OrderManageController {

    @Autowired
    private IOrderService orderService;


    @RequestMapping("/list.do")
    public ServerResponse list(@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        return orderService.manageList(pageNum, pageSize);

    }

    @RequestMapping("/detail.do")
    public ServerResponse list(@RequestParam("orderNo")Long orderNo){

        return orderService.manageDetail(orderNo);

    }

    @RequestMapping("/search.do")
    public ServerResponse search(@RequestParam("orderNo") Long orderNo,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                 @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        return orderService.manageSearch(orderNo, pageNum, pageSize);

    }

    @RequestMapping("/send_goods.do")
    public ServerResponse sendGoods(@RequestParam("orderNo") Long orderNo) {

        return orderService.manageSendGoods(orderNo);

    }



}
