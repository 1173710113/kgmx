package com.example.demo.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	public File storeFile(MultipartFile file, String fileFolder);
}
