package com.example.demo.service.imp;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.MyException;
import com.example.demo.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImp implements FileService{

	@Override
	public File storeFile(MultipartFile file, String fileFolder) {
		String fileName = file.getOriginalFilename();
		log.info(fileName);
		File dir = new File(fileFolder + "/" + fileName);
		try {
			file.transferTo(dir.getAbsoluteFile());
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new MyException("上传失败");
		}
		String filePath = dir.getPath();
		log.info(filePath);
		return dir;
	}

}
