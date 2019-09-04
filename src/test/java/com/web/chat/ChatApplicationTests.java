package com.web.chat;

import com.web.chat.common.HttpRespons;
import com.web.chat.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Vector;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatApplicationTests {
    @Resource
    TestService testService;

    @Test
    public void contextLoads() {

//        //测试类型转换
//        Map map = new HashMap();
//        map.put("code", 200);
//        map.put("msg", "This is testing ClassUtil");
//
//        //测试接口调用
//        Result o = (Result) ApplicationUtil.execute(TestServiceImpl.class, "login2");
//        System.out.println(o.toString());


//        Result o1 = JSON.parseObject(JSON.toJSONString(map), Result.class);//方式一
//        System.out.println(o1.toString());

//        Result o2 = ClassUtil.toClassByFastjson(map, Result.class);//方式二
//        System.out.println(o2.toString());
//
//        Result o3 = ClassUtil.toClassByUtil(map, Result.class);//方式三
//        System.out.println(o3.toString());


        //测试PropertiesUtil
//        String path = FileUtil.getFilePathByName("application.properties");
//        String path = FileUtil.getFilePathByName("aa.xml");
//        System.out.println(path);
//
//        PropertiesUtil.setValue(path, "bb", "test22", true, false);
//
//        String name = PropertiesUtil.getValue(path, "test.ip", false);
//        String aa = PropertiesUtil.getValue(path, "bb", false);
//        System.out.println(name);
//        System.out.println(aa);

        try {
            HttpURLConnection urlConnection = null;
            String urlString = "http://localhost:8080/chat/test/login";
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            HttpRespons httpResponser = new HttpRespons();
            try {
                InputStream in = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(in));
                httpResponser.contentCollection = new Vector<String>();
                StringBuffer temp = new StringBuffer();
                String line = bufferedReader.readLine();
                while (line != null) {
                    httpResponser.contentCollection.add(line);
                    temp.append(line).append("\r\n");
                    line = bufferedReader.readLine();
                }
                bufferedReader.close();

                String ecod = urlConnection.getContentEncoding();
                if (ecod == null) {
                    ecod = Charset.defaultCharset().name();
                }
                httpResponser.urlString = urlString;
                httpResponser.defaultPort = urlConnection.getURL().getDefaultPort();
                httpResponser.file = urlConnection.getURL().getFile();
                httpResponser.host = urlConnection.getURL().getHost();
                httpResponser.path = urlConnection.getURL().getPath();
                httpResponser.port = urlConnection.getURL().getPort();
                httpResponser.protocol = urlConnection.getURL().getProtocol();
                httpResponser.query = urlConnection.getURL().getQuery();
                httpResponser.ref = urlConnection.getURL().getRef();
                httpResponser.userInfo = urlConnection.getURL().getUserInfo();
                httpResponser.content = new String(temp.toString().getBytes(), ecod);
                httpResponser.contentEncoding = ecod;
                httpResponser.code = urlConnection.getResponseCode();
                httpResponser.message = urlConnection.getResponseMessage();
                httpResponser.contentType = urlConnection.getContentType();
                httpResponser.method = urlConnection.getRequestMethod();
                httpResponser.connectTimeout = urlConnection.getConnectTimeout();
                httpResponser.readTimeout = urlConnection.getReadTimeout();
//                return httpResponser;
            } catch (IOException e) {
                throw e;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            System.out.println("结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        return this.makeContent(urlString, urlConnection);
    }

}
