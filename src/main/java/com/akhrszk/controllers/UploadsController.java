package com.akhrszk.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="uploads")
public class UploadsController {
	
	@Value("${path.uploads.images}")
	private String uploadImagesPath;
	
	@Value("${path.documentroot}")
	private String documentroot;
	
	@ResponseBody
	@RequestMapping(value="/images/{filename:.+}", method=RequestMethod.GET)
	public byte[] uploadedFile(@PathVariable String filename) throws IOException {
		File file = new File(documentroot + uploadImagesPath + '/' + filename);
		InputStream inputStream = new FileInputStream(file);
		return IOUtils.toByteArray(inputStream);
	}
}
