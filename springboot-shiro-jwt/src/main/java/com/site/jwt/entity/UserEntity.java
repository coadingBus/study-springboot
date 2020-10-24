package com.site.jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Serializable {
 
	private static final long serialVersionUID = 1L;
 
	private Long id; // 主键ID
 
	private String name; // 登录用户名
 
	private String password; // 登录密码
 
	private String nickName; // 昵称
 
	private Boolean locked; // 账户是否被锁定

}