package com.carl.util.common;

import cn.hutool.core.util.ObjectUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Word文档操作工具类 （替换Word文档中的占位符）
 * @author Carl.He
 * @date 2020/08/06 11:11
 */
public class PoiWordUtil {

    // 标识忽略
    static final int ignoreTg = 0;
    // 标识已读取到'$'字符
    static final int startTg = 1;
    // 标识已读取到'{'字符
    static final int readTg = 2;

    /**
     * 获取表格中占位符信息
     *
     * @param doc               当前文档
     * @param list              存储占位符信息
     * @param placeholderValues 需要替换的值
     * @return void
     * @date 2020/7/6
     */
    public static void getTableParagraphs(XWPFDocument doc, List<XWPFParagraph> list, Map<String, String> placeholderValues) {
        List<XWPFTable> tables = doc.getTables();
        if (ObjectUtil.isEmpty(tables)) {
            return;
        }
        tables.forEach(table -> table.getRows().forEach(row -> row.getTableCells().forEach(cell -> {
            String text = cell.getText();
            if (ObjectUtil.isEmpty(text)) {
                return;
            }
            Iterator<String> it = placeholderValues.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (text.indexOf(key) != -1) {
                    list.addAll(cell.getParagraphs());
                }
            }
        })));
    }

    /**
     * 清除占位符信息
     *
     * @param placeholderList 占位符位置信息
     * @param paragraphList   行数据
     * @return void
     * @date 2020/6/10
     */
    public static void clearPlaceholder(List<int[]> placeholderList, List<XWPFParagraph> paragraphList) {
        if (ObjectUtil.isEmpty(placeholderList)) {
            return;
        }
        int[] currentPlaceholder = placeholderList.get(0);
        StringBuilder tempSb = new StringBuilder();
        for (int i = 0; i < paragraphList.size(); i++) {
            XWPFParagraph p = paragraphList.get(i);
            List<XWPFRun> runs = p.getRuns();
            for (int j = 0; j < runs.size(); j++) {
                XWPFRun run = runs.get(j);
                String text = run.getText(run.getTextPosition());
                StringBuilder nval = new StringBuilder();
                char[] textChars = text.toCharArray();
                for (int m = 0; m < textChars.length; m++) {
                    char c = textChars[m];
                    if (null == currentPlaceholder) {
                        nval.append(c);
                        continue;
                    }
                    //处理因为文档格式出现“$”未替换的问题
                    if (j == currentPlaceholder[1]-1 && currentPlaceholder[2] == -1){
                        if (c == '$') {
                            tempSb.append(c);
                            continue;
                        }
                    }
                    // 排除'$'和'}'两个字符之间的字符
                    int start = currentPlaceholder[0] * 1000000 + currentPlaceholder[1] * 500 + currentPlaceholder[2];
                    int end = currentPlaceholder[3] * 1000000 + currentPlaceholder[4] * 500 + currentPlaceholder[5];
                    int cur = i * 1000000 + j * 500 + m;
                    if (!(cur >= start && cur <= end)) {
                        nval.append(c);
                    } else {
                        tempSb.append(c);
                    }
                    //判断是否是占位符结尾，如果是那获取新的占位符
                    if (tempSb.toString().endsWith("}")) {
                        placeholderList.remove(0);
                        if (ObjectUtil.isEmpty(placeholderList)) {
                            currentPlaceholder = null;
                            continue;
                        }
                        currentPlaceholder = placeholderList.get(0);
                        tempSb = new StringBuilder();
                    }
                }
                run.setText(nval.toString(), run.getTextPosition());

            }
        }
    }

    /**
     * 获取占位符信息，并且在占位符后面填充值
     *
     * @param paragraphList 行数据
     * @param map           要替换的占位符key\value
     * @return java.util.List<int [ ]>
     * @date 2020/6/10
     */
    public static List<int[]> getPlaceholderList(List<XWPFParagraph> paragraphList, Map<String, String> map) {
        // 存储占位符 位置信息集合
        List<int[]> placeholderList = new ArrayList<>();
        // 当前占位符 0：'$'字符在XWPFParagraph集合中下标
        //          1：'$'字符在XWPFRun集合中下标
        //          2：'$'字符在text.toCharArray()数组下标
        //          3: '}'字符在XWPFParagraph集合中下标
        //          4: '}'字符在XWPFRun集合中下标
        //          5：'}'字符在text.toCharArray()数组下标
        int[] currentPlaceholder = new int[6];

        // 当前标识
        int modeTg = ignoreTg;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paragraphList.size(); i++) {
            XWPFParagraph p = paragraphList.get(i);
            List<XWPFRun> runs = p.getRuns();
            for (int j = 0; j < runs.size(); j++) {
                XWPFRun run = runs.get(j);
                String text = run.getText(run.getTextPosition());
                char[] textChars = text.toCharArray();
                String newVal = "";
                StringBuilder textSofar = new StringBuilder();
                for (int m = 0; m < textChars.length; m++) {
                    char c = textChars[m];
                    textSofar.append(c);
                    switch (c) {
                        case '$': {
                            modeTg = startTg;
                            sb.append(c);
                        }
                        break;
                        case '{': {
                            if (modeTg == startTg) {
                                sb.append(c);
                                modeTg = readTg;
                                currentPlaceholder[0] = i;
                                currentPlaceholder[1] = j;
                                currentPlaceholder[2] = m - 1;
                            } else {
                                if (modeTg == readTg) {
                                    sb = new StringBuilder();
                                    modeTg = ignoreTg;
                                }
                            }
                        }
                        break;
                        case '}': {
                            if (modeTg == readTg) {
                                modeTg = ignoreTg;
                                sb.append(c);
                                String val = map.get(sb.toString());
                                if (ObjectUtil.isNotEmpty(val)) {
                                    newVal += textSofar.toString() + val;
                                    placeholderList.add(currentPlaceholder);
                                    textSofar = new StringBuilder();
                                }
                                currentPlaceholder[3] = i;
                                currentPlaceholder[4] = j;
                                currentPlaceholder[5] = m;
                                currentPlaceholder = new int[6];
                                sb = new StringBuilder();
                            } else if (modeTg == startTg) {
                                modeTg = ignoreTg;
                                sb = new StringBuilder();
                            }
                        }
                        default: {
                            if (modeTg == readTg) {
                                sb.append(c);
                            } else if (modeTg == startTg) {
                                modeTg = ignoreTg;
                                sb = new StringBuilder();
                            }
                        }
                    }
                }
                newVal += textSofar.toString();
                run.setTextPosition(0);
                run.setText(newVal, run.getTextPosition());
            }
        }
        return placeholderList;
    }
}
