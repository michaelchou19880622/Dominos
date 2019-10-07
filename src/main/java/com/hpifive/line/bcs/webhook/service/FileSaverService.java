package com.hpifive.line.bcs.webhook.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class FileSaverService {

	
	private static final Logger logger = LoggerFactory.getLogger(FileSaverService.class);

	/**
	 * 
	 * @param image Buffered
	 * @param filename 不包含附檔名的檔案名稱
	 * @param type 檔案擴展名
	 */
	public void save(BufferedImage image, String filename, String type) {
		File file = this.getFile("image", filename+"."+type);
		try {
			ImageIO.write(image, type, file);
		} catch (Exception e) {
			logger.error("Save Image error", e);
		}
		
	}
	
	public File getFile(String folderName, String filename) {
		File homedir = new File(System.getProperty("user.home"));
		File result = new File(homedir, this.getFilePath(folderName, filename));
		if (! result.exists()) {
			try {
				//TODO: 查一下權限的問題
				Files.createDirectories(result.toPath());
			} catch (Exception e) {
				logger.error("File createDirectories", e);
			}
		}
		logger.info("Absolute Path: {}", result.getAbsolutePath());
		return result;
	}
	
	/**
	 * 
	 * @param folderName 資料夾名稱(ex: csv, image, etc.)
	 * @param filename 包含檔案擴展名
	 * @return absolute path of file
	 */
	public String getPath(String folderName, String filename) {
		return getFile(folderName, filename).getAbsolutePath();
	}
	
	/**
	 * 
	 * @param folderName 資料夾名稱(ex: csv, image, etc.)
	 * @param filename 包含檔案擴展名
	 * @return relative path
	 */
	private String getFilePath(String folderName, String filename) {
		String basic = "upload";
		String path = String.format("%s%s%s", basic, File.separator, folderName);
		return String.format("%s%s%s", path, File.separator, filename);
	}
	
	
}
