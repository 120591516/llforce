package com.llvision.face.service;

import Aladdin.Hasp;
import Aladdin.HaspApiVersion;
import Aladdin.HaspStatus;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

@Service
@Transactional
@Slf4j
public class WatchDogService {
	 public static final String scope1 = new String(
      "<haspscope />\n");

    public static final String view = new String(
    		"<haspformat root=\"hasp_info\">"+
    		"    <feature>"+
    		"       <attribute name=\"id\" />"+
    		"       <attribute name=\"locked\" />"+
    		"       <attribute name=\"expired\" />"+
    		"       <attribute name=\"disabled\" />"+
    		"       <attribute name=\"usable\" />"+
    		"    </feature>"+
    		"</haspformat>");

	 @Value("${hasp_default_fid}")
	 private long hasp_default_fid;
	 @Value("${vendorCode}")
	 private String vendorCode;
	 @Value("${path.resourcePath}")
	 private String resourcePath;
//	private long hasp_default_fid = 2000;
//	private String vendorCode = "4iBFM5hHMAVdRceVh7ws+43LkY56N5Lbz7zaalDxt7MPx7fDxD8o3d067/fjONUeB9JdIfiptuWBUOebY3fbS4XeHh5T98IOmUIqJ+TPJGlILHpXApzwknvTAoGWCEKnZdHJUlY/GeUSUctvgauLHjGEeAxxJ1hy5fWXVeEO1pOC/7akRHP/IX5qJTmkEnzqMTQXhircVuGS2fg8Ortr8sTRpSS5lC9P73wZ1MyD07v7Gxnqj8jILNvxfNfATKGHe0yer3MOLHUVy9ON44LxSaC9yCJnqDLELoasJnsQ3HTuNo1QV2L9Tr0izsxGtncsQPTtmOY5kmwoqRIjymsQtVGlARBelh/xHMrpjbxy3+ykE9h75No3xG21DzB/TIClFNfzcJmQJgbiCfTTfbfu+eNx6JGTcYMMINnANmjlGfZ+Y5amyJCxY6PLMmVX7Xv4DHrp9/7wDOlzOCtZ21mFSlzm5NZaotEBc6Z+/r7iXF34Wg408YpuRsFGzGlmtPrR5ywTSiQ2Ki8e2z6kXkfErt/yc8gBVf1An3icaD6P15211qdF+OMPJZMCVQgahEwGc3IVREcoiWXNKIm4u9bNRssDzfumWLSzLaNxHCBTrESP15YOC+V8lrp6xXM0U6fsIROpBFk0CEuIjDD40e9hAI9kxTNGLgaimLFT2z9Y+WyzjBRyIBtl8OeF6fGZsQD3Z1dma3+0QcZpx6QLf487EVMZMoNWrweN6BbuZyAuyjcNfGGyxoIVTZWAWV3WJ0k+DK1ngHC6H2Ycujltiuic8k14+naOzu/ixXSSrc+yHvMBBhlkxlY13VjZxLfSAnXsCJd+YtUHKlhzJ/GbK4oUTOJe5TwjtQjM7ujSqfEIDna6GDdIM3mD6zD2R/pO72sQk8BorsgRCc8s0gkFZYZdr7z9yXnmrQw4umsNwytavk3pxoR0HCgqPDH3znkyVGNzGVHAhieQo60ItaIGoRAtpg==";
	private static Hasp hasp = null;
	private String infos;
	private int status;

	public boolean login() {
		boolean flag = false;
		int status;
		hasp = new Hasp(hasp_default_fid);
		HaspApiVersion version = hasp.getVersion(vendorCode);
		status = version.getLastError();

		switch (status) {
		case HaspStatus.HASP_STATUS_OK:
			break;
		case HaspStatus.HASP_NO_API_DYLIB:
			log.info("Sentinel API dynamic library not found");
			break;
		case HaspStatus.HASP_INV_API_DYLIB:
			log.info("Sentinel API dynamic library is corrupt");
			break;
		default:
			log.info("unexpected error");
		}


		/**********************************************************************
		 * hasp_login establish a context for Sentinel services
		 */

		hasp.login(vendorCode);
		status = hasp.getLastError();

		switch (status) {
		case HaspStatus.HASP_STATUS_OK:
			flag = true;
			break;
		case HaspStatus.HASP_FEATURE_NOT_FOUND:
			log.info("no Sentinel DEMOMA key found");
			return false;
		case HaspStatus.HASP_HASP_NOT_FOUND:
			log.info("Sentinel key not found");
			return false;
		case HaspStatus.HASP_OLD_DRIVER:
			log.info("outdated driver version or no driver installed");
			return false;
		case HaspStatus.HASP_NO_DRIVER:
			log.info("Sentinel key not found");
			return false;
		case HaspStatus.HASP_INV_VCODE:
			log.info("invalid vendor code");
			return false;
		default:
			log.info("login to default feature failed");
			return false;
		}
		if (status != HaspStatus.HASP_STATUS_OK) {
			flag = false;
		} else {
			flag = true;
		}
		log.info("success!");
		return flag;
	}

