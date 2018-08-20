package com.mall.dao;

import com.mall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhouheng
 */
public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    List<OrderItem> getByOrderNoUserId(@Param("orderNo") Long orderNo, @Param("userId") Integer userId);

    void batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);

    List<OrderItem> getbyOrderNo(Long orderNo);

    OrderItem selectbyOrderNo(Long orderNo);
}