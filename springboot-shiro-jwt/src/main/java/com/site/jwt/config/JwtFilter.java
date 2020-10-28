package com.site.jwt.config;

import java.io.PrintWriter;
 
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.site.jwt.util.CookieUtils;
import com.site.jwt.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
 
/**
 * 自定义的认证过滤器，用来拦截Header中携带 JWT token的请求
 * 因为放行了登陆请求，所以这里拦截其他的携带JWT token请求
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

	/**
	 * 前置处理
	 */
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
		HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
		// 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
		if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
			httpServletResponse.setStatus(HttpStatus.OK.value());
			return false;
		}
		return super.preHandle(request, response);
	}
 
	/**
	 * 后置处理
	 */
	@Override
	protected void postHandle(ServletRequest request, ServletResponse response) {
		// 添加跨域支持
		this.fillCorsHeader(WebUtils.toHttp(request), WebUtils.toHttp(response));
	}
 
	/**
	 * 过滤器拦截请求的入口方法 
	 * 返回 true 则允许访问 
	 * 返回false 则禁止访问，会进入 onAccessDenied()
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		log.info("===============isAccessAllowed============");
		// 原用来判断是否是登录请求，在本例中不会拦截登录请求，用来检测Header中是否包含 JWT token 字段
		if (this.isLoginAttempt(request, response)) {
			log.info("===============isLoginAttempt============");
			return false;
		}else {
			return true;
		}
	}
 
	/**
	 * 检测Header中是否包含 JWT token 字段
	 */
	@Override
	protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
		//String header = ((HttpServletRequest) request).getHeader(JwtUtils.AUTH_HEADER);
		Cookie[] cookies = ((HttpServletRequest) request).getCookies();
		Cookie cookie = CookieUtils.getCookie(cookies, JwtUtils.AUTH_HEADER);
		String token = cookie.getValue();
		log.info("token==>"+token);
		//log.info("header==>"+header);
		return token == null;
	}

 
	/**
	 * 从 Header 里提取 JWT token
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		String authorization = httpServletRequest.getHeader(JwtUtils.AUTH_HEADER);
		JwtToken token = new JwtToken(authorization);
		return token;
	}
 
	/**
	 * isAccessAllowed()方法返回false，会进入该方法，表示拒绝访问
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		log.info("===============onAccessDenied============");
		HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);
		httpResponse.setCharacterEncoding("UTF-8");
		httpResponse.setContentType("application/json;charset=UTF-8");
		httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
		PrintWriter writer = httpResponse.getWriter();
		writer.write("{\"errCode\": 401, \"msg\": \"UNAUTHORIZED\"}");
		fillCorsHeader(WebUtils.toHttp(servletRequest), httpResponse);
		return false;
	}
 
	///**
	// * Shiro 利用 JWT token 登录成功，会进入该方法
	// */
	//@Override
	//protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
	//		ServletResponse response) throws Exception {
	//
	//	log.info("===============onLoginSuccess============");
	//	HttpServletResponse httpResponse = WebUtils.toHttp(response);
	//	String newToken = null;
	//	if (token instanceof JwtToken) {
	//		newToken = JwtUtils.refreshTokenExpired(token.getCredentials().toString(), JwtUtils.SECRET);
	//	}
	//	if (newToken != null) {
	//		httpResponse.setHeader(JwtUtils.AUTH_HEADER, newToken);
	//	}
	//	return true;
	//}

 
	/**
	 * 添加跨域支持
	 */
	protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,HEAD");
		httpServletResponse.setHeader("Access-Control-Allow-Headers",
				httpServletRequest.getHeader("Access-Control-Request-Headers"));
	}
}