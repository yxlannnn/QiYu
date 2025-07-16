package com.atguigu.lease.model.entity;

import com.atguigu.lease.model.enums.BaseStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "用户信息表")
@TableName(value = "user_info")
@Data
public class UserInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "手机号码（用做登录用户名）")
    @TableField(value = "phone")
    private String phone;


    //TODO 如果不想让字段出现在json信息里传给前端，可以使用@JsonIgnore注解或者在@TableField注解中添加select = false
    @Schema(description = "密码")
    @TableField(value = "password",select = false)
    private String password;

    @Schema(description = "头像url")
    @TableField(value = "avatar_url")
    private String avatarUrl;

    @Schema(description = "昵称")
    @TableField(value = "nickname")
    private String nickname;

    @Schema(description = "账号状态")
    @TableField(value = "status")
    private BaseStatus status;


}