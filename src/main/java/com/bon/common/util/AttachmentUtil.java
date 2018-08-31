package com.bon.common.util;

import com.bon.common.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;

/**
 * @program: 后端-基础项目
 * @description: 文件工具类
 * @author: Bon
 * @create: 2018-08-23 20:07
 **/

public class AttachmentUtil {

	private static MyLog log = MyLog.getLog(AttachmentUtil.class);
	private static final String FTP_PATH = GeneratePropertyUtil.getProperty("attachment.path");

	private static String getCreateAttachmentRelativePath(String fileName) {
		return String.format("%s/%s", DateUtil.format(new Date(), DateUtil.DATE_CHAR_PATTERN), fileName);
	}

	public static String getAttachmentAbsolutePath(String fileSource) {
		return String.format("%s/%s", FTP_PATH, fileSource);
	}

	public static String getCreateAttachmentAbsolutePath(String fileName) {
		return String.format("%s/%s", FTP_PATH, getCreateAttachmentRelativePath(fileName));
	}

	public static String uploadAvatar(MultipartFile file) throws BusinessException {
		if (file == null) {
			throw new BusinessException("上传文件不能为空");
		}

		File ftpFile = new File(FTP_PATH);
		if (ftpFile == null || (!ftpFile.exists())) {
			ftpFile.mkdirs();
		}

		String fileName = file.getOriginalFilename();

		//获取后缀
		int dot = fileName.lastIndexOf('.');
		String extend = fileName.substring(dot + 1);

		String uuid = UUID.randomUUID().toString();

		//拼接文件名
		String saveFileName = uuid + "." + extend;
		String absolutePath = AttachmentUtil.getCreateAttachmentAbsolutePath(saveFileName);
		String relativePath = AttachmentUtil.getCreateAttachmentRelativePath(saveFileName);

		//创建文件
		File targetFile = new File(absolutePath);

		if (!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}

		// 保存
		try {
			//转换文件
			file.transferTo(targetFile);
			return relativePath;
		} catch (Exception e) {
			log.error("【上传文件】错误：" + e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
	}

	public static void download(HttpServletResponse res,String fileName,String filePath) throws BusinessException, UnsupportedEncodingException {
		res.setHeader("content-type", "application/octet-stream");
		res.setContentType("application/octet-stream");
		res.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			os = res.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void getImage(HttpServletResponse res,String filePath) {
		// 设置响应的类型格式为图片格式
		res.setContentType("image/jpeg");
		//禁止图像缓存。
		res.setHeader("Pragma", "no-cache");
		res.setHeader("Cache-Control", "no-cache");
		res.setDateHeader("Expires", 0);
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			//获取绝对地址
			filePath = FTP_PATH + filePath;
			os = res.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


//	public static Integer uploadAttachmentFile(MultipartFile file, String kind, Date createTime) throws BusinessException {
//		if (file == null) {
//			throw new BusinessException("attachment_upload_00001");
//		}
//
//		File ftpFile = new File(FTP_PATH);
//		if (ftpFile == null || (!ftpFile.exists())) {
//			ftpFile.mkdirs();
//		}
//		IAttachmentDao attachmentDao = (IAttachmentDao) SpringUtil.getBean("IAttachmentDao");
//
//		String fileName = file.getOriginalFilename();
//
//		int dot = fileName.lastIndexOf('.');
//		String extend = fileName.substring(dot + 1);
//
//		String uuid = UUID.randomUUID().toString();
//
//		String saveFileName = uuid + "." + extend;
//		String absolutePath = AttachmentUtil.getCreateAttachmentAbsolutePath(saveFileName);
//		String relativePath = AttachmentUtil.getCreateAttachmentRelativePath(saveFileName);
//
//		File targetFile = new File(absolutePath);
//
//		if (!targetFile.getParentFile().exists()) {
//			targetFile.getParentFile().mkdirs();
//		}
//
//		// 保存
//		try {
//			file.transferTo(targetFile);
//
//			Attachment att = new Attachment();
//			att.setExtend(extend);
//			att.setKind(kind);
//			att.setName(fileName);
//			att.setSource(relativePath);
//			att.setCreateTime(createTime);
//			return attachmentDao.saveWithIncrementId(att).intValue();
//		} catch (Exception e) {
//			log.error("【上传文件】错误：" + e.getMessage(), e);
//			throw new BusinessException(e.getMessage());
//		}
//	}
//
//	public static Integer uploadAttachmentFile(MultipartFile file, AttachmentType type, Integer referId,
//                                               Integer enterpriceId) throws BusinessException {
//		if (file == null) {
//			throw new BusinessException("attachment_upload_00001");
//		}
//
//		String ftpPath = ServerSystemContext.getProp().getAttachmentPath();
//		File ftpFile = new File(ftpPath);
//		if (ftpFile == null || (!ftpFile.exists())) {
//			ftpFile.mkdirs();
//		}
//		IAttachmentDao attachmentDao = (IAttachmentDao) SpringUtil.getBean("IAttachmentDao");
//
//		String fileName = file.getOriginalFilename();
//
//		int dot = fileName.lastIndexOf('.');
//		String extend = fileName.substring(dot + 1);
//
//		String uuid = UUID.randomUUID().toString();
//
//		String saveFileName = uuid + "." + extend;
//		String absolutePath = AttachmentUtil.getCreateAttachmentAbsolutePath(saveFileName);
//		String relativePath = AttachmentUtil.getCreateAttachmentRelativePath(saveFileName);
//
//		// // 判断文件是否存在
//		// File targetFile = new File(absolutePath);
//		// int i = 1;
//		// String tempName = fileName;
//		// while (targetFile.exists()) {
//		// int dot = fileName.lastIndexOf('.');
//		// String name = fileName.substring(0, dot);
//		// String extend = fileName.substring(dot + 1);
//		// name = String.format("%s_%s", name, i);
//		// tempName = String.format("%s.%s", name, extend);
//		//
//		// absolutePath = AttachmentUtil.getAttachmentAbsolutePath(type,
//		// referId, tempName);
//		// relativePath = AttachmentUtil.getAttachmentRelativePath(type,
//		// referId, tempName);
//		// targetFile = new File(absolutePath);
//		// i++;
//		// }
//		// fileName = tempName;
//
//		File targetFile = new File(absolutePath);
//
//		if (!targetFile.getParentFile().exists()) {
//			targetFile.getParentFile().mkdirs();
//		}
//
//		// 保存
//		try {
//			file.transferTo(targetFile);
//
//			Attachment att = new Attachment();
//			att.setEnterpriceId(enterpriceId);
//			att.setExtend(extend);
//			att.setKind(type.getValue());
//			att.setName(fileName);
//			att.setReferId(referId);
//			att.setReferTable(type.getReferTable());
//			att.setSource(relativePath);
//			att.setCreateTime(new Date());
//			return attachmentDao.saveWithIncrementId(att).intValue();
//		} catch (Exception e) {
//			log.error("【上传文件】错误：" + e.getMessage(), e);
//			throw new BusinessException(e.getMessage());
//		}
//	}
}
