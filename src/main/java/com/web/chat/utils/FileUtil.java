package com.web.chat.utils;

import com.adm.handle.model.FileBaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件操作
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(Object.class);

    /**
     * 获取文件路径
     *
     * @param fileName 文件名.后缀
     * @return
     */
    public static String getFilePathByName(String fileName) {
        return new FileUtil().getClass().getClassLoader().getResource(fileName).getPath();
    }

    /**
     * 文件上传
     *
     * @param request  请求
     * @param key      文件属性名
     * @param savePath 保存路径
     * @return
     */
    public static Map<String, Object> uploadFile(HttpServletRequest request, String key, String savePath) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            //file是form-data中二进制字段对应的name
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile(key);
            // 判断上传的文件是否为空
            if (file != null) {
                // 文件存放路径
                String path = savePath;
                // 文件类型
                String type = null;
                // 文件原名称
                String fileName = file.getOriginalFilename();
                // 判断文件类型
                type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
                // 判断文件类型是否为空
                if (type != null) {
                    // 项目在容器中实际发布运行的根路径
                    String realPath = request.getSession().getServletContext().getRealPath("/");
                    // 判断是放在指定目录下还是固定路径
                    if (!StringUtil.isEmpty(path)) {
                        realPath = path;
                    }
                    // 自定义的文件夹名称
                    String cusFileName = String.valueOf(System.currentTimeMillis());
                    // 创建文件夹
                    com.adm.utils.FileUtils.createFolder(realPath + cusFileName);
                    // 设置存放图片文件的路径
                    path = realPath + cusFileName + "\\" + fileName;
                    resultMap.put("path", path);
                    // 转存文件到指定的路径
                    file.transferTo(new File(path));
                } else {
                    resultMap.put("error", "文件类型为空");
                }
            } else {
                resultMap.put("error", "没有找到相对应的文件");
            }
        } catch (Exception e) {
            logger.info(e.toString());
        }
        return resultMap;
    }

    /**
     * 文件导出
     *
     * @param downloadPath
     * @param savePath
     * @return
     */
    public static ResponseEntity<byte[]> downloadFile(String downloadPath, String savePath) {
        try {
            FileBaseConfig fileBaseConfig = new FileBaseConfig();
            fileBaseConfig.setSaveFilePath(savePath + "download" + File.separator + UUID.randomUUID().toString());
            if (StringUtil.isEmpty(downloadPath)) {
                return null;
            }
            //得到文件
            File file = new File(downloadPath);
            String fileName = file.getName();

            // 创建导出文件夹
            com.adm.utils.FileUtils.createFolder(savePath);
            com.adm.utils.FileUtils.createFolder(savePath + "download");
            com.adm.utils.FileUtils.createFolder(fileBaseConfig.getSaveFilePath());

            // 添加到流
            ResponseEntity<byte[]> responseEntity = FileHandleUtils.startDownload(file, fileName);

            // 删除导出文件
            com.adm.utils.FileUtils.deleteAllFiles(fileBaseConfig.getSaveFilePath());
            com.adm.utils.FileUtils.delFolder(fileBaseConfig.getSaveFilePath());

            return responseEntity;
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }
}
