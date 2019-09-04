package com.web.chat.controller;

import com.web.chat.config.DataSource;
import com.web.chat.config.DataSourceType;
import com.web.chat.service.TestService;
import com.web.chat.utils.HttpRequestUtil;
import com.web.chat.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    private TestService testService;

    @PostMapping("/login")
    @ResponseBody
    public Result login() {
        return testService.login(HttpRequestUtil.getParams());
    }

    @PostMapping("/getCookie")
    @ResponseBody
    public Result getCookie(HttpServletRequest request, HttpServletResponse response) {
        return testService.getCookie(request, response);
    }

    @PostMapping("/setCookie")
    @ResponseBody
    public Result setCookie(HttpServletRequest request, HttpServletResponse response) {
        return testService.setCookie(request, response);
    }

    @PostMapping("/getSql")
    @ResponseBody
    public Result getSql() {
        return testService.getSql();
    }

    @PostMapping("/testXml")
    @ResponseBody
    public Result testXml() {
        return testService.testXml();
    }

    @DataSource(value = DataSourceType.SLAVE)
    @PostMapping("/list")
    @ResponseBody
    public void list() {

    }
}
