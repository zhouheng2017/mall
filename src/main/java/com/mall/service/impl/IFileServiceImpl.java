package com.mall.service.impl;

import com.google.common.collect.Lists;
import com.mall.service.IFileService;
import com.mall.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-04
 * @Time: 16:27
 */
@Service
@Slf4j
public class IFileServiceImpl implements IFileService {
    @Override
    public String upload(MultipartFile multipartFile, String path) {

        String fileName = multipartFile.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        log.info("文件开始上传，文件名{}， 路径：{}， 新文件名{}", fileName, path, uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(path, uploadFileName);

        try {
            multipartFile.transferTo(targetFile);
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
//            删除文件/
            targetFile.delete();
        } catch (IOException e) {
            log.error("文件上传异常", e);
            return null;
        }
        return targetFile.getName();

    }

   /* public static void main(String[] args) {
        File fileDir = new File("E:/path/like");

        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
    }*/
}
