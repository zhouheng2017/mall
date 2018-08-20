package com.mall.vo;

import com.mall.pojo.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-04
 * @Time: 11:07
 */
@Getter
@Setter
@ToString
public class ProductListVo extends Product {
    private String imageHost;


    public static ProductListVo adapt(Product product) {

        ProductListVo productListVo = new ProductListVo();
        BeanUtils.copyProperties(product, productListVo);

        return productListVo;
    }
}
