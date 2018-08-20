package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Category;
import com.mall.pojo.Product;
import com.mall.service.ICategoryService;
import com.mall.service.IProductService;
import com.mall.util.DateTimeUtil;
import com.mall.util.PropertiesUtil;
import com.mall.vo.ProductDetailVo;
import com.mall.vo.ProductListVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-04
 * @Time: 10:52
 */
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService categoryService;

    /**
     * 显示所有的产品
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo<ProductListVo>> list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<Product> productList = productMapper.selectAllProduct();
        if (CollectionUtils.isEmpty(productList)) {
            return ServerResponse.createByErrorMessage("没有商品");
        }

        PageInfo<ProductListVo> pageResult = getProductListVoPageInfo(productList);


        return ServerResponse.createBySuccess(pageResult);
    }

    /**
     * 获取分页的详细信息
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public ServerResponse<PageInfo<ProductListVo>> listAll(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        if (categoryId == null || StringUtils.isBlank(keyword)) {

            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        List<Integer> categoryIdList = Lists.newArrayList();

        if (categoryId != null) {

            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (StringUtils.isBlank(keyword) && category == null) {

                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo<ProductListVo> pageResult = new PageInfo<>(productListVoList);
                return ServerResponse.createBySuccess(pageResult);
            }
            categoryIdList = categoryService.getDeepCategory(categoryId).getData();

        }

        PageHelper.startPage(pageNum, pageSize);
        String key = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(keyword)) {

            key = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

//        排序

        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] split = orderBy.split("_");

                String join = Joiner.on(" ").join(split);
                System.out.println(join+"-------------------------------");

                PageHelper.orderBy(join);

            }
        }


        List<Product> productList = productMapper.selectAllProductByCategoryIdAndKeyword(categoryIdList.size() == 0 ? null : categoryIdList, StringUtils.isBlank(key) ? null : key);
        PageInfo<ProductListVo> result = getProductListVoPageInfo(productList);

        return ServerResponse.createBySuccess(result);
    }

    /**
     * 搜索产品模糊查找
     *
     * @param productId
     * @param productName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse searchByIdAndName(Integer productId, String productName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        String productNameNew = new StringBuilder().append("%").append(productName).append("%").toString();
        List<Product> productList = productMapper.selectByNameAndId(productNameNew, productId);
        if (CollectionUtils.isEmpty(productList)) {
            return ServerResponse.createByErrorMessage("搜索的商品不存在");
        }
        PageInfo<ProductListVo> pageResult = getProductListVoPageInfo(productList);

        return ServerResponse.createBySuccess(pageResult);

    }

    @Override
    public ServerResponse<String> saveOrUpdateProduct(Product product) {
        if (product != null) {

            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] split = product.getSubImages().split(",");
                if (split.length > 0) {

                    product.setMainImage(split[0]);
                }

            }

            if (product.getId() == null) {
                int count = productMapper.insertSelective(product);
                if (count > 0) {

                    return ServerResponse.createBySuccessMessage("新增商品成功");
                }
                return ServerResponse.createBySuccess("新增产品失败");

            } else {

                int count = productMapper.updateByPrimaryKeySelective(product);
                if (count > 0) {
                    return ServerResponse.createBySuccessMessage("更新商品成功");
                }
                return ServerResponse.createBySuccess("更新产品失败");

            }
        }
        return ServerResponse.createByErrorMessage("更新或新增商品失败");
    }

    @Override
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已经下架或者删除");
        }
       /* if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("产品已经下架或者删除");
        }*/

        ProductDetailVo productDetailVo = ProductDetailVo.adapt(product);
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));


        Category category = categoryMapper.selectByPrimaryKey(productDetailVo.getCategoryId());
        if (category != null) {
            productDetailVo.setParentCategoryId(category.getId());


        } else {
            productDetailVo.setParentCategoryId(0);

        }




        return ServerResponse.createBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse<Product> setProductStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {

            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        Preconditions.checkNotNull(product, "改变产品的不存在");
        product.setStatus(status);
        int i = productMapper.updateByPrimaryKeySelective(product);
        if (i > 0) {
            return ServerResponse.createBySuccess("更新产品状态", product);

        }

        return ServerResponse.createByErrorMessage("更新状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已经下架或者删除");
        }
        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("产品已经下架或者删除");
        }

        ProductDetailVo productDetailVo = ProductDetailVo.adapt(product);
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));


        Category category = categoryMapper.selectByPrimaryKey(productDetailVo.getCategoryId());
        if (category != null) {
            productDetailVo.setParentCategoryId(category.getId());


        } else {
            productDetailVo.setParentCategoryId(0);

        }




        return ServerResponse.createBySuccess(productDetailVo);
    }



    /**
     * 获取对应的分页信息
     * @param productList
     * @return
     */
    private PageInfo<ProductListVo> getProductListVoPageInfo(List<Product> productList) {
        List<ProductListVo> productListVoList = Lists.newArrayList();

        ProductListVo productListVo = null;
        for (Product product : productList) {
            productListVo = ProductListVo.adapt(product);
            productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
            productListVoList.add(productListVo);

        }
        return new PageInfo<>(productListVoList);
    }

}
