package com.web.chat.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.*;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created by chengsheng on 2015/8/19.
 * update by luochao on 2019-8-14 10:01:56
 */
public class XmlUtil {

    public static String readFileByPath(String path) {
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            ByteBuffer bb = ByteBuffer.allocate(new Long(file.length()).intValue());
            //fc向buffer中读入数据
            fc.read(bb);
            //反转此缓冲区。首先对当前位置设置限制，然后将该位置设置为零。如果已定义了标记，则丢弃该标记。
            bb.flip();
            String str = new String(bb.array(), "UTF8");
            fc.close();
            fis.close();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * xml转json
     *
     * @param xmlStr
     * @return
     * @throws DocumentException
     */
    public static JSONObject xml2Json(String xmlStr) {
        JSONObject json = new JSONObject();
        try {
            Document doc = DocumentHelper.parseText(xmlStr);
            dom4j2Json(doc.getRootElement(), json);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * xml转json
     *
     * @param element
     * @param json
     */
    public static void dom4j2Json(Element element, JSONObject json) {
        //如果是属性
        for (Object o : element.attributes()) {
            Attribute attr = (Attribute) o;
//            if (StringUtil.isNotEmpty(attr.getValue())) {
            json.put("@" + attr.getName(), attr.getValue());
//            }
        }
        List<Element> chdEl = element.elements();
        if (chdEl.isEmpty() && StringUtil.isNotEmpty(element.getText())) {//如果没有子元素,只有一个值
            json.put(element.getName(), element.getText());
        }
        for (Element e : chdEl) {//有子元素
            for (Object o : element.attributes()) {
                Attribute attr = (Attribute) o;
//                    if (StringUtil.isNotEmpty(attr.getValue())) {
                json.put("@" + attr.getName(), attr.getValue());
//                    }
            }
            if (!e.elements().isEmpty()) {//子元素也有子元素
                JSONObject child = new JSONObject();
                dom4j2Json(e, child);
                Object o = json.get(e.getName());
                if (o != null) {//o != null
                    JSONArray jArray = null;
                    if (o instanceof JSONObject) {//如果此元素已存在,则转为jsonArray
                        JSONObject obj = (JSONObject) o;
                        json.remove(e.getName());
                        jArray = new JSONArray();
                        jArray.add(obj);
                        jArray.add(child);
                    }
                    if (o instanceof JSONArray) {
                        jArray = (JSONArray) o;
                        jArray.add(child);
                    }
                    json.put(e.getName(), jArray);
                } else {
//                    //去除empty元素
//                    if (!child.isEmpty()) {
//                        continue;
//                    }
                    json.put(e.getName(), child);
                }
            } else {//子元素没有子元素
                for (Object o : element.attributes()) {
                    Attribute attr = (Attribute) o;
//                    if (StringUtil.isNotEmpty(attr.getValue())) {
                    json.put("@" + attr.getName(), attr.getValue());
//                    }
                }
                //去除empty元素
//                if (!e.getText().isEmpty()) {
//                    continue;
//                }
                json.put(e.getName(), e.getText());
            }
        }
    }


//    public static void main(String[] args) {
//        String xmlStr = readFileByPath("C:\\Users\\LUOCHAO\\Desktop\\aa.xml");
//        JSONObject json = xml2Json(xmlStr);
//        System.out.println("xml2Json:" + json.toJSONString());
//
//
//        System.out.println(StringUtil.isEmpty(null));
//
//    }
}