package com.llvision.face.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.llvision.face.constants.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Administrator on 2018/1/4.
 */

@Slf4j
public class SFTPUtil {
    private static final String TAG = "SFTPUtil";
    public static int SUCC = 0;
    public static int FAIL = -1;
    private String host;
    private String username;
    private String password;
    private int port;
    private ChannelSftp sftp = null;
    private Session sshSession = null;

    public SFTPUtil(String host, String username, String password, int port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        log.info("{}: host:{} name:{} passwd:{}  port:{}.",TAG,host,username,password,port);
    }

    //连接sftp服务器
    public ChannelSftp connect() {
        try {
            JSch jsch = new JSch();
            sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(config);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            if (channel != null) {
                channel.connect();
            } else {
                log.error("{} channel connecting failed.",TAG);
            }
            sftp = (ChannelSftp) channel;
            log.error("{}  Connected to {}",TAG,host);
            return sftp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 断开服务器
     */
    public void disconnect() {
        if (this.sftp != null) {
            if (this.sftp.isConnected()) {
                this.sftp.disconnect();
                log.info("{} sftp is closed already",TAG);
            }
        }
        if (this.sshSession != null) {
            if (this.sshSession.isConnected()) {
                this.sshSession.disconnect();
                log.info("{} sshSession is closed already",TAG);
            }
        }
    }

    //下载文件
    public static int download(ChannelSftp sftp, String directory, String downloadFile, String saveFile) {
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            sftp.get(downloadFile, new FileOutputStream(file));
            log.info("{} Download succ.",TAG,downloadFile);
            return SUCC;
        } catch (Exception e) {
            e.printStackTrace();
            return FAIL;
        }
    }

    public boolean createDir(String createPath) {
		try {
			if (isDirExist(createPath)) {
				sftp.cd(createPath);
				return true;
			}
			String pathArry[] = createPath.split("/");
			StringBuffer filePath = new StringBuffer("/");
			for (String path : pathArry) {
				if (path.equals("")) {
					continue;
				}
				filePath.append(path + "/");
				if (isDirExist(filePath.toString())) {
					sftp.cd(filePath.toString());
				} else {
					// 建立目录
					sftp.mkdir(filePath.toString());
					// 进入并设置为当前目录
					sftp.cd(filePath.toString());
				}
			}
		} catch (SftpException e) {
		}
        return false;
    }

    /**
     * 判断目录是否存在
     *
     * @param directory
     * @return
     */
    public boolean isDirExist(String directory) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
			if (null == e.getMessage() || e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            log.info("deleteFile not exists:{}",filePath);
            return false;
        }
        if (!file.isFile()) {
            log.info("deleteFile not file:{}",filePath);
            return false;
        }
        return file.delete();
    }

    //删除文件
    public static int delete(ChannelSftp sftp, String directory, String deleteFile) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
            return SUCC;
        } catch (Exception e) {
            e.printStackTrace();
            return FAIL;
        }
    }

    //列出目录下的文件
    public static Vector<?> listFiles(ChannelSftp sftp, String directory) throws SftpException {
        //Vector容器内部保存的是LsEntry类型对象。
        return sftp.ls(directory);
    }
    //上传文件
    public boolean upload(String remotePath, File uploadFile) {
        FileInputStream in = null;
        try {
            createDir(remotePath);
            in = new FileInputStream(uploadFile);
            sftp.put(in, uploadFile.getName(), ChannelSftp.RESUME);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

	public boolean upload(String remotePath, String fileName, InputStream in) {
		try {
			createDir(remotePath);
			sftp.cd(remotePath);
			sftp.put(in, fileName, ChannelSftp.RESUME);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

    /**
     * 批量上传文件
     * @param remotePath
     * @param uploadFiles
     * @return
     */
    public boolean uploadBatch(String remotePath, Set<File> uploadFiles) {
        FileInputStream in = null;
        try {
            createDir(remotePath);
            for (File file:uploadFiles) {
                in = new FileInputStream(file);
                sftp.put(in, file.getName(), ChannelSftp.RESUME);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

	public static void main(String[] args) {
		String num = UUID.randomUUID().toString();
		String fileName = num + ".jpg";
		String filePath = Constants.FACE_RECORD_PIC_URL + "/";
		SFTPUtil sftpUtil = new SFTPUtil("arise.test.llvision.com", "llvision", "llvision@test", 40022);
		String newSourcePicBase64 = FileBase64ConvertUitl.encodeBase64File("D:/测试照片/93fb69e0-5c57-4ab5-bb28-131c006a3dd6.jpg");
		byte[] decode = Base64.getDecoder().decode(newSourcePicBase64);
		InputStream input = new ByteArrayInputStream(decode);
		sftpUtil.upload(filePath, fileName, input);
	}
}
