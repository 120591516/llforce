package com.llvision.face.utils;

import com.llvision.face.constants.Constants;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.MalformedURLException;
import java.util.UUID;

/**
 * 文件操作工具类
 */
public class FileUtils {
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);
    /**
     * 创建目录和文件
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static File makefile(String path) throws IOException {

        if (path == null || "".equals(path.trim())) {
            return null;
        }
        //本地使用
        //String dirPath = path.substring(0, path.lastIndexOf("\\"));
        //服务器使用
        String dirPath = path.substring(0, path.lastIndexOf("/"));
        int index = path.lastIndexOf(".");
        if (index > 0) { // 全路径，保存文件后缀
            File dir = new File(dirPath);
            if (!dir.exists()) { //先建目录
                dir.mkdirs();
                dir = null;
            }

            File file = new File(path);
            if (!file.exists()) {//再建文件
                file.createNewFile();
            }
            return file;
        } else {
            File dir = new File(dirPath); //直接建目录
            if (!dir.exists()) {
                dir.mkdirs();
                dir = null;
            }
            return dir;
        }

    }

    /**
     * 获取文件或目录的大小
     *
     * @param file
     */
    public static long sizeOf(File file) {
        if (!file.exists()) {
            String message = file + "不存在";
            throw new IllegalArgumentException(message);
        }
        if (file.isDirectory()) {
            return sizeOfDirectory(file);
        } else {
            return file.length();
        }
    }

    /**
     * 获取目录的大小
     *
     * @param directory
     */
    public static long sizeOfDirectory(File directory) {
        if (!directory.exists()) {
            String message = directory + " 不存在";
            throw new IllegalArgumentException(message);
        }
        if (!directory.isDirectory()) {
            String message = directory + " 不是目录";
            throw new IllegalArgumentException(message);
        }
        long size = 0;
        File[] files = directory.listFiles();
        if (files == null) {  // 空文件夹
            return 0L;
        }
        for (File file : files) {
            size += sizeOf(file);
        }

        return size;
    }


    // 链接url下载图片
    public static String saveImageForUri(String uri, HttpServletRequest request) {
        String num = UUID.randomUUID().toString();
        String pathName = Constants.FACE_RECORD_PIC_URL + "/" + num + ".jpg";
        String path = request.getSession().getServletContext().getRealPath("/") + pathName;
        FileOutputStream fileOutputStream = null;
        DataInputStream dataInputStream = null;
        try {
            Request.Builder builder = new Request.Builder().url(uri);
            Call call = new OkHttpClient().newCall(builder.build());
            Response response = call.execute();
            InputStream in = response.body().byteStream();
            dataInputStream = new DataInputStream(in);

            fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
        } catch (MalformedURLException e) {
            log.info("图片无法访问");
        } catch (IOException e) {
            log.info("io异常");
        } finally {
            try {
                dataInputStream.close();
            } catch (IOException e) {
                log.info("断开连接失败");
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                log.info("断开连接失败");
            }
        }
        return pathName;
    }
}
