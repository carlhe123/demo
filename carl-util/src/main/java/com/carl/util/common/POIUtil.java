package com.carl.util.common;


import cn.hutool.core.util.ObjectUtil;
import org.apache.poi.xwpf.usermodel.*;
import org.docx4j.org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class POIUtil {

    private static final Logger logger = LoggerFactory.getLogger(POIUtil.class);

    /**
     * word文档转换成html输出
     *
     * @param filePath
     * @return
     */
    public static String convertWordToHtml(String filePath) throws Exception {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            // 获取word中的所有段落与表格
//            if (filePath.toLowerCase().endsWith("docx")) {
                XWPFDocument document = new XWPFDocument(inputStream);
                List<IBodyElement> elements = document.getBodyElements();
                for (IBodyElement element : elements) {
                    // 段落
                    if (element instanceof XWPFParagraph) {
                        sb.append(parseParagraphText((XWPFParagraph) element, "<br/>"));
                    }
                    // 表格
                    else if (element instanceof XWPFTable) {
                        sb.append(parseTableToHtml((XWPFTable) element, filePath)).append("<br/>");
                    }
                }
//            }
        } catch (Exception e) {
            logger.info("convertWordToHtml_error ex={}",e.toString(),e);
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取段落内容
     *
     * @param xwpfParagraph
     * @param separator
     * @return
     */
    public static String parseParagraphText(XWPFParagraph xwpfParagraph, String separator) {
        List<XWPFRun> runs = xwpfParagraph.getRuns();
        if (runs.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        System.out.println(xwpfParagraph.getBorderLeft());
        System.out.println(xwpfParagraph.getBorderRight());
        if (xwpfParagraph.getIndentationFirstLine() == 420 ) {
            sb.append("&nbsp&nbsp&nbsp&nbsp");
        }
        for (XWPFRun run : runs) {
            if(!StringUtils.isEmpty(run.getColor())) {
                sb.append("<font color='" + run.getColor() +"'>"+run.text() + "</font>");
            }else {
                sb.append(run.text());
            }
        }
        return sb.toString().concat(separator);

    }

    /**
     * 将表格转成html
     *
     * @param xwpfTable
     * @param filePath
     * @return
     */
    public static String parseTableToHtml(XWPFTable xwpfTable, String filePath) {
        try {
            int columns = xwpfTable.getRows().get(0).getTableCells().size();
            List<String> list = new ArrayList<>(getTableText(xwpfTable, filePath));
            return concatHtml(list, columns);
        } catch (Exception e) {
            logger.info("parseTableToHtml_error ex={}",e.toString(),e);
            return "";
        }
    }

    /**
     * 获得表格内容
     *
     * @param table
     * @param filePath
     * @return
     */
    public static List<String> getTableText(XWPFTable table, String filePath) {
        List<String> list = new ArrayList<>();
//        if (filePath.toLowerCase().endsWith("docx")) {
            // 如果是office2007  docx格式
            List<XWPFTableRow> rows = table.getRows();
            //读取每一行数据
            for (XWPFTableRow row : rows) {
                //读取每一列数据
                List<XWPFTableCell> cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    //输出当前的单元格的数据
                    list.add(cell.getText());
                }
            }
//        }
        return list;
    }

    /**
     * 表格内容拼接成html
     *
     * @param list
     * @param columns
     * @return
     */
    public static String concatHtml(List<String> list, int columns) {
        StringBuilder sb = new StringBuilder("");
        if (!CollectionUtils.isEmpty(list)) {
            sb.append("<html><body><table border=\"1\" >");
            for (int i = 0; i < list.size(); i++) {
                if ((i + 1) % columns == 1) {
                    sb.append("<tr>");
                }
                sb.append("<td>").append(list.get(i)).append("</td>");
                if ((i + 1) % columns == 0) {
                    sb.append("</tr>");
                }
            }
            sb.append("</table>");
            sb.append("</body></html>");
        }
        StringBuilder replace = new StringBuilder("<tr>");
        for (int i = 0; i < columns; i++) {
            replace.append("<td></td>");
        }
        replace.append("</tr>");
        return sb.toString().replace(replace, "");
    }


}

