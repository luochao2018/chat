package com.web.chat.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.stream.FileImageInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * word操作
 */
public class WordUtil {
    private static Logger logger = LoggerFactory.getLogger(Object.class);
    public static final String CONTENT = "一般文本";
    public static final String TABLE = "表格";
    public static final String IMG = "图片";
    private XWPFDocumentUtil xdoc;//PDF模板对象
    private XWPFTableCell cell;//单元格对象
    private String text;//内容
    private String bgcolor;//背景色
    private int width;//宽度
    private String fontType;//字体样式
    private int fontSize;//字体大小
    private boolean isBlod = false;//是否加粗
    private ParagraphAlignment align = ParagraphAlignment.CENTER;//对齐方式

    public WordUtil() {
    }

    public WordUtil(XWPFDocumentUtil xdoc, XWPFTableCell cell, String text, String bgcolor, int width, String fontType, int fontSize, boolean isBlod, ParagraphAlignment align) {
        this.xdoc = xdoc;
        this.cell = cell;
        this.text = text;
        this.bgcolor = bgcolor;
        this.width = width;
        this.fontType = fontType;
        this.fontSize = fontSize;
        this.isBlod = isBlod;
        this.align = align;
    }

    public XWPFDocumentUtil getDoc() {
        return xdoc;
    }

    public void setDoc(XWPFDocumentUtil xdoc) {
        this.xdoc = xdoc;
    }

    public XWPFTableCell getCell() {
        return cell;
    }

    public void setCell(XWPFTableCell cell) {
        this.cell = cell;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getFontType() {
        return fontType;
    }

    public void setFontType(String fontType) {
        this.fontType = fontType;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isBlod() {
        return isBlod;
    }

    public void setBlod(boolean blod) {
        isBlod = blod;
    }

    public ParagraphAlignment getAlign() {
        return align;
    }

    public void setAlign(ParagraphAlignment align) {
        this.align = align;
    }

    /**
     * 导出word(入口)
     *
     * @param fileName 文件名
     * @param maps     传参(type,title,data)
     * @return
     */
    public static XWPFDocument export(String fileName, List<Map> maps) {
        try {
            XWPFDocumentUtil xdoc = new XWPFDocumentUtil();//初始化doc对象
            createCtp(xdoc, fileName);//创建页眉
            for (Map map : maps) {
                String type = (String) map.get("type");//类型(文本,表格,图片)
                String title = (String) map.get("title");//标题
                String path = (String) map.get("path");//路径(图片操作,如果传送的地址,而不是字节流时,需要传递)
                createDocTitle(xdoc, title);//创建标题
                XWPFTable table = xdoc.createTable(1, 1);
                switch (type) {
                    case TABLE:
                        List<Map> data = (List<Map>) map.get("data");
                        if (data != null) {
                            if (data.get(0).size() > 0) {
                                table = xdoc.createTable(data.size(), data.get(0).size());
                            }
                        }
                        break;
                    case IMG:
                        table = xdoc.createTable(1, 1);
                        if (path != null && !"".equals(path)) {
                            map.put("data", image2byte(path));
                        }
                        break;
                    default:
                }
                createDataReportTable(type, table, xdoc, map.get("data"));
            }
            return xdoc;
        } catch (Exception e) {
            logger.info(e.toString());
            throw new RuntimeException("create file error");
        }
    }

    /**
     * 图片转byte数组
     *
     * @param path
     * @return
     */
    public static byte[] image2byte(String path) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            logger.info(ex1.toString());
        } catch (IOException ex1) {
            logger.info(ex1.toString());
        }
        return data;
    }

