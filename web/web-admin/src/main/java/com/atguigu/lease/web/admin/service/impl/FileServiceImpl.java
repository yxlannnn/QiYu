package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.common.minio.MinioProperties;
import com.atguigu.lease.web.admin.service.FileService;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {


    @Autowired
    private  MinioClient minioClient;

    @Autowired
    private MinioProperties properties;



    @Override
    public String upload(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //TODO 设置文件上传的方法实现 重点！！！

        //TODO 异常处理：如果minio服务停止了，文件上传异常处理方法




        //判断桶是否存在
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs
                            .builder()
                            .bucket(properties.getBucketName())
                            .build());
            //如果不存在
            if (!bucketExists){
                //创建桶，桶的名字
                minioClient.makeBucket(MakeBucketArgs
                                .builder()
                                .bucket(properties.getBucketName())
                                .build());

                //设置桶的权限，桶的名字，权限配置
                minioClient.setBucketPolicy(SetBucketPolicyArgs
                        .builder()
                        .bucket(properties.getBucketName())
                        .config(createBucketPolicyConfig(properties.getBucketName()))
                        .build());

                //设置唯一不重复的文件名
                String filename = new SimpleDateFormat("yyyyMMdd")
                        .format(new Date()) + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

                //设置放入桶内的对象，桶的名字，文件上传的方式（inputstream流），上传文件的别名（唯一不可重复）
                minioClient.putObject(PutObjectArgs
                        .builder()
                        .bucket(properties.getBucketName())
                        .stream(file.getInputStream(),file.getSize(),-1)
                        .object(filename)
                        .contentType(file.getContentType())//自定义文件上传类型，使得直接访问图片时是直接显示还是会进行下载
                        .build());

                System.out.println("上传完成");

                //返回添加的对象的url
//                String url = properties.getEndpoint()+"/"+properties.getBucketName()+"/"+filename;

                return String.join("/",properties.getEndpoint(),properties.getBucketName(),filename);


            }else {
                //设置唯一不重复的文件名
                String filename = new SimpleDateFormat("yyyyMMdd")
                        .format(new Date()) + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

                //设置放入桶内的对象，桶的名字，文件上传的方式（inputstream流），上传文件的别名（唯一不可重复）
                minioClient.putObject(PutObjectArgs
                        .builder()
                        .bucket(properties.getBucketName())
                        .stream(file.getInputStream(),file.getSize(),-1)
                        .object(filename)
                        .contentType(file.getContentType())//自定义文件上传类型，使得直接访问图片时是直接显示还是会进行下载
                        .build());

                System.out.println("上传完成");

                //返回添加的对象的url
//                String url = properties.getEndpoint()+"/"+properties.getBucketName()+"/"+filename;
                return String.join("/",properties.getEndpoint(),properties.getBucketName(),filename);

            }

    }

    private String createBucketPolicyConfig(String bucketName) {

        return """
            {
              "Statement" : [ {
                "Action" : "s3:GetObject",
                "Effect" : "Allow",
                "Principal" : "*",
                "Resource" : "arn:aws:s3:::%s/*"
              } ],
              "Version" : "2012-10-17"
            }
            """.formatted(bucketName);
    }


}
