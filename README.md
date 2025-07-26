# 栖寓
  <p align="center">
    <image src="https://github.com/yxlannnn/QiYu/blob/main/web/QiYulog.png" width="500" height="500"/>
  <p/>
    
## 项目介绍
    
栖寓是一个基于Spring Boot的房屋租赁管理平台，提供了房源管理、租赁合同管理、预约看房、用户管理等功能。系统分为管理端（Admin）和用户端（App）两个子系统，实现了房屋租赁全流程的线上化管理。

## 技术栈
- 后端框架 ：Spring Boot 3.0.5
- 数据库 ：MySQL
- ORM框架 ：MyBatis-Plus 3.5.3.1
- 缓存 ：Redis
- 对象存储 ：MinIO
- API文档 ：Knife4j 4.1.0（基于Swagger）
- 认证授权 ：JWT
- 短信服务 ：阿里云SMS
- 其他工具 ：Lombok、EasyCaptcha等
## 系统架构
项目采用多模块架构设计，主要包含以下模块：

- model ：实体类模块，包含所有业务实体类和枚举类
- common ：公共模块，包含通用工具类、异常处理、配置类等
- web ：Web应用模块，包含管理端和用户端两个子模块
  - web-admin ：管理端，提供后台管理功能
  - web-app ：用户端，提供面向租户的功能

## 环境要求
- JDK 17+
- MySQL 5.7+
- Redis 6.0+
- Maven 3.6+
- MinIO（对象存储服务）
## 安装部署
### 1. 数据库配置
创建名为 lease 的数据库，并导入项目SQL脚本。

```
CREATE DATABASE lease DEFAULT CHARACTER 
SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
### 2. 修改配置文件
根据实际环境修改以下配置文件：

- web/web-admin/src/main/resources/application.yml
- web/web-app/src/main/resources/application.yml

主要配置项：

```
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lease?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2b8
    username: root
    password: your_password

# Redis配置
  data:
    redis:
      host: localhost
      port: 6379
      database: 0

# MinIO配置
minio:
  endpoint: http://localhost:9000
  access-key: minioadmin
  secret-key: minioadmin
  bucket-name: lease

# 阿里云短信服务配置（仅用户端需要）
aliyun:
  sms:
    access-key-id: your_access_key_id
    access-key-secret: your_access_key_secret
    endpoint: dysmsapi.aliyuncs.com
```

### 3. 访问系统
- 管理端： http://localhost:8080
- 用户端： http://localhost:8081
- API文档：
  - 管理端： http://localhost:8080/doc.html
  - 用户端： http://localhost:8081/doc.html

## 许可证
MIT License
