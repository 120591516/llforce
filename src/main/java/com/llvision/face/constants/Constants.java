package com.llvision.face.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class Constants {

    /**
     * 日志id
     */
    public static final String TRACE_LOG_ID = "TRACE_LOG_ID";
    /**
     * 方法开始时间(用来计算方法执行时间)
     */
    public static final String METHOD_START_TIME = "METHOD_START_TIME";

    public static String CUSTOMER;
    @Value("${customer}")
    public void setCUSTOMER(String CUSTOMER) {
        Constants.CUSTOMER = CUSTOMER;
    }

    private static String PIC_ADDRESS;

    @Value("${pic_url}")
    public static void setPIC_ADDRESS(String picAddress) {
        Constants.PIC_ADDRESS = picAddress;
    }

    public static final String DEFAULT_IMAGE = "/upload/record/face/frist_homepage.jpg";
    public static final String WARNING_IMAGE = "/upload/record/face/batchWarning.jpg";
    public static final String HUAWEICLOUDIMAGE = "/upload/huawei/person.png";

    //超时时间
	public static final Integer WEB_OUT_TIME = 30000000; // token超时时间
    public static final Integer APP_OUT_TIME = 30000000; // token超时时间

    public static final String PERMISSION_FAIL = "权限不足";
    public static final String REQUEST_FAIL = "请求失败";  //
    public static final String REQUEST_SUCCEED = "请求成功";
    public static final String LOGIN_SUCCEED = "登录成功";  //用户登录成功信息
    public static final String TOKEN_INVALID = "请求失败,token无效";  //token失效信息
    public static final int LOGIN_INVALID_CODE_ERRROR = -2;  //登录失败coed
    public static final int TOKEN_INVALID_CODE_ERRROR = -1;  //token失效代码
    public static final int OTHER_ERROR_CODE_ERRROR = -3;  //其他错误
    public static final int PERMISSION_CODE_ERROR = -4; //权限错误
    public static final String REQUEST_FAIL_JURISDICTION_NOT_ENOUGH = "权限不足,无法删除此记录";  //

    //车牌底色常量代号
    public static final int CAR_PLATE_COLOR_BLUE = 1;  //蓝色
    public static final int CAR_PLATE_COLOR_YELLOW = 2;  //黄色
    public static final int CAR_PLATE_COLOR_RED = 3;  //红色
    public static final int CAR_PLATE_COLOR_BLACK = 4;  //黑色
    public static final int CAR_PLATE_COLOR_GREEN = 5;  //绿色
	public static final int CAR_PLATE_COLOR_WHITE = 6; // 白色

    //人员警示
    public static final int PERSON_WARNING_ZTRY = 1;  //在逃人员
    public static final int PERSON_WARNING_ZDRY = 2;  //重点人员
    public static final int PERSON_WARNING_XDRY = 3;  //吸毒人员
    public static final int PERSON_WARNING_KBFZ = 4;  //恐暴分子
    public static final int PERSON_WARNING_SDRY = 5;  //涉毒人员
    public static final int PERSON_WARNING_SZPRY = 6;  //涉诈骗人员
    public static final int PERSON_WARNING_XSFZ = 7;  //重点刑事犯罪前科人员
    public static final int PERSON_WARNING_SWRY = 8;  //涉稳人员
    public static final int PERSON_WARNING_SKRY = 9;  //涉恐人员
    public static final int PERSON_WARNING_ZDSFRY = 10;  //重点上访人员
    public static final int PERSON_WARNING_ZSZHJSBR = 11;  //肇事肇祸精神病人

    //洛阳yitu数据库警示人员类别
    public static final int PERSON_WARNING_LY_XF = 12;  //信访人员
    public static final int PERSON_WARNING_LY_JNZD = 13;  //局内重点人员
    public static final int PERSON_WARNING_LY_WFQK = 14;  //违法前科人员
    public static final int PERSON_WARNING_LY_WWB = 15;  //维稳办人员
    public static final int PERSON_WARNING_LY_BKJL = 16;  //布控精灵
	public static final int PERSON_WARNING_LY_HZZD = 17; // 户政重点
	public static final int PERSON_WARNING_LY_QLZD = 18; // 七类重点
    //人员警示展示
    public static final String PERSON_WARNING_STRING_ZTRY = "在逃人员";  //在逃人员
    public static final String PERSON_WARNING_STRING_ZDRY = "重点人员";  //重点人员
    public static final String PERSON_WARNING_STRING_XDRY = "吸毒人员";  //吸毒人员
    public static final String PERSON_WARNING_STRING_KBFZ = "恐暴分子";  //恐暴分子
    public static final String PERSON_WARNING_STRING_SDRY = "涉毒人员";  //涉毒人员
    public static final String PERSON_WARNING_STRING_SZPRY = "涉诈骗人员";  //涉诈骗人员
    public static final String PERSON_WARNING_STRING_XSFZ = "重点刑事犯罪前科人员";  //重点刑事犯罪前科人员
    public static final String PERSON_WARNING_STRING_SWRY = "涉稳人员";  //涉稳人员
    public static final String PERSON_WARNING_STRING_SKRY = "涉恐人员";  //涉恐人员
    public static final String PERSON_WARNING_STRING_ZDSFRY = "重点上访人员";  //重点上访人员
    public static final String PERSON_WARNING_STRING_ZSZHJSBR = "肇事肇祸精神病人";  //肇事肇祸精神病人

    //车牌警示
    public static final int CAR_WARNING_TP = 1;  //套牌
	public static final int CAR_WARNING_WZ = 2; // 违章
    public static final int CAR_WARNING_DQCL = 3;  //盗抢车辆
    public static final int CAR_WARNING_XCCF = 4;  //现场处罚
    public static final int CAR_WARNING_FXCCF = 5;  //非现场处罚
    public static final int CAR_WARNING_JYXXGQ = 6;  //检验信息过期
    public static final int CAR_WARNING_BKJL = 7;  //布控精灵
	public static final int CAR_WARNING_WFXX = 8; // 违法信息
	public static final int CAR_WARNING_FYY = 9; // 非预约车辆
	public static final int CAR_WARNING_HMD = 10; // 黑名单车辆
	public static final int CAR_WARNING_XMBD = 11; // 校门不对
	// 广东定制车牌警示
	public static final int GD_CAR_WARNING_DZWF = 1; // 多宗违法1
	public static final int GD_CAR_WARNING_DZWF2 = 2; // 多宗违法2
	public static final int GD_CAR_WARNING_WNJ = 3; // 未年检
	public static final int GD_CAR_WARNING_WBF = 4; // 未报废
	public static final int GD_CAR_WARNING_YCZT = 5; // 异常状态
	public static final int GD_CAR_WARNING_YZF = 6; // 营转非1
	public static final int GD_CAR_WARNING_YZF2 = 7; // 营转非2
	public static final int GD_CAR_WARNING_ZDWF3 = 8; // 多宗违法3
	public static final int GD_CAR_WARNING_JCP = 9; // 假牌车
	public static final int GD_CAR_WARNING_TPC = 10; // 套牌车
	public static final int GD_CAR_WARNING_JTPC = 11; // 假套牌车
	public static final int GD_CAR_WARNING_HBC = 12; // 黄标车
	public static final int GD_CAR_WARNING_ZDXYCL1 = 13; // 重点嫌疑车辆1
	public static final int GD_CAR_WARNING_ZDXYCL = 14; // 重点嫌疑车辆
	public static final int GD_CAR_WARNING_SJRY = 15; // 失驾人员
	public static final int GD_CAR_WARNING_YQWNJ = 16; // 逾期未年检

    //人员警示展示
    public static final String CAR_WARNING_STRING_TP = "套牌";  //套牌
    public static final String CAR_WARNING_STRING_WZ = "违章";  //违章
    public static final String CAR_WARNING_STRING_DQCL = "盗抢车辆";  //盗抢车辆
    public static final String CAR_WARNING_STRING_XCCF = "现场处罚";  //现场处罚
    public static final String CAR_WARNING_STRING_FXCCF = "非现场处罚";  //非现场处罚
    public static final String CAR_WARNING_STRING_JYXXGQ = "检验信息过期";  //检验信息过期
    public static final String CAR_WARNING_STRING_BKJL = "布控精灵";  //布控精灵

	// 车辆警示信息
	public static final String GD_CAR_WARNING_STRING_DZWF = "多宗违法1"; // 多宗违法1
	public static final String GD_CAR_WARNING_STRING_DZWF2 = "多宗违法2"; // 多宗违法2
	public static final String GD_CAR_WARNING_STRING_WNJ = "未年检"; // 未年检
	public static final String GD_CAR_WARNING_STRING_WBF = "未报废"; // 未报废
	public static final String GD_CAR_WARNING_STRING_YCZT = "异常状态"; // 异常状态
	public static final String GD_CAR_WARNING_STRING_YZF = "营转非1"; // 营转非1
	public static final String GD_CAR_WARNING_STRING_YZF2 = "营转非2"; // 营转非2
	public static final String GD_CAR_WARNING_STRING_ZDWF3 = "多宗违法3"; // 多宗违法3
	public static final String GD_CAR_WARNING_STRING_JCP = "假牌车"; // 假牌车
	public static final String GD_CAR_WARNING_STRING_TPC = "套牌车"; // 套牌车
	public static final String GD_CAR_WARNING_STRING_JTPC = "假套牌车"; // 假套牌车
	public static final String GD_CAR_WARNING_STRING_HBC = "黄标车"; // 黄标车
	public static final String GD_CAR_WARNING_STRING_ZDXYCL1 = "重点嫌疑车辆1"; // 重点嫌疑车辆1
	public static final String GD_CAR_WARNING_STRING_ZDXYCL = "重点嫌疑车辆"; // 重点嫌疑车辆
	public static final String GD_CAR_WARNING_STRING_SJRY = "失驾人员"; // 失驾人员
	public static final String GD_CAR_WARNING_STRING_YQWNJ = "逾期未年检"; // 逾期未年检

    //系统配置 system_conf
    public static final String SHARPNESS = "sharpness";     //清晰度 or 帧率
    public static final String ANGLE = "angle";             //角度
    public static final String OFFLINE_FACE_POWER = "offline_face_power";     //离线人像库开关
    public static final String SIMILARITY = "similarity";     //识别相似度阈值
    public static final String OFFLINE_CAR_POWER = "offline_car_power";     //离线车牌库开关
    public static final String VIDERO_RECORD_POWER = "video_record";     //视频识别开关
    public static final String ALARM_THRESHOLD ="alarm_threshold";  //告警阈值
    public static final String IS_CAR_CONFIG ="is_car_config";  //是否需要车牌配置
    public static final String IS_VIDEO_RECORDING="is_video_recording";//是否需要录制视频
	public static final String ALARM_MODE = "alarm_mode";// 报警模式 1 黑名单 0 白名单
	public static final String FACE_MODE = "face_mode";// 抓脸模式
	public static final String MAP_MODE = "map_mode";// 地图模式
	public static final String RECORD_MODE = "record_mode";// 识别模式
	public static final String VERSION_CODE = "version_code";// 版本号
	public static final String VERSION_TIME = "version_time";// 版本时间
    // 系统配置默认值
	public static final String SHARPNESS_VALUE = "35"; // 清晰度 or 帧率
    public static final String ANGLE_VALUE = "35";             //角度
    public static final String OFFLINE_FACE_POWER_VALUE = "1";     //离线人像库开关
    public static final String SIMILARITY_VALUE = "80";     //识别相似度阈值
	public static final String OFFLINE_CAR_POWER_VALUE = "1"; // 离线车牌库开关
    public static final String VIDERO_RECORD_POWER_VALUE = "0";     //离线人像库开关
    public static final String ALARM_THRESHOLD_VALUE ="80";  //告警阈值
    public static final String IS_CAR_CONFIG_VALUE ="1";  //是否需要车牌配置
    public static final String IS_VIDEO_RECORDING_VALUE="0";//是否需要录制视频
	public static final String ALARM_MODE_VALUE = "1";// 报警模式
	public static final String RECORD_MODE_VALUE = "1";// 识别模式 1、离/在线识别 0、在线识别
	public static final String VERSION_CODE_VALUE = "1.6.2";// 版本号
	public static final String VERSION_TIME_VALUE = "2019-04-09";// 版本时间

    //系统配置关键字
    // Linux 版
    public static final String UPLOAD_APK_URL = "/upload/apk/"; //上传apk路径
    public static final String UPLOAD_FACE_URL = "/upload/face/"; //上传人像路径
    public static final String UPLOAD_VIDEO_URL = "/upload/video/"; // 上传人像路径
    public static final String FACE_RECORD_PIC_URL = "/upload/record/face"; //人像识别照片存储路径
	public static final String FACE_RECORD_COMPIC_URL = "/upload/record/comPic"; // 人像识别照片存储路径
    public static final String CAR_RECORD_PIC_URL = "/upload/record/car"; //车辆识别照片存储路径
	public static final String UPLOAD_ZIP_URL = "/upload/zip/"; // 车辆识别照片存储路径
	public static final String UPLOAD_FILE_URL = "/upload/file/";

    //测试服务器
//    public static final String SERVER_URL = "http://139.198.12.170:35018";
    //正式服务器
    //public static final String SERVER_URL = "http://glxssforce.llvision.com";
    //燕窝服务器
    //public static final String SERVER_URL = "http://172.16.62.10:8081";

    public static String getIpAddress(HttpServletRequest request) {
        String url = "http://" + request.getServerName() // 服务器地址
                + ":" + request.getServerPort();// 端口号
//        String suffix = request.getRequestURI();
//        if (CUSTOMER.equals("henan") && suffix.startsWith("/app")) {
//            url = "http://192.168.20.100:6090";
//        }
//        if (CUSTOMER.equals("haikangnc") && suffix.startsWith("/app")) {
//        url = "http://172.17.10.216:8080";
//        }
		// return "http://glxssforce.llvision.com";
		return PIC_ADDRESS;
    }


}
