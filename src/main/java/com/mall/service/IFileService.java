package com.mall.service;

import org.springframework.web.multipart.MultipartFile; /**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-04
 * @Time: 16:26
 */
public interface IFileService {
    /**
     * 上传文件
     * @param multipartFile
     * @param path
     * @return
     */
    String upload(MultipartFile multipartFile, String path);
}