	public boolean getInfo() {
		boolean flag = false;
		hasp = new Hasp(hasp_default_fid);
		infos = hasp.getInfo(scope1, view, vendorCode);
		status = hasp.getLastError();
		flag = readStringXmlOut(infos);
		return flag;
	}

	public Boolean readStringXmlOut(String xml) {
		Boolean flag = false;
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);
			Element rootElt = doc.getRootElement();
			Iterator iter = rootElt.elementIterator("feature");
			while (iter.hasNext()) {
				Element recordEle = (Element) iter.next();
				String id = recordEle.attributeValue("id");
				if (id.equals(hasp_default_fid + "")) {
					String expired = recordEle.attributeValue("expired");
					log.info("expired" + expired);
					String usable = recordEle.attributeValue("usable");
					log.info("usable" + usable);
					if (expired.equals("false") && usable.equals("true")) {
						flag = true;
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	public boolean encryptFile(String filePath){
		boolean flag = false;
		byte[] buffer =null;
		try {
			File file = new File(filePath);
			FileInputStream inputFile = new FileInputStream(file);
			buffer = new byte[(int) file.length()];
			inputFile.read(buffer);
			inputFile.close();
			flag = hasp.encrypt(buffer);
			FileOutputStream out = new FileOutputStream("D:/encrypt.txt");
			out.write(buffer);
			out.close();
		}catch(Exception e){
			flag = false;
		}
		return  flag;
	}
	/**
	 *
	 * @param keyWord  字段名称
	 * @return
	 */
	public Object decryptFile(String keyWord){
		Object result = null;
		login();
		byte[] buffer =null;
		try {
			File file = ResourceUtils.getFile(resourcePath+"encrypt.txt");
			FileInputStream inputFile = new FileInputStream(file);
			buffer = new byte[(int) file.length()];
			inputFile.read(buffer);
			inputFile.close();
			hasp.decrypt(buffer);
			String s = new String(buffer);
			JSONObject str = JSONObject.parseObject(s);
			if(str.getJSONObject("data").containsKey(keyWord)){
				result = str.getJSONObject("data").get(keyWord);
			}
			hasp.logout();
		}catch(Exception e){
			e.printStackTrace();
		}
		return  result;
	}
	/**
	 *
	 * @param filePath 文件位置
	 * @param keyWord  字段名称
	 * @return
	 */
	public boolean decryptFile(String filePath,String keyWord){
		boolean flag = false;
		byte[] buffer =null;
		try {
			File file = new File(filePath);
			FileInputStream inputFile = new FileInputStream(file);
			buffer = new byte[(int) file.length()];
			inputFile.read(buffer);
			inputFile.close();
			flag = hasp.decrypt(buffer);
			String s = new String(buffer);
			System.out.println(s);
			JSONObject str = JSONObject.parseObject(s);
			System.out.println(keyWord+":>>>>"+str.getJSONObject("data").get(keyWord));
			hasp.logout();
		}catch(Exception e){
			flag = false;
		}
		return  flag;
	}

	/**
	 *
	 * @return JSONObject
	 */
	public JSONObject decryptFile(){
		JSONObject result = new JSONObject();
		login();
		byte[] buffer =null;
		try {
			File file = ResourceUtils.getFile(resourcePath+"encrypt.txt");
			FileInputStream inputFile = new FileInputStream(file);
			buffer = new byte[(int) file.length()];
			inputFile.read(buffer);
			inputFile.close();
			hasp.decrypt(buffer);
			String s = new String(buffer);
//			log.info("decryptFile s:{}",s);
			result = JSONObject.parseObject(s).getJSONObject("data");
			hasp.logout();
		}catch(Exception e){
			log.error("decryptFile error",e);
		}
		return  result;
	}

	public static void main(String[] args) {
		WatchDogService dogTest = new WatchDogService();
		System.out.println(dogTest.login());
		System.out.println(dogTest.encryptFile("D:/demo2.txt"));
		System.out.println(dogTest.decryptFile("D:/encrypt.txt","compareMode"));
		System.out.println(dogTest.decryptFile("D:/encrypt.txt","collectMode"));
	}
}
