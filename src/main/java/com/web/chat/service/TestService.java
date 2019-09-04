package com.web.chat.service;

import com.web.chat.utils.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface TestService {
    public Result login(Map map);

    /**
     * 获取cookie
     *
     * @param request
     * @param response
     * @return
     */
    Result getCookie(HttpServletRequest request, HttpServletResponse response);

    Result setCookie(HttpServletRequest request, HttpServletResponse response);

    Result getSql();

    Result testXml();
}
