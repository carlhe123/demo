package com.carl.demo.controller;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.http.HttpUtil;
import com.carl.demo.exception.BusinessException;
import com.carl.demo.util.HdfsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@Slf4j
@RequestMapping("/hadoop")
public class HadoopTestController {

    @Autowired
    HdfsClient hdfsClient;

    @RequestMapping(value = "/downloadTest",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> downloadTest(){
        ResponseEntity<byte[]> result = null;
        try {
            String path = "/caifu/cache/electronic/contract/2019/07/23/f68de6da10fa254c885ffeec044a46d30723195441.pdf";
            String str = Base64Encoder.encode(hdfsClient.downloadFile(path));
            HttpHeaders heads = new HttpHeaders();
            heads.add("Content-Disposition", "attachment;filename=test.pdf");
            heads.add("Content-Type", "application/pdf");
            result = new ResponseEntity(Base64Decoder.decode(str), heads, HttpStatus.OK);
        }catch (BusinessException ex){
            log.error("测试下载文件出现异常:{} {}",ex.getMessage(),ex.getStackTrace());
        }
        return result;
    }

    @RequestMapping(value = "/uploadTest",method = RequestMethod.GET)
    @ResponseBody
    public void uploadTest(File file){
        try {
            String path = "/caifu/cache/electronic/contract/2019/07/23/f68de6da10fa254c885ffeec044a46d30723195441.pdf";
            hdfsClient.uploadFile(path,file);
        }catch (Exception ex){
            log.error("测试上传文件出现异常:{} {}",ex.getMessage(),ex.getStackTrace());
        }
    }

    @RequestMapping(value = "/viewTest",method = RequestMethod.GET)
    public void viewTest(HttpServletResponse response){
        try {
            String path = "/caifu/cache/electronic/contract/2019/07/23/f68de6da10fa254c885ffeec044a46d30723195441.pdf";
            String str = Base64Encoder.encode(hdfsClient.downloadFile(path));
            flushStream(response, str);
        }catch (BusinessException ex){
            log.error("测试预览出现异常:{} {}",ex.getMessage(),ex.getStackTrace());
        }
    }

    /**
     * 功能描述: 将流输出到前端
     *
     * @param response
     * @param str base64编码的字符串
     * @Return: void
     * @Author: dengshuai
     * @Date: 2019/8/9 9:20
     */
    private void flushStream(HttpServletResponse response, String str) {
        // 将base64进行解码
        byte[] decode = Base64Decoder.decode(str);
        response.setContentType("application/pdf");
        InputStream in = null;
        OutputStream out = null;
        try {
            // 将转码的字节数组转换成流
            in = new ByteArrayInputStream(decode);
            out = response.getOutputStream();
            byte[] b = new byte[512];
            while ((in.read(b))!=-1) {
                out.write(b);
            }
            out.flush();
        } catch (Exception e) {
            log.error(e.toString(),e);
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
