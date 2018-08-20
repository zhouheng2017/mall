package com.mall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.Product;
import com.mall.pojo.User;
import com.mall.service.IFileService;
import com.mall.service.IProductService;
import com.mall.service.IUserService;
import com.mall.util.PropertiesUtil;
import com.mall.vo.ProductDetailVo;
import com.mall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-04
 * @Time: 10:30
 */
@Controller
@RequestMapping("/manage/product")
@ResponseBody
public class ProductManageController {
    @Autowired
    private IProductService productService;

    @Autowired
    private IFileService fileService;

    @Autowired
    private IUserService userService;

    @RequestMapping("/list.do")
    public ServerResponse<PageInfo<ProductListVo>> list(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        return productService.list(pageNum, pageSize);

    }

    @RequestMapping("/search.do")
    public ServerResponse<PageInfo> search(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                           @RequestParam(value = "productId", required = false) Integer productId,
                                           @RequestParam(value = "productName") String productName) {

        return productService.searchByIdAndName(productId, productName, pageNum, pageSize);
    }

    @RequestMapping("/save.do")
    public ServerResponse<String> save(Product product) {

        return productService.saveOrUpdateProduct(product);
    }

    @RequestMapping("/detail.do")
    public ServerResponse<ProductDetailVo> detail(@RequestParam("productId") Integer productId) {

        return productService.manageProductDetail(productId);
    }

    @RequestMapping("/set_sale_status.do")
    public ServerResponse<Product> setStatus(@RequestParam("productId") Integer productId, @RequestParam("status") Integer status) {

        return productService.setProductStatus(productId, status);
    }

    @RequestMapping("/upload.do")
    public ServerResponse upload(@RequestParam("upload_file") MultipartFile multipartFile, HttpServletRequest request) {

        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(multipartFile, path);

        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);

        return ServerResponse.createBySuccess(fileMap);
    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
        if (userService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }
    }


}
