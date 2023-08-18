package com.hang.controller;

import com.hang.result.ResponseResult;
import com.hang.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName UploadController
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/7 19:10
 * @Version 1.0
 */
@RestController
@RequestMapping("/images")
public class UploadController {

    @Autowired
    private UploadService uploadService;


    @PostMapping("/upload")
    public ResponseResult uploadImg(@RequestParam("file") MultipartFile multipartFile) {
        try {
            return uploadService.uploadImg(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传上传失败");
        }
    }
    /**
     * 上传多组照片
     * @param multipartFile
     * @return
     */
//    public ResponseResult uploadImg(MultipartFile[] multipartFile) {
//        try {
//            return uploadService.uploadImg(multipartFile);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("文件上传上传失败");
//        }
//    }
}
