//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.web.chat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * request操作
 */
public class HttpRequestUtil {
    private static Logger logger = LoggerFactory.getLogger(Object.class);
    private ServletRequestAttributes servletRequestAttributes;

    private HttpRequestUtil(ServletRequestAttributes servletRequestAttributes) {
        this.servletRequestAttributes = servletRequestAttributes;
    }

    /**
     * request转Map
     *
     * @param request
     * @return
     */
    public static Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String[]> properties = request.getParameterMap();
        Map<String, String> returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            Entry entry = (Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (!(valueObj instanceof String[])) {
                value = valueObj.toString();
            } else {
                String[] values = (String[]) ((String[]) valueObj);
                for (int i = 0; i < values.length; ++i) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            }
            try {
                value = value.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                returnMap.put(name, URLDecoder.decode(value, "UTF-8"));
            } catch (UnsupportedEncodingException var10) {
                logger.info(var10.toString());
            }
        }
        return returnMap;
    }

    /**
     * request转Map
     *
     * @return
     */
    public static Map<String, String> getParams() {
        HttpRequestUtil httpRequestUtils = new HttpRequestUtil((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        return getParams(httpRequestUtils.servletRequestAttributes.getRequest());
    }
}
