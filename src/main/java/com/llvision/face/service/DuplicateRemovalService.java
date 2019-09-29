package com.llvision.face.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llvision.face.utils.OkHttpUtils;
import com.llvision.face.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author wangwenteng
 *	去重相关
 */
@Service
@Slf4j
public class DuplicateRemovalService {

	@Value("${duplicateRemoval.ipAddress}")
	private String ipAddress;
	// private String ipAddress = "http://10.33.48.53:48813";

	private String determineUrl = "/api/v1/cachelib/determine";

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
	 * {
	 "version":"V1.0",
	 "seqnum":1,
	 "from":"",
	 "to":"",
	 "type":"CWBS",
	 "number":"1-1-1-1",
	 "data": {
	 "uid":"11111",
	 "name":"123456",
	 "image":"base64",
	 "boundingbox":{
	 "points_x":[1.00,2.00,3.00,4.00,5.00],
	 "points_y":[1.00,2.00,3.00,4.00,5.00],
	 "face_tl": [2.00, 2.00],
	 "face_br": [8.00, 8.00]
	 }
	 }
	 }

	 */

	public String duplicateRemoval(int userId, String imageName, String baseImage64, String landmarkJSON) {
		log.info("开始请求去重接口");
		JSONObject json = JSON.parseObject(landmarkJSON);
		Map mapType = JSON.parseObject(json.toJSONString(), Map.class);
		JSONObject data = new JSONObject();
		data.put("image", baseImage64);
		data.put("uid", userId + "");
		data.put("name", imageName);
		data.put("boundingbox", mapType);
		JSONObject commonParam = getCommonParam();
		commonParam.put("data", data);
		log.info("请求地址：" + ipAddress + determineUrl);
		String responseString = OkHttpUtils.jsonOkHttp(ipAddress + determineUrl, commonParam);
		log.info("responseString" + responseString);
		if (StringUtils.isNotEmpty(responseString)) {
			JSONObject compareFace = (JSONObject) JSONObject.parse(responseString);
			if (isResponseOk(compareFace)) {
				JSONObject dataJSON = compareFace.getJSONObject("data");
				return dataJSON.toString();
			}
		}
		return null;
	}

	public boolean isResponseOk(JSONObject responseJSON) {
		return responseJSON.getIntValue("code") == 0;
	}
}
