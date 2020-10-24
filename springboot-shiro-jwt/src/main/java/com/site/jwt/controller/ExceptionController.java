package com.site.jwt.controller;

import javax.servlet.http.HttpServletRequest;

import com.site.jwt.result.BaseResponse;
import org.apache.shiro.ShiroException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
 

 
@RestControllerAdvice
/**
 * 处理全局异常
 */
public class ExceptionController {
 
	// 捕捉shiro的异常
	@ExceptionHandler(ShiroException.class)
	public Object handleShiroException(ShiroException e) {
		BaseResponse<Object> ret = new BaseResponse<Object>();
		ret.setCode(401);
		ret.setMsg(e.getMessage());
		return ret;
	}
 
	// 捕捉其他所有异常
	@ExceptionHandler(Exception.class)
	public Object globalException(HttpServletRequest request, Throwable ex) {
		BaseResponse<Object> ret = new BaseResponse<Object>();
		ret.setCode(401);
		ret.setMsg(ex.getMessage());
		return ret;
	}
}