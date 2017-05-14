package com.akhrszk.services;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

public class MyService {
	public class FileUploader {
		private String uploadPath;
		private MultipartFile file;
		private File uploadedFile;
		
		public FileUploader(MultipartFile file) {
			this.file = file;
		}
		
		public boolean isImage() {
			String contentType = file.getContentType();
			return contentType.matches("image/.*");
		}
		
		private String getExtension(String filename) {
			return filename.substring(filename.lastIndexOf(".") +1);
		}
		
		private String generateFilename(int length) {
			String filename = file.getOriginalFilename();
			String extension = getExtension(filename);
			String rand = RandomStringUtils.randomAlphanumeric(length);
			return rand + '.' + extension;
		}
		
		private void mkdir(String dirpath) {
			File dir = new File(dirpath);
			dir.mkdirs();
		}
		
		public void upload(String path) throws IOException {
			this.uploadPath = path;
			mkdir(uploadPath);
			String filename = generateFilename(25);
			String filePath = uploadPath + '/' + filename;
			File newFile = new File(filePath);
			FileUtils.writeByteArrayToFile(newFile, file.getBytes());
			uploadedFile = newFile;
		}
		
		public void cropSquare() throws IOException {
			BufferedImage image = ImageIO.read(uploadedFile);
			int width = image.getWidth();
			int height = image.getHeight();
			int newWidth = 0;
			int newHeight = 0;
			if(width > height) {
				newWidth = height;
				newHeight = height;
			} else {
				newWidth = width;
				newHeight = width;
			}
			Point point = new Point();
			point.x = (width - newWidth) /2;
			point.y = (height - newHeight) /2;
			BufferedImage croppedImage = image.getSubimage(point.x, point.y, newWidth, newHeight);
			String filename = generateFilename(25);
			String extension = getExtension(filename);
			File newFile = new File(uploadPath + '/' + filename);
			ImageIO.write(croppedImage, extension, newFile);
			uploadedFile = newFile;
		}
		
		public File getUploadedFile() {
			return uploadedFile;
		}
	}

}
