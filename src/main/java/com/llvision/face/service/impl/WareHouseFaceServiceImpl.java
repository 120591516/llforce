package com.llvision.face.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.llvision.face.config.SftpConfig;
import com.llvision.face.constants.Constants;
import com.llvision.face.dao.biz.OfflineFaceBiz;
import com.llvision.face.dao.biz.PersonBiz;
import com.llvision.face.dao.mapper.PersonMapper;
import com.llvision.face.dao.mapper.WareHouseFaceMapper;
import com.llvision.face.entity.OffLineFace;
import com.llvision.face.entity.Person;
import com.llvision.face.entity.WareHouseFace;
import com.llvision.face.response.OfflineFacePojo;
import com.llvision.face.service.IOffLineFaceService;
import com.llvision.face.service.IWareHouseFaceService;
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
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: guoyc
 * @Date: 2019/5/14 15:54
 * @Version 1.0
 */
@Service
@Transactional
@Slf4j
public class WareHouseFaceServiceImpl implements IWareHouseFaceService {

    @Resource
    private OfflineFaceBiz offlineFaceBiz;

    @Resource
    private PersonBiz personBiz;

    @Resource
    private Llvision00052Service llvision00052Service;

    @Resource
    private PersonMapper personMapper;

    @Resource
    private WareHouseFaceMapper wareHouseFaceMapper;

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
    private IOffLineFaceService offLineFaceService;

    @Resource
    private SftpConfig sftpConfig;

