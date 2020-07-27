package com.example.demo.vo;

import java.util.Map;

import com.example.demo.domain.TemplateData;

import lombok.Data;

@Data
public class WxMssVo {
	private String touser;//用户openid
    private String template_id;//订阅消息模版id
    private String page = "pages/index/index";//默认跳到小程序首页
    private String miniprogram_state = "developer";
    private Map<String, TemplateData> data;//推送文字
}
