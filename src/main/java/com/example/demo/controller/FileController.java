package com.example.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.example.demo.domain.SubscribeView;
import com.example.demo.domain.User;
import com.example.demo.exception.MyException;
import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;
import com.example.demo.mapper.SubscribeViewMapper;
import com.example.demo.service.FileService;
import com.example.demo.service.UserService;

@RequestMapping("/file")
@Controller
public class FileController {
	
	@Value("${avatar.path}")
	private String avatarPath;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private SubscribeViewMapper mapper;
	
	@Value("${host}")
	private String host;
	
	@Autowired
	private UserService userService;

	@PostMapping("/upload/avatar")
	@ResponseBody
	public MyResult uploadAvatar(MultipartFile file, String sessionKey)
			throws MyException, IllegalStateException, IOException {
		User user = userService.validateSession(sessionKey);
		makeFileDir(avatarPath);
		File filePath = fileService.storeFile(file, avatarPath);
		return MyResultGenerator.successResult(userService.updateAvatarUrl(user, host + filePath.getName()));
	}
	
	@RequestMapping("/excel")
	@ResponseBody
	public MyResult getExel(HttpServletRequest request, HttpServletResponse response) {
		EasyExcel.write(avatarPath+"/test.xlsx", SubscribeView.class).sheet("模板").doWrite(mapper.selectAll());
		File file = new File(avatarPath+"/test.xlsx");
		try(FileInputStream stream = new FileInputStream(file)){
			response.setContentType(request.getSession().getServletContext().getMimeType("xlsx"));
			response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode("test.xlsx", "UTF-8"));
			response.setContentLengthLong(file.length());
			ServletOutputStream out = response.getOutputStream();
			FileCopyUtils.copy(stream, out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return MyResultGenerator.successResult(null);
	}
	
	
	private void makeFileDir(String folder) {
		File targetFile = new File(folder);
		if (!targetFile.exists() && !targetFile.isDirectory()) {
			targetFile.mkdirs();
		}
	}
}