    @Override
    public Result warehouseList(Integer userId, Integer pageNum, Integer pageSize, Integer type, String appId, HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();

        Map<String,Integer> page = PageUtil.getPageInfo(pageSize + "", pageNum + "");
        PageHelper.startPage(page.get("pageIndex"), page.get("pageSize"));
        List<OffLineFace> offLineFaceList = offlineFaceBiz.selectOfflineFaces(userId,type,appId);
        PageInfo pageInfo = new PageInfo(offLineFaceList);
        Map<String, Object> dateMap = new HashMap<>();//返回给客户端
        List<OfflineFacePojo> offlineFacePojoList = new ArrayList<>();//新建一个返回结构List
        for (OffLineFace a : offLineFaceList) {
            OfflineFacePojo offlineFacePojo = new OfflineFacePojo();
            offlineFacePojo.setId(a.getId());
            offlineFacePojo.setName(a.getName());
            offlineFacePojo.setStatus(a.getStatus());
            offlineFacePojo.setCreate_time(DateUtils.dateTimeToString(a.getCreateTime()));
            offlineFacePojo.setUpdate_time(DateUtils.dateTimeToString(a.getUpdateTime()));
            offlineFacePojo.setTime1(DateUtils.dateToDateStr(a.getCreateTime()));
            offlineFacePojo.setTime2(DateUtils.dateToTimeStr(a.getCreateTime()));
            offlineFacePojo.setUpdate_time1(DateUtils.dateToDateStr(a.getUpdateTime()));
            offlineFacePojo.setUpdate_time2(DateUtils.dateToTimeStr(a.getUpdateTime()));
            offlineFacePojoList.add(offlineFacePojo);
        }

        dateMap.put("warehouseList", offlineFacePojoList);
        dateMap.put("pageTotal", pageInfo.getPageSize());
        dateMap.put("pageNum", pageInfo.getPageNum());
        dateMap.put("total", pageInfo.getTotal());
        dateMap.put("userId", userId);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        r.setData(dateMap);
        JSONObject jsonObject = (JSONObject)JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), jsonObject.toJSONString());
        return r;
    }

    @Override
    public Result uploadFacePic(Integer userId, Integer type, Integer libId, String appId, MultipartFile[] files, HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();

        OffLineFace offlineFace = offlineFaceBiz.getOfflineFaceValidByIdAndAppId(libId,appId);
        if (offlineFace == null) { // 如果实普通管理员无法访问该列表
            return Result.error(Constants.PERMISSION_CODE_ERROR, requestContext.getMessage("xxbz"));
        }
        int errNum = 0;
        String uuId = UUID.randomUUID().toString().replaceAll("-", "");
        Person person;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Map<String, Object> param = new HashMap<>();
        param.put("version", "V1.0");
        param.put("seqnum", 1);
        param.put("from", "");
        param.put("to", "");
        param.put("type", "");
        param.put("number", "1-1-1-1");
        Map<String, Object> data = null;

        // 查询人像库是否超限
        if (!offLineFaceService.checkFaceCount(appId,files.length - 1 )) {
            return Result.error(Constants.PERMISSION_CODE_ERROR, requestContext.getMessage("rxkydsx"));
        }

        SFTPUtil sftpUtil = new SFTPUtil(sftpConfig.getHost(),sftpConfig.getUsername(),sftpConfig.getPassword(),sftpConfig.getPort());
        sftpUtil.connect();
        for (MultipartFile file : files) {
            String filePath;
            String dataFilePath = "";
            // 转存文件
            try {
                String prefix = file.getOriginalFilename().trim().substring(0, file.getOriginalFilename().indexOf("."));
                String suffix = file.getOriginalFilename().trim().substring(file.getOriginalFilename().indexOf("."));
                if (suffix.endsWith("jpg") || suffix.endsWith("JPG") || suffix.endsWith("png") || suffix.endsWith("PNG")) {
                    String num = UUID.randomUUID().toString();
                    // 服务器存储文件路径
                    String pathName = Constants.UPLOAD_FACE_URL + num + suffix;
                    sftpUtil.upload(Constants.UPLOAD_FACE_URL,num + suffix,file.getInputStream());
                    // 文件保存路径
                    /*if (Constants.CUSTOMER.equals("pudong")) {
                        filePath = file_path + "/" + pathName;
                    } else {
                    }*/
//                    filePath = request.getSession().getServletContext().getRealPath("/") + pathName;
                    filePath = pic_url + pathName;
                    /*FileUtils.makefile(filePath);
                    file.transferTo(new File(filePath));*/
                    // 数据库存放的路径
                    dataFilePath = pathName;
                    // 判断type 类型
                    person = new Person();
                    if (type == 1) {
                        person.setName(prefix);
                    }
                    if (type == 2) {
                        person.setCard(prefix);
                    }
                    List<Person> personList = personBiz.getPersonByPersonAndWarehouseId(person, libId);
                    if (personList.size() > 0) {
                        person = personList.get(0);
                    }
                    person.setPic(dataFilePath);
                    if (null != person.getId()) {
                        person.setUpdateTime(new Date());
                        personMapper.updateByPrimaryKeySelective(person);
                    } else {
                        person.setCreateTime(new Date());
                        person.setUpdateTime(new Date());
                        person.setCreateUser(userId);
                        person.setUpdateUser(userId);
                        person.setIsWarning(0);
                        person.setIsDel(0);
                        person.setSex(2);
                        if (flag.equals("true")) {
                            log.info("开始提取人脸特征值");
                            data = new HashMap<>();
                            data.put("type", 1);
                            data.put("name", file.getOriginalFilename());
                            data.put("image", FileBase64ConvertUitl.encodeBase64File(file));
                            param.put("data", data);
                            log.info("请求参数："+JSON.toJSONString(param));
                            String jsonOkHttp = OkHttpUtils.jsonOkHttp(featureExtractUrl, param);
                            JSONObject resultJSON = JSON.parseObject(jsonOkHttp);
                            if (null != resultJSON) {
                                if (resultJSON.containsKey("code") && resultJSON.getInteger("code") == 0) {
                                    JSONObject resultDate = resultJSON.getJSONObject("data");
                                    String resultName = resultDate.getString("name");
                                    if (resultName.equals(file.getOriginalFilename())) {
                                        String feature = resultDate.getString("feature");
                                        log.info("feature"+feature);
                                        person.setHash(feature);
                                        person.setModel("LocalModel_g26_201904");
                                    }
                                }
                            }
                        }
                        personMapper.insertUseGeneratedKeys(person);
                        if (Constants.CUSTOMER.equals("llvision00052")) {
                            llvision00052Service.putFeature(request, person, libId);
                        }
                        WareHouseFace warehouseFace = new WareHouseFace(); // 创建一个
                        warehouseFace.setLibId(libId);
                        warehouseFace.setPersonId(person.getId());
                        warehouseFace.setCreateUser(userId);
                        warehouseFace.setUpdateUser(userId);
                        warehouseFace.setUpdateTime(new Date());
                        warehouseFace.setCreateTime(new Date());
                        warehouseFace.setIsDel(0);
                        wareHouseFaceMapper.insert(warehouseFace);
                    }
                } else {
                    /*errNum += 1;
                    UploadError uploadError = new UploadError();
                    uploadError.setKey(file.getOriginalFilename().trim());
                    uploadError.setGroup(uuId);
                    uploadError.setDoTime(new Date());
                    uploadErrorService.insert(uploadError);*/

                }
            } catch (Exception e) {
                // 上传失败
                /*errNum += 1;
                UploadError uploadError = new UploadError();
                uploadError.setKey(file.getOriginalFilename().trim());
                uploadError.setGroup(uuId);
                uploadError.setDoTime(new Date());
                uploadErrorService.insert(uploadError);*/
            }
        }
        sftpUtil.disconnect();
        executorService.shutdown();
        Map<String, Object> dateMap = new HashMap<>();// 返回给客户端
        dateMap.put("userId", userId);
        dateMap.put("errNum", errNum);
        dateMap.put("group", uuId);
        r.setData(dateMap);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }

    @Override
    public Result uploadExcel(Integer userId, MultipartFile file, Integer libId, String appId, HttpServletRequest request) throws IOException {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();

        // 判断文件后缀
        if (!file.getOriginalFilename().endsWith("xlsx") && !file.getOriginalFilename().endsWith("xls")) {
            return Result.error(Constants.OTHER_ERROR_CODE_ERRROR, requestContext.getMessage("wjgsbzq"));
        }
        int keyField = ExcelUtil.getKeyField(file.getInputStream(), file.getOriginalFilename());
        if (keyField == 0) {
            // excel 的重点字段不正确，或者标题头不对，文件格式不正确
            return Result.error(Constants.OTHER_ERROR_CODE_ERRROR, requestContext.getMessage("mbxxbpp"));
        }

        OffLineFace offlineFace = offlineFaceBiz.getOfflineFaceValidByIdAndAppId(libId,appId);
        if (offlineFace == null) { // 如果实普通管理员无法访问该列表
            return Result.error(Constants.PERMISSION_CODE_ERROR, requestContext.getMessage("xxbz"));
        }

        ArrayList<Person> readExcel = ExcelUtil.readExcel(file.getInputStream(), file.getOriginalFilename(), keyField);

        // 判断person在数据库中有没有 有的话update 没有insert
        Person query;
        if (null != readExcel && readExcel.size() > 0) {
            // 查询人像库是否超限
            if (!offLineFaceService.checkFaceCount(appId,readExcel.size() - 1)) {
                return Result.error(Constants.PERMISSION_CODE_ERROR, requestContext.getMessage("rxkydsx"));
            }
            for (Person person : readExcel) {
                query = new Person();
                // 如果keyField 为1 说明是姓名不能为空
                // 如果keyField 为2 说明是身份证号不能为空
                if (keyField == 1) {
                    if (StringUtils.isEmpty(person.getName())) {
                        return Result.error(Constants.OTHER_ERROR_CODE_ERRROR, requestContext.getMessage("excelxxbq"));
                    }
                    query.setName(person.getName());
                }
                if (keyField == 2) {
                    if (StringUtils.isEmpty(person.getCard())) {
                        return Result.error(Constants.OTHER_ERROR_CODE_ERRROR, requestContext.getMessage("excelxxbq"));
                    }
                    query.setCard(person.getCard());
                }
                List<Person> result = personBiz.getPersonByPersonAndWarehouseId(query, libId);
                if (result.size() > 0) {
                    person.setId(result.get(0).getId());
                    person.setUpdateTime(new Date());
                    personMapper.updateByPrimaryKeySelective(person);
                } else {
                    person.setCreateTime(new Date());
                    person.setCreateUser(userId);
                    person.setUpdateUser(userId);
                    person.setUpdateTime(new Date());
                    person.setIsDel(0);
                    personMapper.insertUseGeneratedKeys(person);
                    WareHouseFace warehouseFace = new WareHouseFace(); // 创建一个
                    warehouseFace.setLibId(libId);
                    warehouseFace.setPersonId(person.getId());
                    warehouseFace.setCreateUser(userId);
                    warehouseFace.setUpdateUser(userId);
                    warehouseFace.setCreateTime(new Date());
                    warehouseFace.setUpdateTime(new Date());
                    warehouseFace.setIsDel(0);
                    wareHouseFaceMapper.insert(warehouseFace);
                }
            }
        } else {
            return Result.error(Constants.OTHER_ERROR_CODE_ERRROR, requestContext.getMessage("excelxxbq"));
        }
        Map<String, Object> dateMap = new HashMap<>();// 返回给客户端
        dateMap.put("userId", userId);
        r.setData(dateMap);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }
}
