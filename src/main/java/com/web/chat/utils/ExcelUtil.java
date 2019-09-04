package com.web.chat.utils;

import com.adm.common.FileHandleFactroy;
import com.adm.handle.model.FileBaseConfig;
import com.adm.utils.FileUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;


public class ExcelUtil {
    private static Logger logger = LoggerFactory.getLogger(Object.class);
    private final static String excel2003L = ".xls";    //2003- 版本的excel
    private final static String excel2007U = ".xlsx";   //2007+ 版本的excel

    /**
     * 导出
     *
     * @param data
     * @param titleMap
     * @param modulePath
     * @param fileName
     * @return
     * @throws IOException
     */
    public static ResponseEntity<byte[]> export(List<JSONObject> data, Map<String, String> titleMap, String modulePath, String fileName) {
        try {

            //获取titleSort
            Map<String, String> titleSort = new HashMap();
            Iterator it = titleMap.entrySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                titleSort.put(entry.getKey().toString(), String.valueOf(++i));
            }

            //初始化excle属性
            FileBaseConfig fileBaseConfig = new FileBaseConfig();
            fileBaseConfig.setTitleNM(titleMap);
            fileBaseConfig.setTitleSort(titleSort);
            fileBaseConfig.setExcelType(FileBaseConfig.XLS_TYPE);
            fileBaseConfig.setSaveFilePath(modulePath + "download" + File.separator + UUID.randomUUID().toString());

            //从这里开始jar需要替换

            // 创建导出文件夹
            FileUtils.createFolder(modulePath);
            FileUtils.createFolder(modulePath + "download");
            FileUtils.createFolder(fileBaseConfig.getSaveFilePath());

            // 执行导出
            new FileHandleFactroy().exportExcelFileByJsonString(data, fileBaseConfig);

            // 获取文件
            List<File> list = Arrays.asList(FileUtils.getFileList(fileBaseConfig.getSaveFilePath()));

            // 添加到流
            ResponseEntity<byte[]> responseEntity = FileHandleUtils.startDownload(list.get(0), new String(fileName.getBytes("gb2312"), "iso-8859-1") + ".xls");

            // 删除导出文件
            FileUtils.deleteAllFiles(fileBaseConfig.getSaveFilePath());
            FileUtils.delFolder(fileBaseConfig.getSaveFilePath());

            return responseEntity;
        } catch (Exception e) {
            logger.info(e.toString());
        }
        return null;
    }

    /**
     * 描述：获取IO流中的数据，组装成List<List<Object>>对象
     *
     * @param in
     * @param fileName
     * @return
     */
    public static List<List<Object>> getData(InputStream in, String fileName) {
        List<List<Object>> list = null;
        Workbook work = null;
        try {
            work = getWorkbook(in, fileName);
            Sheet sheet = null;
            Row row = null;
            Cell cell = null;
            list = new ArrayList<List<Object>>();
            //遍历Excel中所有的sheet
            for (int i = 0; i < work.getNumberOfSheets(); i++) {
                sheet = work.getSheetAt(i);
                if (sheet == null) {
                    continue;
                }
                //遍历当前sheet中的所有行
                for (int j = sheet.getFirstRowNum(); j < sheet.getLastRowNum() + 1; j++) {
                    row = sheet.getRow(j);
                    if (row == null) {
                        continue;
                    }
                    //  if(row==null||row.getFirstCellNum()==j){continue;}
                    //遍历所有的列
                    List<Object> li = new ArrayList<Object>();
                    for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                        cell = row.getCell(y);
                        if (cell == null || "".equals(cell)) {
                            li.add("");
                        } else {
                            li.add(getCellValue(cell));
                        }
                    }
                    list.add(li);
                }
            }
        } catch (Exception e) {
            logger.info(e.toString());
        } finally {
            if (work != null) {
                try {
                    work.close();
                } catch (IOException e) {
                    logger.info(e.toString());
                }
            }
        }
        return list;
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     *
     * @param inStr
     * @param fileName
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (excel2003L.equals(fileType)) {
            wb = new HSSFWorkbook(inStr);  //2003-
        } else if (excel2007U.equals(fileType)) {
            wb = new XSSFWorkbook(inStr);  //2007+
        } else {
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 描述：对表格中数值进行格式化
     *
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell) throws Exception {
        Object value = null;
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字
        //数字格式化
        String style = "";
        if ((cell.getCellType() == Cell.CELL_TYPE_NUMERIC)) {
            Object cellValue = cell.getNumericCellValue();
            if (cellValue == null) {
                cellValue = "";
            }
            if (cellValue.toString().contains(".")) {
                style = cellValue.toString();
                String[] str = style.split("\\.");
                if (str.length > 0) {
                    int i = 0;
                    style = "0.";
                    while (i < str[1].length()) {
                        i++;
                        style += "0";
                    }
                }
            }
        }
        if (!StringUtil.isEmpty(style)) {
            df2 = df = new DecimalFormat(style);  //格式化数字
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    value = df.format(cell.getNumericCellValue());
                } else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
                    value = DateUtil.formatterDate().format(cell.getDateCellValue());
                } else {
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }
}