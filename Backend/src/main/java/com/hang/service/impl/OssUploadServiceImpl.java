package com.hang.service.impl;


import com.google.gson.Gson;
import com.hang.emuns.AppHttpCodeEnum;
import com.hang.exception.SystemException;
import com.hang.result.ResponseResult;
import com.hang.service.UploadService;
import com.hang.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UploadServiceImpl
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/7 19:11
 * @Version 1.0
 */
@Data
@Service
public class OssUploadServiceImpl implements UploadService {

    @Override
    public ResponseResult uploadImg(MultipartFile[] imgs) {
        // 遍历每个文件进行处理
        List<String> urls = new ArrayList<>();
        for (MultipartFile img : imgs) {
            // 获取文件名进行判断
            String originalFilename = img.getOriginalFilename();
            // 对原始文件名进行判断
            if (!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")) {
                throw new SystemException(AppHttpCodeEnum.FILENAME_ERROR);
            }
            // 判断通过 上传文件到OSS
            String filePath = PathUtils.generateFilePath(originalFilename);
            String url = UploadOss(img, filePath); // 2023/8/7/uuid.png
            urls.add(url);
        }
        return ResponseResult.okResult(urls);
    }

    @Override
    public ResponseResult uploadImg(MultipartFile img) {

        // 获取文件名进行判断
        String originalFilename = img.getOriginalFilename();
        // 对原始文件名进行判断
        if (!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")) {
            throw new SystemException(AppHttpCodeEnum.FILENAME_ERROR);
        }
        // 判断通过 上传文件到OSS
        String filePath = PathUtils.generateFilePath(originalFilename);
        String url = UploadOss(img, filePath); // 2023/8/7/uuid.png

        return ResponseResult.okResult(url);
    }

    //@Value("${oss.accessKey}")
    private String accessKey = "cuwMLlcQi3v83Q9WmNeqn2FSq2TCvE8C5ok7McfH";
    //@Value("${oss.secretKey}")
    private String secretKey = "V-LBcGJAuptis_hXNsWLuuJg-wioAEBsN9zFiMKT";
    //@Value("${oss.bucket}")
    private String bucket = "yc-shop";

    private static final String _web = "http://rzl9bicnx.hn-bkt.clouddn.com/";

    private String UploadOss(MultipartFile imgFile, String filePath) {

        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(cfg);
        String key = filePath;
        try {
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return _web + key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "fail";
    }
}
