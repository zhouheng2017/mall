package com.mall.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePayRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.dao.*;
import com.mall.pojo.*;
import com.mall.service.IOrderService;
import com.mall.util.BigDecimalUtil;
import com.mall.util.DateTimeUtil;
import com.mall.util.FTPUtil;
import com.mall.util.PropertiesUtil;
import com.mall.vo.OrderItemVo;
import com.mall.vo.OrderProductVo;
import com.mall.vo.OrderVo;
import com.mall.vo.ShippingVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-16
 * @Time: 22:30
 */
@Service
@Slf4j
public class IOrderServiceImpl implements IOrderService {


    private static  AlipayTradeService tradeService;
    static {

        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;


    /**
     * 支付
     *
     * @param orderNo
     * @param userId
     * @param path
     * @return
     */
/*    @Override
    public ServerResponse pay(Long orderNo, Integer userId, String path) {
        Map<String, String> resultMap = Maps.newHashMap();
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (!ObjectUtils.allNotNull(order)) {
            return ServerResponse.createByErrorMessage("用户订单不存在");
        }
        resultMap.put("orderNo", String.valueOf(orderNo));


        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店消费”
        String subject = new StringBuilder().append("happmmall扫码支付，订单号为：").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (必填) 付款条码，用户支付宝钱包手机app点击“付款”产生的付款条码
        String authCode = "2088102176088144"; // 条码示例，286648048691290423
        // (可选，根据需要决定是否使用) 订单可打折金额，可以配合商家平台配置折扣活动，如果订单部分商品参与打折，可以将部分商品总价填写至此字段，默认全部商品可打折
        // 如果该值未传入,但传入了【订单总金额】,【不可打折金额】 则该值默认为【订单总金额】- 【不可打折金额】
        //        String discountableAmount = "1.00"; //

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0.0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品3件共20.00元"
        String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共").append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        String providerId = "2088100200300400500";
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId(providerId);

        // 支付超时，线下扫码交易定义为5分钟
        String timeoutExpress = "5m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo, userId);
        for (OrderItem orderItem : orderItemList) {
            GoodsDetail goodsDetail = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(), BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(), new Double(100).doubleValue()).longValue(),
                    orderItem.getQuantity());

            goodsDetailList.add(goodsDetail);
        }
  *//*      // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx面包", 1000, 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
        goodsDetailList.add(goods2);*//*

        String appAuthToken = "应用授权令牌";//根据真实值填写

        // 创建条码支付请求builder，设置请求参数
        AlipayTradePayRequestBuilder builder = new AlipayTradePayRequestBuilder()
                //            .setAppAuthToken(appAuthToken)
                .setOutTradeNo(outTradeNo).setSubject(subject).setAuthCode(authCode)
                .setTotalAmount(totalAmount).setStoreId(storeId)
                .setUndiscountableAmount(undiscountableAmount).setBody(body).setOperatorId(operatorId)
                .setExtendParams(extendParams).setSellerId(sellerId)
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))
                .setGoodsDetailList(goodsDetailList).setTimeoutExpress(timeoutExpress);

        // FIXME: 2018/8/17 x需要修改订单的回调地址
        // 调用tradePay方法获取当面付应答
        AlipayF2FPayResult result = tradeService.tradePay(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝支付成功: )");
                AlipayTradePayResponse response = result.getResponse();
                dumpResponse(response);
                File folder = new File(path);
                if (!folder.exists()) {
                    folder.setWritable(true);
                    folder.mkdirs();

                }
                String qrPath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getCardBalance(), 156, qrPath);

                File targetFile = new File(path,qrFileName);
                try {
                    FTPUtil.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    log.error("上传二维码异常",e);
                }
                log.info("qrPath:" + qrPath);
                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFile.getName();
                resultMap.put("qrUrl",qrUrl);
                return ServerResponse.createBySuccess(resultMap);

            case FAILED:
                log.error("支付宝支付失败!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，订单状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return null;
    }*/


    public ServerResponse pay(Long orderNo,Integer userId,String path){
        Map<String ,String> resultMap = Maps.newHashMap();
        Order order = orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
        if(order == null){
            return ServerResponse.createByErrorMessage("用户没有该订单");
        }
        resultMap.put("orderNo",String.valueOf(order.getOrderNo()));



        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();


        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("happymmall扫码支付,订单号:").append(outTradeNo).toString();


        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();


        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";



        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共").append(totalAmount).append("元").toString();


        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");




        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo,userId);
        for(OrderItem orderItem : orderItemList){
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
                    BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(),new Double(100).doubleValue()).longValue(),
                    orderItem.getQuantity());
            goodsDetailList.add(goods);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);


        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File folder = new File(path);
                if(!folder.exists()){
                    folder.setWritable(true);
                    folder.mkdirs();
                }

                // 需要修改为运行机器上的路径
                //细节细节细节
                String qrPath = String.format(path+"/qr-%s.png",response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png",response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                File targetFile = new File(path,qrFileName);
                try {
                    FTPUtil.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    log.error("上传二维码异常",e);
                }
                log.info("qrPath:" + qrPath);
                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFile.getName();
                resultMap.put("qrUrl",qrUrl);
                return ServerResponse.createBySuccess(resultMap);
            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }

    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (org.apache.commons.lang.StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    /**
     * 回调
     *
     * @param params
     * @return
     */
    @Override
    public ServerResponse aliCallback(Map<String, String> params) {
        long orderNo = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMessage("非快乐慕商城的订单,回调忽略");
        }
        if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
            return ServerResponse.createBySuccess("支付宝重复调用");
        }
        if (Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
            order.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
            order.setStatus(Const.OrderStatusEnum.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);

        }
        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);
        payInfoMapper.insert(payInfo);

        return ServerResponse.createBySuccess();
    }

    /**
     * 查询订单支付状态
     *
     * @param userId
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);

        if (order == null) {
            return ServerResponse.createBySuccess("用户灭有订单");
        }
        if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    /**
     * 创建订单
     *
     * @param userId
     * @param shippingId
     * @return
     */
    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        if (!ObjectUtils.allNotNull(userId, shippingId)) {
            log.info("userId:{}, shippingId:{}", userId, shippingId);
            return ServerResponse.createByErrorMessage("参数错误不可以为空");
        }


        List<Cart> cartList = cartMapper.selectByCheckedUserId(userId);

        ServerResponse cartOrderItem = this.getCartOrderItem(userId, cartList);
        if (!cartOrderItem.isSuccess()) {
            return cartOrderItem;
        }

        List<OrderItem> orderItemList = (List<OrderItem>) cartOrderItem.getData();
        BigDecimal payment = getOrderTotalPrice(orderItemList);

        Order order = assembleOrder(userId, shippingId, payment);
        if (order == null) {
            return ServerResponse.createByErrorMessage("生成订单错误");
        }


        if (CollectionUtils.isEmpty(orderItemList)) {
            return ServerResponse.createByErrorMessage("购物车为空");
        }

        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());

        }

        orderItemMapper.batchInsert(orderItemList);
        reduceProductStock(orderItemList);

        cleanCart(cartList);
        OrderVo orderVo = assembleOrderVo(order, orderItemList);
        return ServerResponse.createBySuccess(orderVo);
    }

    private OrderVo assembleOrderVo(Order order, List<OrderItem> orderItemList) {
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());

        OrderVo orderVo = OrderVo.builder()
                .orderNo(order.getOrderNo())
                .payment(order.getPayment())
                .paymentType(order.getPaymentType())
                .paymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue())
                .postage(order.getPostage())
                .status(order.getStatus())
                .statusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue())
                .shippingId(order.getShippingId())
                .paymentTime(DateTimeUtil.dateToStr(order.getPaymentTime() ))
                .sendTime(DateTimeUtil.dateToStr(order.getSendTime() ))
                .endTime(DateTimeUtil.dateToStr(order.getCreateTime()))
                .closeTime(DateTimeUtil.dateToStr(order.getCloseTime()))
                .imageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"))

                .build();

        if (shipping != null) {

            orderVo.setReceiverName(shipping.getReceiverName());
            ShippingVo shippingVo = new ShippingVo();
            BeanUtils.copyProperties(shipping, shippingVo);

            orderVo.setShippingVo(shippingVo);

        }
        List<OrderItemVo> orderItemVoList = getOrderItemVo(orderItemList);

        orderVo.setOrderItemVoList(orderItemVoList);

        return orderVo;
    }

    private List<OrderItemVo> getOrderItemVo(List<OrderItem> orderItemList) {
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();

        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(orderItem, orderItemVo);

//            OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        return orderItemVoList;
    }


    private void cleanCart(List<Cart> cartList) {
        for (Cart cart : cartList) {
            cartMapper.deleteByPrimaryKey(cart.getId());

        }

    }

    private void reduceProductStock(List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);

        }

    }

    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment) {
        long orderNo = generateOrderNo();
        Order order = Order.builder()
                .orderNo(orderNo)
                .status(Const.OrderStatusEnum.NO_PAY.getCode())
                .postage(0)
                .paymentType(Const.PaymentTypeEnum.ONLINE_PAY.getCode())
                .payment(payment)
                .userId(userId)
                .shippingId(shippingId)
                .build();

        int result = orderMapper.insert(order);
        if (result > 0) {
            return order;
        }
        return null;
    }

    private long generateOrderNo() {

        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal bigDecimal = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            bigDecimal = BigDecimalUtil.add(bigDecimal.doubleValue(), orderItem.getTotalPrice().doubleValue());

        }
        return bigDecimal;
    }

    private ServerResponse getCartOrderItem(Integer userId, List<Cart> cartList) {
        List<OrderItem> orderItemList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(cartList)) {
            return ServerResponse.createByErrorMessage("购物车为空");
        }
        for (Cart cart : cartList) {
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if (Const.ProductStatusEnum.ON_SALE.getCode() != product.getStatus()) {
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "不是在手卖的状态");
            }

            if (cart.getQuantity() > product.getStock()) {
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "库存不足");
            }

            OrderItem orderItem = OrderItem.builder()
                    .userId(userId)
                    .productId(product.getId())
                    .productName(product.getName())
                    .productImage(product.getMainImage())
                    .currentUnitPrice(product.getPrice())
                    .quantity(cart.getQuantity())
                    .totalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cart.getQuantity().doubleValue()))
                    .build();
            orderItemList.add(orderItem);

        }
        return ServerResponse.createBySuccess(orderItemList);

    }

    /**
     * 取消订单
     *
     * @param userId
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse<String> cancel(Integer userId, Long orderNo) {
        if (!ObjectUtils.allNotNull(userId, orderNo)) {
            return ServerResponse.createByErrorMessage("参数错误，不可以为空值");
        }
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (!ObjectUtils.allNotNull(order)) {
            return ServerResponse.createByErrorMessage("订单不存在");
        }
        if (!order.getStatus().equals(Const.OrderStatusEnum.NO_PAY.getCode())) {
            return ServerResponse.createByErrorMessage("订单已付款无法取消");

        }
        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(Const.OrderStatusEnum.CANCELED.getCode());

        int i = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if (i > 0) {

            return ServerResponse.createBySuccess();
        }

        return ServerResponse.createByError();
    }

    /**
     * 获取订单购物车物品
     *
     * @param userId
     * @return
     */
    @Override
    public ServerResponse getOrderCartProduct(Integer userId) {
        OrderProductVo orderProductVo = new OrderProductVo();
        List<Cart> cartList = cartMapper.selectByCheckedUserId(userId);

        ServerResponse response = getCartOrderItem(userId, cartList);
        if (!response.isSuccess()) {
            return response;
        }

        List<OrderItem> orderItemList = (List<OrderItem>) response.getData();
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        BigDecimal payment = new BigDecimal("0");

        for (OrderItem orderItem : orderItemList) {

            payment = BigDecimalUtil.mul(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
            orderItemVoList.add(assembleOrderItemVo(orderItem));

        }
        orderProductVo.setProductTotalPrice(payment);
        orderProductVo.setOrderItemVoList(orderItemVoList);
        orderProductVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));


        return ServerResponse.createBySuccess(orderProductVo);
    }


    private OrderItemVo assembleOrderItemVo(OrderItem orderItem){
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());

        orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
        return orderItemVo;
    }
    /**
     * 获取订单详情
     *
     * @param userId
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo) {
        if (!ObjectUtils.allNotNull(userId, orderNo)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo, userId);
        if (CollectionUtils.isEmpty(orderItemList)) {
            return ServerResponse.createByErrorMessage("没有知道对应的订单");
        }
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);


        OrderVo orderVo = assembleOrderVo(order, orderItemList);


        return ServerResponse.createBySuccess(orderVo);
    }

    /**
     * 获取订单列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<Order> orderList = orderMapper.selectByUserId(userId);
        if (CollectionUtils.isEmpty(orderList)) {
            return ServerResponse.createByErrorMessage("没有订单");
        }
        List<OrderVo> orderVoList = assembleOrderVoList(userId, orderList);
        PageInfo pageInfo = new PageInfo(orderVoList);


        return ServerResponse.createBySuccess(pageInfo);
    }

    private List<OrderVo> assembleOrderVoList(Integer userId, List<Order> orderList) {
        List<OrderVo> orderVoList = Lists.newArrayList();

        for (Order order : orderList) {

            List<OrderItem> orderItemList = Lists.newArrayList();
            if (userId == null) {
                orderItemList = orderItemMapper.getbyOrderNo(order.getOrderNo());

            } else {

                orderItemList = orderItemMapper.getByOrderNoUserId(order.getOrderNo(), userId);
            }
            OrderVo orderVo = assembleOrderVo(order, orderItemList);
            orderVoList.add(orderVo);

        }
        return orderVoList;
    }

    /**
     * 管理列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo> manageList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<Order> orderList = orderMapper.selectAll();

        if (CollectionUtils.isEmpty(orderList)) {
            return ServerResponse.createByErrorMessage("没有订单");
        }
        List<OrderVo> orderVoList = assembleOrderVoList(null, orderList);

        PageInfo pageInfo = new PageInfo(orderVoList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    /**
     * 管理详情
     *
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse<OrderVo> manageDetail(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMessage("kong");
        }
        List<OrderItem> orderItem = orderItemMapper.getbyOrderNo(orderNo);


        OrderVo orderVo = assembleOrderVo(order, orderItem);

        return ServerResponse.createBySuccess(orderVo);
    }

    /**
     * 管理搜索
     *
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMessage("kong");
        }
        List<OrderItem> orderItem = orderItemMapper.getbyOrderNo(orderNo);


        OrderVo orderVo = assembleOrderVo(order, orderItem);


        PageInfo pageInfo = new PageInfo(Arrays.asList(orderVo));

        pageInfo.setList(Arrays.asList(orderVo));

        return ServerResponse.createBySuccess(pageInfo);
    }

    /**
     * 管理发送的goods
     *
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse<String> manageSendGoods(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMessage("不攒在");
        }
        if (order.getStatus() == Const.OrderStatusEnum.PAID.getCode()) {

            order.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
            order.setSendTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
            return ServerResponse.createBySuccessMessage("发货成功");
        }

        return ServerResponse.createByErrorMessage("失败");
    }

    /**
     * 关闭订单hour个小时以为未付款的订单进行关闭
     *
     * @param hour
     */
    @Override
    public void closerOrder(int hour) {

//        关闭半个小时以内未关闭的订单
        Date dateTime = DateUtils.addHours(new Date(), -hour);

        List<Order> orderList = orderMapper.selectbyOrderStatusAndCreateTime(Const.OrderStatusEnum.NO_PAY.getCode(), DateTimeUtil.dateToStr(dateTime));

        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderItemMapper.getbyOrderNo(order.getOrderNo());

            for (OrderItem orderItem : orderItemList) {

                Integer stock = productMapper.selectStockByPrimaryKey(orderItem.getProductId());

                if (stock == null) {
                    continue;

                }

                Product product = new Product();
                product.setStock(stock + orderItem.getQuantity());

                product.setId(product.getId());
                productMapper.updateByPrimaryKeySelective(product);

            }
            orderMapper.closeOrderByOrderId(order.getId());
            log.info("关闭订单OrderNo：{}", order.getOrderNo());



        }
    }
}
