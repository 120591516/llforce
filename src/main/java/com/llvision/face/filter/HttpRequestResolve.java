package com.llvision.face.filter;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

@Component
@Slf4j
public class HttpRequestResolve {

	public static String getResponseBody(HttpServletResponse response){
		String result = new String();
		try {
			result = response.getOutputStream().toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 解析请求实体信息
	 * 
	 * @param request
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static String getRequestBody(HttpServletRequest request) throws InterruptedException {
		String reqeustUrl = request.getRequestURI();
		Enumeration enu = request.getParameterNames();
		String paraName = "";
		String userId = "";
		JSONObject jsonObj = new JSONObject();
		while (enu.hasMoreElements()) {
			paraName = (String) enu.nextElement();
			log.info(paraName + ": " + request.getParameter(paraName));
			if (paraName.contains("Base64") || paraName.contains("Pic")) {
				jsonObj.put(paraName, "...");
			} else {
				jsonObj.put(paraName, request.getParameter(paraName));

			}
			if (paraName.equals("userId")) {
				userId = request.getParameter(paraName);
			}
		}
		Enumeration attrs = null;
		attrs = request.getHeaderNames();
		while (attrs.hasMoreElements()) {
			String attr = (String) attrs.nextElement();
			log.info(attr + ": " + request.getHeader(attr));
			jsonObj.put(attr, request.getHeader(attr));
		}
		log.info(jsonObj.toJSONString());
		return jsonObj.toString();
	}

	/**
	 * 获取客户端的IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_client_ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		// 如果是多级代理，那么取第一个ip为客户ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
		}
		return ip;
	}

}
