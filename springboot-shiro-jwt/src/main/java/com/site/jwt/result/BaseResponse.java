package com.site.jwt.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> implements Serializable {
 
	private static final long serialVersionUID = 1L;
 
	/**
	 * 状态码
	 */
	private int Code;
 
	/**
	 * 消息内容
	 */
	private String msg;
 
	/**
	 * 返回数据
	 */
	private T data;

 
}