package com.web.chat.config;

import com.web.chat.common.HttpRequester;
import com.web.chat.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luochao
 * createTime 2019-7-17 14:00:56
 **/
@Component
public class ApplicationRunnerConfig implements ApplicationRunner {
    private static Logger logger = LoggerFactory.getLogger(Object.class);
    @Resource
    private TestService testService;

    @Override
    public void run(ApplicationArguments args) {
        logger.info("通过实现ApplicationRunner接口，在spring boot项目启动后执行调度");
        try {
            HttpRequester httpRequester = new HttpRequester();
            Map<String, String> map = new HashMap<>();
            map.put("id", "*");
            map.put("start", "true");//false/true
            String url = "http://localhost:8080/test/login";
            httpRequester.sendPost(url, map);//方式1,通过url,暂有问题(需通过application启动)
//            testService.login(map);//方式2,通过接口
        } catch (Exception e) {
            logger.info(e.toString());
            e.printStackTrace();
        }
    }
}