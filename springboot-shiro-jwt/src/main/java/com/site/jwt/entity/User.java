package com.site.jwt.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 客户端用户表(User)实体类
 *
 * @author lmk
 * @since 2020-10-24 14:31:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("客户端用户表")
public class User implements Serializable {
    private static final long serialVersionUID = 338107033047862563L;
    /**
     * 用户主键ID
     */
    @ApiModelProperty("用户主键ID")
    private String userId;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String userName;

    /**
     * 用户手机号
     */
    @ApiModelProperty("用户手机号")
    private String userPhone;

    /**
     * 用户登录密码
     */
    @ApiModelProperty("用户登录密码")
    private String userPassword;

}