package com.mall.dao;

import com.mall.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    Order selectByOrderNo(long orderNo);

    Order selectByOrderNoAndUserId(Long orderNo, Integer userId);

    List<Order> selectByUserId(Integer userId);

    Order selectByUserIdAndShippingId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    List<Order> selectAll();

    List<Order> selectbyOrderStatusAndCreateTime(@Param("stauts") int stauts, @Param("createTime") String createTime);

    void closeOrderByOrderId(Integer id);
}