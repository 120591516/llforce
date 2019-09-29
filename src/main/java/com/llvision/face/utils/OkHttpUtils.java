package com.llvision.face.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class OkHttpUtils {
    private static final Logger log = LoggerFactory.getLogger(OkHttpUtils.class);

    public static OkHttpClient okHttpClient=new OkHttpClient();

    /**
     * post请求
     * @param url   api接口地址
     * @param params  参数 Map<String,Object></>
     * @return
     */
    public static String commonPostOkHttp(String url,Map<String,Object> params){
        FormBody.Builder formBody = new FormBody.Builder();
        if(!params.isEmpty()){
            Set<String> keySet = params.keySet();
            for (String key:keySet){
                formBody.add(key, String.valueOf(params.get(key)));
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String responseStr = response.body().string();
            JSONObject result = JSONObject.parseObject(responseStr);
            response.close();
            return result.toJSONString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * post请求
     * @param url   api接口地址
     * @param params  参数 Map<String,Object></>
     * @param headerMap 自定义header
     * @return
     */
    public static String commonPostOkHttp(String url,Map<String,Object> params,Map<String,Object> headerMap){
        FormBody.Builder formBody = new FormBody.Builder();
        if(!params.isEmpty()){
            Set<String> keySet = params.keySet();
            for (String key:keySet){
                formBody.add(key, String.valueOf(params.get(key)));
            }
        }
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if(!headerMap.isEmpty()){
            Set<String> keySet = headerMap.keySet();
            for (String key:keySet){
                builder.addHeader(key, String.valueOf(headerMap.get(key)));
            }
        }
        Request request = builder.post(formBody.build()).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String responseStr = response.body().string();
            JSONObject result = JSONObject.parseObject(responseStr);
            response.close();
            return result.toJSONString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get请求
     * @param url  地址，如有参数url?xxx=xxx&xxx=xxx
     * @return
     */
    public static String commonGetOkHttp(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String responseStr = response.body().string();
            JSONObject result = JSONObject.parseObject(responseStr);
            response.close();
            return result.toJSONString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get请求
     * @param url  地址，如有参数url?xxx=xxx&xxx=xxx
     * @param headerMap 自定义header
     * @return
     */
    public static String commonGetOkHttp(String url,Map<String,Object> headerMap){
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if(!headerMap.isEmpty()){
            Set<String> keySet = headerMap.keySet();
            for (String key:keySet){
                builder.addHeader(key, String.valueOf(headerMap.get(key)));
            }
        }
        Request request = builder.build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String responseStr = response.body().string();
            JSONObject result = JSONObject.parseObject(responseStr);
            response.close();
            return result.toJSONString();
        } catch (IOException e) {
            log.info("接口调用失败"+e.getMessage());
        }
        return null;
    }

    /**
     * application/json 格式post请求
     * @param url  地址
     * @param params 参数 Map<String,Object></>
     * @return
     */
    public static String jsonOkHttp(String url,Map<String,Object> params){
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(params));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String responseStr = response.body().string();
            JSONObject result = JSONObject.parseObject(responseStr);
            response.close();
			return result == null ? "" : result.toJSONString();
        } catch (IOException e) {
            log.info("接口调用失败"+e.getMessage());
        }
        return null;
    }

    /**
     * application/json 格式post请求
     * @param url  地址
     * @param params 参数 Map<String,Object></>
     * @param headerMap 自定义header
     * @return
     */
    public static String jsonOkHttp(String url,Map<String,Object> params,Map<String,Object> headerMap){
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(params));
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if(!headerMap.isEmpty()){
            Set<String> keySet = headerMap.keySet();
            for (String key:keySet){
                builder.addHeader(key, String.valueOf(headerMap.get(key)));
            }
        }
        Request request = builder.post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String responseStr = response.body().string();
            JSONObject result = JSONObject.parseObject(responseStr);
            response.close();
            if(result!=null){
                return result.toJSONString();
            }
        } catch (IOException e) {
            log.info("接口调用失败"+e.getMessage());
        }
        return null;
    }

	/**
	 * application/json 格式post请求
	 * 
	 * @param url
	 *            地址
	 * @param params
	 *            参数 Map<String,Object></>
	 * @param headerMap
	 *            自定义header
	 * @return
	 */
	public static String jsonOkHttp(String url, JSONArray params, Map<String, Object> headerMap) {
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toJSONString());
		Request.Builder builder = new Request.Builder();
		builder.url(url);
		if (!headerMap.isEmpty()) {
			Set<String> keySet = headerMap.keySet();
			for (String key : keySet) {
				builder.addHeader(key, String.valueOf(headerMap.get(key)));
			}
		}
		Request request = builder.post(requestBody).build();
		Call call = okHttpClient.newCall(request);
		try {
			Response response = call.execute();
			String responseStr = response.body().string();
			JSONObject result = JSONObject.parseObject(responseStr);
			response.close();
			return result.toJSONString();
		} catch (IOException e) {
			log.info("接口调用失败" + e.getMessage());
		}
		return null;
	}
    /**
     * fromData 格式post请求
     * @param url  接口地址
     * @param requestBody         RequestBody requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file))
                                    .build();
     * @return
     */
    public static String fromDataOkHttp(String url,RequestBody requestBody){
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String responseStr = response.body().string();
            JSONObject result = JSONObject.parseObject(responseStr);
            response.close();
            return result.toJSONString();
        } catch (IOException e) {
            log.info("接口调用失败"+e.getMessage());
        }
        return null;
    }

    /**
     * fromData 格式post请求
     * @param url  接口地址
     * @param requestBody         RequestBody requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file))
                                    .build();
     * @param headerMap 自定义header
     * @return
     */
    public static String fromDataOkHttp(String url,RequestBody requestBody,Map<String,Object> headerMap){
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if(!headerMap.isEmpty()){
            Set<String> keySet = headerMap.keySet();
            for (String key:keySet){
                builder.addHeader(key, String.valueOf(headerMap.get(key)));
            }
        }
        Request request = builder.post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String responseStr = response.body().string();
            JSONObject result = JSONObject.parseObject(responseStr);
            response.close();
            return result.toJSONString();
        } catch (IOException e) {
            log.info("接口调用失败"+e.getMessage());
        }
        return null;
    }

}
