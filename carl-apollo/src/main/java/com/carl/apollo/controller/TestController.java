package com.carl.apollo.controller;

import cn.hutool.core.io.FileUtil;
import com.carl.apollo.mapper.DemoMapper;
import com.carl.util.common.Docx4jWordUtil;
import com.carl.util.common.POIUtil;
import com.carl.util.common.PoiWordUtil;
import com.carl.util.encrypt.MD5Util;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Text;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.carl.util.common.PoiWordUtil.getPlaceholderList;

@RestController
public class TestController {

    @Value("${testValue}")
    private String testValue;
    @Value("${server.port}")
    private String port;
    @Value("${carl.testEnv}")
    private String testEnv;

    @Resource
    private DemoMapper demoMapper;

    @RequestMapping("/test")
    public String test(){
        String env = testEnv;
        String value = testValue;
        String port1 = port;
        System.out.println(port1 + "ENV:" + env);
        return value;
    }

    @RequestMapping("/mybatis-test")
    public String queryProductName(@RequestParam("id")Long id){
        String encrypt = MD5Util.MD5EncodeUtf8("123456");
        System.out.println(encrypt);
        BASE64Encoder encoder = new BASE64Encoder();
        String encodeString = encoder.encode("123456".getBytes());
        System.out.println(encodeString);
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] bytes = decoder.decodeBuffer(encodeString);
            String result = new String(bytes);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String productName = demoMapper.queryProductNameById(id);
        return productName;
    }

    @RequestMapping("/word-replace-docx4j")
    public void docx4jwordReplace(){
        String docxFile = TestController.class.getClassLoader().getResource("template").getPath();
        WordprocessingMLPackage template = null;
        try {
            template = WordprocessingMLPackage.load(new java.io.File(docxFile+"/old.docx"));
            List<Object> texts = Docx4jWordUtil.getAllElementFromObject(
                    template.getMainDocumentPart(), Text.class);
            Map<String, String> map = new HashMap<>();
            map.put("${name}", "恒大财富");
            map.put("${beforeName}", "恒大财富1");
            map.put("${idNo}", "4523156423156423");
            map.put("${productName}", "华付信息");
            map.put("${year}", "2020");
            map.put("${month}", "07");
            map.put("${day}", "13");
            map.put("${bankCardNo}", "1321654321316");
            map.put("${beforeBankCardNo}", "1321654321316123");
            Docx4jWordUtil.searchAndReplace(texts, map);
            template.save(new java.io.File(docxFile+"/new.docx"));
        } catch (Docx4JException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/word-replace-poi")
    public void poiWordReplace(){
        String docxFile = TestController.class.getClassLoader().getResource("template").getPath();
        BufferedOutputStream bos = null;
        XWPFDocument doc = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("${name}", "恒大财富");
            map.put("${beforeName}", "恒大财富1");
            map.put("${idNo}", "4523156423156423");
            map.put("${productName}", "华付信息");
            map.put("${year}", "2020");
            map.put("${month}", "07");
            map.put("${day}", "13");
            map.put("${bankCardNo}", "1321654321316");
            map.put("${beforeBankCardNo}", "1321654321316123");

            doc = new XWPFDocument(new FileInputStream(new java.io.File(docxFile+"/old.docx")));
            List<XWPFParagraph> paragraphList = new ArrayList(doc.getParagraphs());
            // 获取表格中的占位符信息
            //
            PoiWordUtil.getTableParagraphs(doc, paragraphList, map);
            //获取占位符，并且将占位符需要替换的值写入
            List<int[]> placeholderList = getPlaceholderList(paragraphList, map);
            //清除占位符信息
            PoiWordUtil.clearPlaceholder(placeholderList, paragraphList);
            bos = FileUtil.getOutputStream(new java.io.File(docxFile+"/new.docx"));
            doc.write(bos);
            bos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bos.close();
                doc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/word-output-poi")
    public String poiWordOutput() throws Exception {
        String docxFile = TestController.class.getClassLoader().getResource("template").getPath() + "/old.doc";
        String worldText = POIUtil.convertWordToHtml(docxFile);
        System.out.println(worldText);
        return worldText;

    }
}
