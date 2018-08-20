package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.pojo.Product;
import com.mall.vo.ProductDetailVo;
import com.mall.vo.ProductListVo;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-04
 * @Time: 10:52
 */
public interface IProductService {
    /**
     * 显示所有的产品
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo<ProductListVo>> list(Integer pageNum, Integer pageSize);

    /**
     * 搜索产品模糊查找
     * @param productId
     * @param productName
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> searchByIdAndName(Integer productId, String productName, Integer pageNum, Integer pageSize);

    ServerResponse<String> saveOrUpdateProduct(Product product);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<Product> setProductStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    /**
     * 获取分页的详细信息
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    ServerResponse<PageInfo<ProductListVo>> listAll(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);

}
