package com.web.chat.service.impl;

import com.web.chat.config.ProperConfig;
import com.web.chat.dao.entity.ColumnAttribute;
import com.web.chat.dao.entity.ParameterAttribute;
import com.web.chat.service.TestService;
import com.web.chat.utils.CookieUtil;
import com.web.chat.utils.Result;
import com.web.chat.utils.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TestServiceImpl implements TestService {
    private Logger logger = LoggerFactory.getLogger(Object.class);
    @Resource
    private ProperConfig properConfig;

    @Override
    public Result login(Map map) {
        map.put("login", "This is testing ApplicationUtil login");
        map.put("properName", properConfig.properName);
        logger.info(map.toString());
        return Result.success(map);
    }

    @Override
    public Result getCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.setCookie(response, "name", "dsfcd");
        logger.info(CookieUtil.getCookie(request, "name"));
        return Result.success(CookieUtil.getCookie(request, "password"));
    }

    @Override
    public Result getSql() {
        ParameterAttribute parameterMap = new ParameterAttribute();
        parameterMap.setCode("AA");
        parameterMap.setFill(false);
        parameterMap.setKey("ab");
        parameterMap.setOperator("in");
//        parameterMap.setOperator("llike");
        parameterMap.setTableName("test");
        parameterMap.setType("Date");

        List parameterList = new ArrayList();
        parameterList.add(parameterMap);

        ColumnAttribute column = new ColumnAttribute();
        column.setCode("SDWSKL");
        column.setTableName("asda");
        List columnList = new ArrayList();
        columnList.add(column);

        Map params = new HashMap();
        params.put("ab", "[1, 2, \"aa\"]");

        SqlUtil sqlUtils = new SqlUtil();
        sqlUtils.setColumnList(columnList);
        sqlUtils.setDbType(SqlUtil.ORACLE);
        sqlUtils.setParameterList(parameterList);
        sqlUtils.setParams(params);
        sqlUtils.setSqlType(SqlUtil.SELECT);
        sqlUtils.setTableName("TEST");

        String sql = SqlUtil.getSql(sqlUtils);
        return Result.success(sql);
    }

    @Override
    public Result testXml() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            //解析xml文档，先获取
            Document doc = builder.parse("C:\\Users\\LUOCHAO\\Desktop\\aa.xml");
            //通过student名字来获取dom节点
            NodeList nodeList = doc.getElementsByTagName("list");
            Element e = (Element) nodeList.item(0);
            //获取值
            System.out.println("姓名：" + e.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
            System.out.println("性别：" + e.getElementsByTagName("sex").item(0).getFirstChild().getNodeValue());
            System.out.println("年龄：" + e.getElementsByTagName("age").item(0).getFirstChild().getNodeValue());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Result setCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.setCookie(response, "password", "123");
        return null;
    }


    public Result login2() {
        return Result.success("This is testing ApplicationUtil login2");
    }

//    public Result login2(Map map) {
//        return Result.success("This is testing ApplicationUtil login21");
//    }

//    public Result login2(Map map, Map map2) {
//        return Result.success("This is testing ApplicationUtil login22");
//    }
}
