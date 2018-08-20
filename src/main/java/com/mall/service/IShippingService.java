package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.Shipping;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-06
 * @Time: 20:42
 */
public interface IShippingService {


    /**
     * 增加收货地址
     * @param userId
     * @param shipping
     * @return
     */
    ServerResponse add(Integer userId, Shipping shipping);

    /**
     * 删除用户的地址
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse delete(Integer userId, Integer shippingId);
    ServerResponse update(Integer userId, Shipping shippingId);
    ServerResponse select(Integer userId, Integer shippingId);


    ServerResponse list(Integer userId, Integer pageNum, Integer pageSize);

}
