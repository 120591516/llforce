package com.llvision.face.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.llvision.face.entity.Person;
import com.llvision.face.utils.StringUtils;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class Llvision00052Service {
	public static OkHttpClient okHttpClient = new OkHttpClient();
	private final Logger log = LoggerFactory.getLogger("");
	@Value("${llvision.ipAddress}")
	// private String base_url = "http://10.165.32.145:11180";
	private String ipAddress;
	@Value("${similarityNum}")
	private int similarityNum;

	private String featurePath = "/api/v1/facelib/feature";

	private String queryPathLandMark = "/api/v1/identification/landmark";

	/**
	 * 公共请求参数
	 * 
	 * @return
	 * 
	 */
//	{
//		"version":"V1.0",
//		"seqnum":1,
	// "c":"",
//		"to":"",
//		"type":"CWBS",
//		"number":"1-1-1-1",
//		"data": {
//		   "id":"1",
//		   "name":"2322.jpg",
//		   "path":"http://127.0.0.1",
//		   "alertid":["1","2","3"],
//		   "image":"base64"
//		}
//	}

	public JSONObject getCommonParam() {
		JSONObject commonParam = new JSONObject();
		commonParam.put("version", "V1.0");
		commonParam.put("seqnum", 1);
		commonParam.put("from", "");
		commonParam.put("to", "");
		commonParam.put("type", "CWBS");
		commonParam.put("number", "1-1-1-1");
		return commonParam;
	}
	/**
	 * application/json 格式post请求
	 * 
	 * @param url
	 *            地址
	 * @param params
	 *            参数 Map<String,Object></>
	 * @return
	 */
	public String jsonOkHttp(String url, Map<String, Object> params) {
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(params));
		Request request = new Request.Builder().url(url).put(requestBody).build();
		Call call = okHttpClient.newCall(request);
		return getResponseString(call);
	}


	/**
	 * {
    "version":"V1.0",
    "seqnum":1,
    "from":"",
    "to":"",
    "type":"CWBS",
    "number":"1-1-1-1",
    "data": {
        "libid":"library-1",
        "name":"123456",
        "type": 0,
        "feature":"base64",
    }
}

	 * @param request
	 * @param person
	 * @return
	 */
	public boolean putFeature(HttpServletRequest request, Person person, Integer offlineFaceId) {
		JSONObject data = new JSONObject();
		data.put("libid", offlineFaceId);
		data.put("name", person.getId() + "");
		data.put("feature", person.getHash());
		data.put("type", 0);
		JSONObject commonParam = getCommonParam();
		commonParam.put("data", data);
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
				JSON.toJSONString(commonParam));
		log.info("putFeature:>>请求参数：" + JSON.toJSONString(commonParam));
		Request request1 = new Request.Builder().url(ipAddress + featurePath).post(requestBody).build();
		Call call = okHttpClient.newCall(request1);
		String responseString = getResponseString(call);
		log.info("putFeature result:>>" + responseString);
		JSONObject upload = (JSONObject) JSONObject.parse(responseString);
		return isResponseOk(upload);
	}



	public boolean isResponseOk(JSONObject responseJSON) {
		return responseJSON.getIntValue("code") == 0;
	}
	/**
	 * 获取返回数据
	 * 
	 * @param call
	 * @return
	 */
	private String getResponseString(Call call) {
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

	public String compareFaceLandmark(String baseImage64, String landmarkJSON, List<Integer> libiId) {
		log.info("compareFaceLandmark>>>>>");
		log.info("libiId{}",JSON.toJSONString(libiId));
		if (StringUtils.isNotEmpty(landmarkJSON)) {
			JSONObject json = JSON.parseObject(landmarkJSON);
			Map mapType = JSON.parseObject(json.toJSONString() ,Map.class);
//			Map<String, Object> newMap = new HashMap<>();
//			newMap.put("points_x", mapType.get("points_x"));
			JSONObject data = new JSONObject();
			data.put("resultnumber", similarityNum);
			data.put("image", baseImage64);
			data.put("alertid", JSON.toJSON(libiId));
			data.put("boundingbox", mapType);
			JSONObject commonParam = getCommonParam();
			commonParam.put("data", data);
			RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
					JSON.toJSONString(commonParam));
			Request request1 = new Request.Builder().url(ipAddress + queryPathLandMark).post(requestBody).build();
			Call call = okHttpClient.newCall(request1);
			String responseString = getResponseString(call);
			log.info("responseString:"+responseString);
			if (null != responseString) {
				JSONObject compareFace = (JSONObject) JSONObject.parse(responseString);
				if (isResponseOk(compareFace)) {
					JSONObject dataJSON = compareFace.getJSONObject("data");
					JSONArray list = dataJSON.getJSONArray("list");
					if (null != list && list.size() > 0) {
						return list.toString();
					}
				}
			}
		}
		return null;
	}

	public boolean deleteFeature(long personId) {
		JSONObject data = new JSONObject();
		data.put("name", personId + "");
		JSONObject commonParam = getCommonParam();
		commonParam.put("data", data);
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
				JSON.toJSONString(commonParam));
		log.info("deleteFeature:>>请求参数：" + JSON.toJSONString(commonParam));
		Request request1 = new Request.Builder().url(ipAddress + featurePath).delete(requestBody).build();
		Call call = okHttpClient.newCall(request1);
		String responseString = getResponseString(call);
		log.info("deleteFeature:>>" + responseString);
		JSONObject upload = (JSONObject) JSONObject.parse(responseString);
		return isResponseOk(upload);
	}

}