    /**
     * 生成页眉
     *
     * @param xdoc  对象
     * @param title 标题
     */
    public static void createCtp(XWPFDocumentUtil xdoc, String title) {
        CTSectPr sectPr = xdoc.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(xdoc, sectPr);
        // 添加页眉
        CTP ctpHeader = CTP.Factory.newInstance();
        CTR ctrHeader = ctpHeader.addNewR();
        CTText ctHeader = ctrHeader.addNewT();
        ctHeader.setStringValue(title);
        XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, xdoc);
        // 设置为左对齐
        headerParagraph.setAlignment(ParagraphAlignment.BOTH);
        XWPFParagraph[] parsHeader = new XWPFParagraph[1];
        parsHeader[0] = headerParagraph;
        try {
            policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);
        } catch (IOException e) {
            logger.info(e.toString());
        }
    }

    /**
     * 生成标题
     *
     * @param xdoc       对象
     * @param title      标题
     * @param align      位置
     * @param size       字体大小
     * @param fontFamily 字体样式
     * @param blod       加粗
     * @param color      颜色
     */
    public static void createDocTitle(XWPFDocumentUtil xdoc, String title, ParagraphAlignment align, int size, String fontFamily, boolean blod, String color) {
        XWPFParagraph headLine = xdoc.createParagraph();
        headLine.setAlignment(align);
        XWPFRun runHeadLine = headLine.createRun();
        runHeadLine.setText(title);
        runHeadLine.setFontSize(size);
        runHeadLine.setFontFamily(fontFamily);
        runHeadLine.setBold(blod);
        runHeadLine.setColor(color);
    }

    /**
     * 生成标题(默认值)
     *
     * @param xdoc  对象
     * @param title 标题
     */
    public static void createDocTitle(XWPFDocumentUtil xdoc, String title) {
        createDocTitle(xdoc, title, ParagraphAlignment.CENTER, 16, "华文仿宋", true, "333333");
    }

    /**
     * doc表格初始化
     *
     * @param type    类型
     * @param table   列表信息
     * @param xdoc    对象
     * @param dataObj 数据
     * @param bgColor 背景色
     * @param bigW    最大宽度
     */
    public static void createDataReportTable(String type, XWPFTable table, XWPFDocumentUtil xdoc, Object dataObj, String bgColor, int bigW, String fontType, int fontSize, boolean blod, ParagraphAlignment align) {
        CTTbl ttbl = table.getCTTbl();
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
        tblWidth.setW(new BigInteger(String.valueOf(bigW)));
        tblWidth.setType(STTblWidth.AUTO); // STTblWidth.AUTO 自动长度
        //行列合并暂时不处理
        // mergeCellsVertically(table, 0, 0, 3);//行合并
        // mergeCellsHorizontal(table, 0, 0, 1);//列合并

        XWPFTableCell cell = null;
        //图片加载失败,转文本提示
        WordUtil word = new WordUtil(xdoc, cell, "图片加载失败", bgColor, bigW, fontType, fontSize, blod, align);
        switch (type) {
            case TABLE:
                if (dataObj == null) {
                    break;
                }
                List<Map> data = (List<Map>) dataObj;
                if (data.size() < 1) {
                    break;
                }
                int width = bigW / data.get(0).size();//计算宽度
                if (width < 100) {
                    width = 100;//控制最小宽度
                }
                for (int rowNumber = 0; rowNumber < data.size(); rowNumber++) {
                    int cellNumber = 0;
                    Iterator it = data.get(rowNumber).entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        Object value = entry.getValue();
                        if (value == null) {
                            value = "";
                        }
                        cell = initCellHight(table, rowNumber, cellNumber, 24, false);
                        word.setCell(cell);
                        word.setText(value.toString());
                        word.setWidth(width);
                        setCellText(word);
                        cellNumber++;
                    }
                }
                break;
            case IMG:
                cell = initCellHight(table, 0, 0, 1200, true);
                byte[] base64Info1 = (byte[]) dataObj;
                if (base64Info1 == null || base64Info1.length < 100) {
                    word.setCell(cell);
                    setCellText(word);
                } else {
                    setCellPic(xdoc, initCellHight(table, 0, 0, 1200, true), base64Info1);
                }
                break;
            default:
        }
    }

    /**
     * doc表格初始化(默认参数)
     *
     * @param type    类型
     * @param table   列表信息
     * @param xdoc    对象
     * @param dataObj 数据
     */
    public static void createDataReportTable(String type, XWPFTable table, XWPFDocumentUtil xdoc, Object dataObj) {
        createDataReportTable(type, table, xdoc, dataObj, "111111", 8600, "仿宋", 9, false, ParagraphAlignment.CENTER);
    }

    /**
     * 设置单元格高度
     *
     * @param xTable
     * @param rowNomber
     * @param cellNumber
     * @param hight
     * @param ifNIL
     * @return
     */
    public static XWPFTableCell initCellHight(XWPFTable xTable, int rowNomber, int cellNumber, int hight, boolean ifNIL) {
        XWPFTableRow row = null;
        row = xTable.getRow(rowNomber);
        row.setHeight(hight);
        XWPFTableCell cell = null;
        cell = row.getCell(cellNumber);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        if (ifNIL) {
            CTTcBorders tblBorders = row.getCell(0).getCTTc().getTcPr().addNewTcBorders();
            tblBorders.addNewLeft().setVal(STBorder.NIL);
            tblBorders.addNewRight().setVal(STBorder.NIL);
            tblBorders.addNewBottom().setVal(STBorder.NIL);
            tblBorders.addNewTop().setVal(STBorder.NIL);
            //隐藏这一行所有单元格的边框
            for (int i = 0; i < row.getTableCells().size(); i++) {
                row.getCell(i).getCTTc().getTcPr().setTcBorders(tblBorders);
            }
        }
        return cell;
    }

    /**
     * cell设置图片
     *
     * @param xdoc
     * @param cell
     * @param imageByte
     */
    public static void setCellPic(XWPFDocumentUtil xdoc, XWPFTableCell cell, byte[] imageByte) {
        try {
            //设置图片类型
            xdoc.addPictureData(imageByte, XWPFDocument.PICTURE_TYPE_PNG);
            xdoc.createPicture(cell.addParagraph(), xdoc.getAllPictures().size() - 1, 570, 200, "    ");
        } catch (InvalidFormatException e) {
            logger.info(e.toString());
        }
    }

    /**
     * cell设置文本
     *
     * @param word
     */
    public static void setCellText(WordUtil word) {
        CTTc cttc = word.getCell().getCTTc();
        CTTcPr cellPr = cttc.addNewTcPr();
        cellPr.addNewTcW().setW(BigInteger.valueOf(word.getWidth()));
        XWPFParagraph pIO = word.getCell().addParagraph();
        pIO.setAlignment(word.getAlign());
        word.getCell().removeParagraph(0);
        if (word.getText().contains("\n")) {
            String[] strings = word.getText().split("\n");
            for (int i = 0; i < strings.length; i++) {
                String content = strings[i];
                if (word.isBlod()) {
                    if (i == 0) {
                        setTextStyle(pIO, word.getFontType(), word.getBgcolor(), word.getFontSize(), content, true, true);
                    } else {
                        setTextStyle(pIO, word.getFontType(), word.getBgcolor(), word.getFontSize(), "      " + content, true, false);
                    }
                } else {
                    setTextStyle(pIO, word.getFontType(), word.getBgcolor(), word.getFontSize(), content, true, false);
                }
            }
        } else {
            setTextStyle(pIO, word.getFontType(), word.getBgcolor(), word.getFontSize(), word.getText(), false, false);
        }
    }

    /**
     * 设置文本样式
     *
     * @param pIO
     * @param fontType
     * @param bgcolor
     * @param fontSize
     * @param text
     * @param isEntery
     * @param isBold
     */
    public static void setTextStyle(XWPFParagraph pIO, String fontType, String bgcolor, int fontSize, String text, boolean isEntery, boolean isBold) {
        if (fontType == null || fontType.equals("")) {
            fontType = "微软雅黑";
        }
        if (bgcolor == null || bgcolor.equals("")) {
            bgcolor = "000000";
        }
        XWPFRun rIO = pIO.createRun();
        rIO.setFontFamily(fontType);
        rIO.setColor(bgcolor);
        rIO.setFontSize(fontSize);
        rIO.setText(text);
        rIO.setBold(isBold);
        if (isEntery) {
            rIO.addBreak();
        }
    }

    /**
     * 设置表格间的空行
     *
     * @param xdoc
     * @param r1
     */
    public void setEmptyRow(XWPFDocumentUtil xdoc, XWPFRun r1) {
        XWPFParagraph p1 = xdoc.createParagraph();
        p1.setAlignment(ParagraphAlignment.CENTER);
        p1.setVerticalAlignment(TextAlignment.CENTER);
        r1 = p1.createRun();
    }

    /**
     * word 跨列合并单元格
     *
     * @param table
     * @param row
     * @param fromCell
     * @param toCell
     */
    public void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
            if (cellIndex == fromCell) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * word 跨行合并单元格
     *
     * @param table
     * @param col
     * @param fromRow
     * @param toRow
     */
    public void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            if (rowIndex == fromRow) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

//    public static void main(String[] args) {
//        try {
//            //获取模板
//            XWPFDocument xdoc = new ExportWordUtil().export(fileName, listData);
//            //写入文件
//            FileOutputStream fos = new FileOutputStream(fileBaseConfig.getSaveFilePath() + "/img.docx");
//            xdoc.write(fos);
//            fos.close();
//            // 获取文件
//            List<File> list = Arrays.asList(com.adm.utils.FileUtils.getFileList(fileBaseConfig.getSaveFilePath()));
//            // 添加到流
//            try {
//                //下载文件
//                responseEntity = FileHandleUtils.startDownload(list.get(0), new String(fileName.getBytes("gb2312"), "iso-8859-1") + ".docx");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            // 删除导出文件
//            com.adm.utils.FileUtils.deleteAllFiles(fileBaseConfig.getSaveFilePath());
//            com.adm.utils.FileUtils.delFolder(fileBaseConfig.getSaveFilePath());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
