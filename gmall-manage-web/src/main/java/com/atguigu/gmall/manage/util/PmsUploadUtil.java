package com.atguigu.gmall.manage.util;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PmsUploadUtil {
    public static String uploadImage(MultipartFile multipartFile) {
        String imgUrl =  "http://192.168.245.100";

        //配置fdfs全局连接地址
        String tracker = PmsUploadUtil.class.getResource("/tracker.conf").getPath();
        try {
            ClientGlobal.init(tracker);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TrackerClient trackerClient = new TrackerClient();
        //获得一个trackerServer的实例
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //通过tracker获得一个Storage连接客户端
        StorageClient storageClient = new StorageClient(trackerServer, null);

        try {
            //获取上传文件的二进制对象
            byte[] bytes = multipartFile.getBytes();
            //获取文件后缀名
            String originalFilename = multipartFile.getOriginalFilename();//得到文件全名
            System.out.println(originalFilename);
            int i = originalFilename.lastIndexOf(".");
            String extName = originalFilename.substring(i + 1);

            String[] uploadFile = storageClient.upload_file(bytes, extName, null);


            for (String uploadInfo : uploadFile) {
                imgUrl+="/"+uploadInfo;
                System.out.println(imgUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imgUrl;
    }
}
