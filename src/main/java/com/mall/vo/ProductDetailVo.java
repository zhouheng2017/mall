package com.mall.vo;

import com.mall.pojo.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-04
 * @Time: 15:16
 */
@Getter
@Setter
@ToString
public class ProductDetailVo extends Product {
    private String imageHost;
    private Integer parentCategoryId;

    public static ProductDetailVo adapt(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyProperties(product, productDetailVo);
        return productDetailVo;
    }

}
