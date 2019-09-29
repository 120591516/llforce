package com.llvision.face.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llvision.face.config.SftpConfig;
import com.llvision.face.constants.Constants;
import com.llvision.face.dao.biz.OfflineFaceBiz;
import com.llvision.face.dao.biz.PersonBiz;
import com.llvision.face.dao.biz.WareHouseFaceBiz;
import com.llvision.face.dao.mapper.PersonMapper;
import com.llvision.face.dao.mapper.WareHouseFaceMapper;
import com.llvision.face.entity.OffLineFace;
import com.llvision.face.entity.Person;
import com.llvision.face.entity.WareHouseFace;
import com.llvision.face.service.IOffLineFaceService;
import com.llvision.face.service.IPersonService;
import com.llvision.face.utils.*;
import com.llvision.face.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

/**
 * @Author: guoyc
 * @Date: 2019/5/14 15:53
 * @Version 1.0
 */
@Service
@Slf4j
@Transactional
public class PersonServiceImpl implements IPersonService {

    @Resource
    private PersonMapper personMapper;

    @Resource
    private WareHouseFaceBiz wareHouseFaceBiz;

    @Value("${file_path}")
    private String file_path;

    @Value("${flag}")
    private String flag;

    @Value("${featureExtractUrl}")
    private String featureExtractUrl;

    @Value("${visit_url}")
    private String visit_url;

    @Value("${pic_url}")
    private String pic_url;

    @Resource
    private Llvision00052Service llvision00052Service;

    @Resource
    private WareHouseFaceMapper wareHouseFaceMapper;

    @Resource
    private OfflineFaceBiz offlineFaceBiz;

    @Resource
    private IOffLineFaceService offLineFaceService;

    @Resource
    private PersonBiz personBiz;

    @Resource
    private SftpConfig sftpConfig;

    @Override
    public Result editFace(Integer userId, Long personId, String name, String card, Integer sex, String nation, String birthday, String address, String model, Integer warn, Integer libId,String appId, HttpServletRequest request) throws ParseException {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();
        log.info("editFace params --> userId:{} personId:{} name:{} card:{} sex:{} nation:{} birthday:{} address:{} model:{} warn:{} libId:{}",
                userId,personId,name,card,sex,nation,birthday,address,model,warn,libId);
        Person person = personBiz.getPersonByIdAndAppId(personId,appId);
        if (person == null) { // 如果实普通管理员无法访问该列表
            return Result.error(Constants.PERMISSION_CODE_ERROR, requestContext.getMessage("xxbz"));
        }
        if (StringUtils.isNotEmpty(name) && !StringUtils.regexName(name)) {
            r.setMsg(requestContext.getMessage("qwsstszf"));
            r.setCode(Constants.OTHER_ERROR_CODE_ERRROR);
            return r;
        }
        Person query = new Person();
        query.setName(name);
        List<Person> personByPersonAndWarehouseId = personBiz.getPersonByPersonAndWarehouseId(query, libId);
        if (personByPersonAndWarehouseId.size() > 0 && personByPersonAndWarehouseId.get(0).getId().intValue() != personId.intValue()) {
            r.setMsg(requestContext.getMessage("gxmycz"));
            r.setCode(Constants.OTHER_ERROR_CODE_ERRROR);
            return r;
        }
        if (StringUtils.isNotEmpty(card)) {
            query.setName(null);
            query.setCard(card);
            personByPersonAndWarehouseId = personBiz.getPersonByPersonAndWarehouseId(query, libId);
            if (personByPersonAndWarehouseId.size() > 0
                    && personByPersonAndWarehouseId.get(0).getId().intValue() != personId.intValue()) {
                r.setMsg(requestContext.getMessage("gsfzhycz"));
                r.setCode(Constants.OTHER_ERROR_CODE_ERRROR);
                return r;
            }
        }
        String filePath;
        String dataFilePath = "";
        // 判断文件是否为空
//        if (file != null) {
//            try {
//                //获取文件后缀
//                String suffix = file.getOriginalFilename().trim().substring(file.getOriginalFilename().indexOf("."));
//                String num = UUID.randomUUID().toString();
//                // 服务器存储文件路径
//                String pathName = Constants.UPLOAD_FACE_URL + num + suffix;
//                // 文件保存路径
//                if (Constants.CUSTOMER.equals("pudong")) {
//                    filePath = file_path + "/" + pathName;
//
//                } else {
//                    filePath = request.getSession().getServletContext().getRealPath("/") + pathName;
//                }
//                // 转存文件
//                file.transferTo(new File(filePath));
//                // 数据库存放的路径
//                dataFilePath = "/" + pathName;
//            } catch (Exception e) {
//                log.info("编辑人脸失败", e);
//            }
//        }
        if (person != null) {
            if (!dataFilePath.equals("")) {
                person.setPic(dataFilePath);
            }
            if (!StringUtils.isEmpty(name)) {
                person.setName(name);
            }
            if (StringUtils.isNotEmpty(card)) {
                person.setCard(card);
            }
            if (StringUtils.isNotEmpty(address)) {
                person.setAddress(address);
            }
            if (StringUtils.isNotEmpty(nation)) {
                person.setNation(nation);
            }
            if (StringUtils.isNotEmpty(sex + "")) {
                person.setSex(sex);
            }
            if (!StringUtils.isEmpty(birthday)) {
                person.setBirthday(birthday);
            } else {
                person.setBirthday(null);
            }
            if (!StringUtils.isEmpty(model)) {
                person.setModel(model);
            }
            if (!StringUtils.isEmpty(warn + "")) {
                person.setIsWarning(warn);
            }
            person.setUpdateUser(userId);
            person.setUpdateTime(new Date());
            int maxMark = personBiz.getMaxMark();
            person.setMark(maxMark + 1);
            person.setAction(2);
            personMapper.updateByPrimaryKeySelective(person);
        }
        Map<String, Object> dateMap = new HashMap<>();//返回给客户端
        dateMap.put("userId", userId);
        r.setData(dateMap);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }

