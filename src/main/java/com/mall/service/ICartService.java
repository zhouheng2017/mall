package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.vo.CartVo;

import java.util.List;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-05
 * @Time: 13:44
 */
public interface ICartService {
    ServerResponse<CartVo> list(Integer id);

    ServerResponse<CartVo> addProduct(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> delete(Integer userId, List<Integer> productIdList);

    ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, Integer checked);

    ServerResponse<Integer> getCartProduct(Integer userId);

}
