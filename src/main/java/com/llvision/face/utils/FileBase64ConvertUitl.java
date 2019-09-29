package com.llvision.face.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;

public class FileBase64ConvertUitl {
	/**
	 * 将文件转成base64 字符串
	 * 
	 * @param path 文件路径
	 * @return
	 * @throws Exception
	 */

	public static String encodeBase64File(String path) {
		byte[] buffer =null;
		try {

		File file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
			buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return new String(Base64.getEncoder().encode(buffer));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String encodeBase64File(MultipartFile file) {
		byte[] buffer =null;
		try {
			FileInputStream inputFile = (FileInputStream)file.getInputStream();
			buffer = new byte[(int) file.getSize()];
			inputFile.read(buffer);
			inputFile.close();
			return new String(Base64.getEncoder().encode(buffer));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**

	/**
	 * 将base64字符解码保存文件
	 * 
	 * @param base64Code
	 * @param targetPath
	 * @throws Exception
	 */

	public static void decoderBase64File(String base64Code, String targetPath) throws Exception {
		FileOutputStream out = null;
		try {
			byte[] buffer = Base64.getDecoder().decode(base64Code);
			out = new FileOutputStream(targetPath);
			out.write(buffer);
		} finally {
			out.close();
		}
	}

	/**
	 * 将base64字符保存文本文件
	 * 
	 * @throws Exception
	 */

	public static void toFile(String base64Code, String targetPath) throws Exception {
		byte[] buffer = base64Code.getBytes();
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
	}

	public static void main(String[] args) {
		try {
			String base64Code = encodeBase64File("D:/测试照片/93fb69e0-5c57-4ab5-bb28-131c006a3dd6.jpg");
			System.out.println(encodeBase64File("D:/测试照片/93fb69e0-5c57-4ab5-bb28-131c006a3dd6.jpg"));
			// System.out.println(encodeBase64File("D:/测试照片/生活照.jpg"));
			toFile(base64Code, "D:\\three.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