    @Override
    public Result addFace(Integer userId, String name, String card, Integer sex, String nation, String birthday, String address, Integer status, Integer libId, MultipartFile file,String appId, HttpServletRequest request) throws ParseException {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();

        OffLineFace offlineFace = offlineFaceBiz.getOfflineFaceValidByIdAndAppId(libId,appId);
        if (offlineFace == null) { // 不同appId不能访问
            return Result.error(Constants.PERMISSION_CODE_ERROR, requestContext.getMessage("xxbz"));
        }
        // 查询人像库是否超限
        if (!offLineFaceService.checkFaceCount(appId,0)) {
            return Result.error(Constants.PERMISSION_CODE_ERROR, requestContext.getMessage("rxkydsx"));
        }
        if (!StringUtils.regexName(name)) {
            r.setMsg(requestContext.getMessage("qwsstszf"));
            r.setCode(Constants.OTHER_ERROR_CODE_ERRROR);
            return r;
        }
        Person query = new Person();
        query.setName(name);
        List<Person> personByPersonAndWarehouseId = personBiz.getPersonByPersonAndWarehouseId(query, libId);
        if (personByPersonAndWarehouseId.size() > 0) {
            r.setMsg(requestContext.getMessage("gxmycz"));
            r.setCode(Constants.OTHER_ERROR_CODE_ERRROR);
            return r;
        }
        if (StringUtils.isNotEmpty(card)) {
            query.setName(null);
            query.setCard(card);
            personByPersonAndWarehouseId = personBiz.getPersonByPersonAndWarehouseId(query, libId);
            if (personByPersonAndWarehouseId.size() > 0) {
                r.setMsg(requestContext.getMessage("gsfzhycz"));
                r.setCode(Constants.OTHER_ERROR_CODE_ERRROR);
                return r;
            }
        }
        String filePath = "";
        String dataFilePath = "";
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                //获取文件后缀
                String suffix = file.getOriginalFilename().trim().substring(file.getOriginalFilename().indexOf("."));
                String num = UUID.randomUUID().toString();
                // 服务器存储文件路径
                String pathName = Constants.UPLOAD_FACE_URL + num + suffix;
                // 文件保存路径

                SFTPUtil sftpUtil = new SFTPUtil(sftpConfig.getHost(),sftpConfig.getUsername(),sftpConfig.getPassword(),sftpConfig.getPort());
                sftpUtil.connect();
                InputStream is = file.getInputStream();
                String filename = num + suffix;
                sftpUtil.upload(Constants.UPLOAD_FACE_URL,filename,is);
                sftpUtil.disconnect();

                // 数据库存放的路径
                dataFilePath = pathName;
            } catch (Exception e) {
                log.info("添加人脸失败", e);
            }
        }

        Person person = new Person();//创建一个新的 Person
        person.setPic(dataFilePath);
        person.setName(name);
        person.setIsDel(0);
        if (!StringUtils.isEmpty(card)) {
            person.setCard(card);
        }
        if (!StringUtils.isEmpty(address)) {
            person.setAddress(address);
        }
        if (StringUtils.isNotEmpty(nation)) {
            person.setNation(nation);
        }
        if (!StringUtils.isEmpty(sex)) {
            person.setSex(sex);
        }
        if (birthday != null && !birthday.equals("")) {
            if (DateUtils.validDate(birthday)) {
                person.setBirthday(birthday);
            }
        }
        person.setIsWarning(status);
        person.setCreateUser(userId);
        person.setUpdateUser(userId);
        person.setUpdateTime(new Date());
        person.setCreateTime(new Date());
        /*if (Constants.CUSTOMER.equals("minglue")) {
            if (!StringUtils.isEmpty(card)) {
                String responseStr = minglueService.findCard(card);
                JSONObject result = JSONObject.parseObject(responseStr);
                String codeStr = result.getString("code");
                log.info("明略查询返回数据code==========" + codeStr);
                if (codeStr.equals("0")) {
                    JSONArray peoples = result.getJSONArray("people");
                    log.info("明略查询返回数据people==========" + peoples);
                    if (peoples.size() > 0) {
                        JSONObject ob = peoples.getJSONObject(0);
                        person.setAddress(ob.getString("name"));
                        person.setNation(ob.getString("relation_info"));
                        log.info("明略查询返回数据name=========" + ob.getString("name"));
                        log.info("明略查询返回数据relation_info==========" + ob.getString("relation_info"));
                    }
                }
            }
        }*/
        Map<String, Object> param = new HashMap<>();
        param.put("version", "V1.0");
        param.put("seqnum", 1);
        param.put("from", "");
        param.put("to", "");
        param.put("type", "");
        param.put("number", "1-1-1-1");
        Map<String, Object> data = null;
        if (flag.equals("true")) {
            log.info("开始提取人脸特征值");
            data = new HashMap<>();
            data.put("type", 1);
            data.put("name", file.getOriginalFilename());
            data.put("image", FileBase64ConvertUitl.encodeBase64File(file));
            param.put("data", data);
            String jsonOkHttp = OkHttpUtils.jsonOkHttp(featureExtractUrl, param);
            log.info("提取返回"+jsonOkHttp);
            JSONObject resultJSON = JSON.parseObject(jsonOkHttp);
            if (null != resultJSON) {
                if (resultJSON.containsKey("code") && resultJSON.getInteger("code") == 0) {
                    JSONObject resultDate = resultJSON.getJSONObject("data");
                    String resultName = resultDate.getString("name");
                    if (resultName.equals(file.getOriginalFilename())) {
                        String feature = resultDate.getString("feature");
                        person.setHash(feature);
                        person.setModel("LocalModel_g26_201904");
                        log.info("特征值提取成功");
                    }
                }
            }
        }
        personMapper.insertUseGeneratedKeys(person);
        if (Constants.CUSTOMER.equals("llvision00052")) {
            llvision00052Service.putFeature(request, person, libId);
        }
        Map<String, Object> dateMap = new HashMap<>();//返回给客户端
        WareHouseFace warehouseFace = new WareHouseFace(); //创建一个 WarehouseFace
        warehouseFace.setLibId(libId);
        warehouseFace.setCreateUser(userId);
        warehouseFace.setPersonId(person.getId());
        warehouseFace.setCreateUser(userId);
        warehouseFace.setUpdateUser(userId);
        warehouseFace.setCreateTime(new Date());
        warehouseFace.setUpdateTime(new Date());
        warehouseFace.setIsDel(0);
        wareHouseFaceMapper.insert(warehouseFace);


        dateMap.put("userId", userId);
        r.setData(dateMap);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);


