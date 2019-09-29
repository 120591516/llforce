package com.llvision.face.license;

import com.alibaba.fastjson.JSONObject;
import com.llvision.face.constants.Constants;
import com.llvision.face.service.WatchDogService;
import com.llvision.face.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@Component
public class LicenseInterceptor implements HandlerInterceptor {
	private static final String HEADER_NAME_APP_ID = "appid";
	@Resource
	private WatchDogService watchDogService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {
		//拦截
		/*if (!watchDogService.getInfo()) {
			return false;
		}*/
		LogUtil.traceLogId();
		log.info("---> method:{} url->{} user-agent:{}", request.getMethod(),request.getRequestURI(),request.getHeader("User-Agent"));
		JSONObject jsonObject = new JSONObject();
		jsonObject.putAll(request.getParameterMap());
		if (!request.getRequestURI().equals("/web/v1/offline/uploadFacePic")) {
			log.info("params:{} lang:{}",jsonObject.toJSONString(),request.getSession().getAttribute("lang_session"));
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		long endtime = System.currentTimeMillis();
		long useTime = endtime - Long.parseLong(MDC.get(Constants.METHOD_START_TIME));
		log.info("<--- method:{} url{} useTime:{}ms",request.getMethod(),request.getRequestURI(),useTime);
	}
}
