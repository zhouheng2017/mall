package com.mall.controller.protal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mall.common.Const;
import com.mall.common.RequestHolder;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by geely
 */

@Controller
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private IOrderService orderService;


    @RequestMapping("/create.do")
    @ResponseBody
    public ServerResponse create(@RequestParam("shippingId") Integer shippingId) {
        Integer userId = RequestHolder.getCurrentUser().getId();
        return orderService.createOrder(userId, shippingId);
    }
    @RequestMapping("/cancel.do")
    @ResponseBody
    public ServerResponse cancel(@RequestParam("orderNo") Long orderNo) {
        Integer userId = RequestHolder.getCurrentUser().getId();
        return orderService.cancel(userId, orderNo);
    }

    @RequestMapping("/detail.do")
    @ResponseBody
    public ServerResponse detail(@RequestParam("orderNo") Long orderNo) {
        Integer userId = RequestHolder.getCurrentUser().getId();
        return orderService.getOrderDetail(userId, orderNo);
    }

    @RequestMapping("/list.do")
    @ResponseBody
    public ServerResponse list(@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        Integer userId = RequestHolder.getCurrentUser().getId();


        return orderService.getOrderList(userId, pageNum, pageSize);
    }



    @RequestMapping("/get_order_cart_product.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct() {
        return orderService.getOrderCartProduct(RequestHolder.getCurrentUser().getId());
    }


    @RequestMapping("/pay.do")
    @ResponseBody
    public ServerResponse pay(@RequestParam("orderNo") Long orderNo) {

        Integer userId = RequestHolder.getCurrentUser().getId();
        String path = RequestHolder.getCurrentRequest().getSession().getServletContext().getRealPath("upload");
        return orderService.pay(orderNo, userId, path);

    }


    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {

        Map<String, String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {

                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        log.info("支付宝回调,sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        //非常重要,验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.

        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());

            if (!alipayRSACheckedV2) {
                return ServerResponse.createByErrorMessage("非法请求,验证不通过,再恶意请求我就报警找网警了");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证回调异常", e);
        }

        //todo 验证各种数据



        ServerResponse serverResponse = orderService.aliCallback(params);
        if(serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    @RequestMapping("/query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderStatus(@RequestParam("orderNo") Long orderNo) {

        Integer userId = RequestHolder.getCurrentUser().getId();

        ServerResponse response = orderService.queryOrderPayStatus(userId, orderNo);
        if (response.isSuccess()) {
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }


}