//        new Thread(() -> {
//            Map<String, Object> params = new HashMap<>();
//            params.put("glassType", "G25");
//            params.put("image", base64);
//            params.put("imageType", "1");
//            params.put("imageName", "");
//            params.put("seqNumber", pId);
//            params.put("callHttp", facedtrIp + "/web/v1/offline/facedtr");
//            String httpUrl = "http://120.79.25.118:8087/feature/v1/featureAcquire";
//            String result = OkHttpUtils.jsonOkHttp(httpUrl, params);
//            log.info(result);
//        }).start();
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }

    @Override
    public Result deleteFace(Integer userId, Long personId,String appId, HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();

        Person person = personBiz.getPersonByIdAndAppId(personId,appId);
        if (person == null) { // 如果实普通管理员无法访问该列表
            return Result.error(Constants.PERMISSION_CODE_ERROR, requestContext.getMessage("xxbz"));
        }
        Map<String, Object> dateMap = new HashMap<>();//返回给客户端

        if (null != person.getPic()) {
            String fileName = ArrayUtils.removeArrayEmptyTextBackNewArray(person.getPic().split("/"))[2];
            if (Constants.CUSTOMER.equals("llvision00052")) {
                llvision00052Service.deleteFeature(person.getId());
            }
        }
        personBiz.del(person);
        wareHouseFaceBiz.delByPersonId(personId);

        dateMap.put("userId", userId);
        r.setData(dateMap);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        log.info("method:{} result:{}",request.getRequestURI(),r);
        return r;
    }

    @Override
    public Result getFace(Long personId, String appId,HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();
        Person person = personBiz.getPersonByIdAndAppId(personId,appId);
        if ( person == null) { // 如果实普通管理员无法访问该列表
            return Result.error(Constants.PERMISSION_CODE_ERROR, requestContext.getMessage("xxbz"));
        }
        Map<String, Object> dateMap = new HashMap<>();//返回给客户端
        if (person != null) {
            dateMap.put("id", person.getId());
            dateMap.put("name", StringUtils.isNotEmpty(person.getName()) ? person.getName() : "");
            dateMap.put("status", person.getIsWarning());
            if (StringUtils.isNotEmpty(person.getPic())) {
                /*if (!Constants.CUSTOMER.equals("pudong")) {
                } else {
                    dateMap.put("pic", visit_url + "/" + person.getPic());
                }*/
                dateMap.put("pic", pic_url + person.getPic());
            } else {
                dateMap.put("pic", "");
            }
            dateMap.put("card", person.getCard());
            if (person.getSex() != null) {
                dateMap.put("sex", person.getSex());
            } else {
                dateMap.put("sex", 2);
            }
            dateMap.put("birthday", person.getBirthday());
            if (person.getAddress() != null && !person.getAddress().equals("")) {
                dateMap.put("address", person.getAddress());
            } else {
                dateMap.put("address", "");
            }
            dateMap.put("warning", WarningUtils.getWarningList(person.getWarningType()));
            dateMap.put("model", person.getModel());
            dateMap.put("Hash", person.getHash() != null ? requestContext.getMessage("cz") : requestContext.getMessage("bcz"));
            dateMap.put("create_time", DateUtils.dateTimeToString(person.getCreateTime()));
            dateMap.put("update_time", DateUtils.dateTimeToString(person.getUpdateTime()));
        }
        r.setData(dateMap);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        JSONObject jsonObject = (JSONObject)JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), jsonObject.toJSONString());
        return r;
    }
}
