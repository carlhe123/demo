package com.carl.util.common;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EmailCheckUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailCheckUtil.class);

    public static boolean checkEmail(String toMail, String domain) {

        if (!toMail.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            return false;
        }
        String hostName = toMail.split("@")[1];// 去掉@后面的
        logger.info("hostName:" + hostName);
        Record[] result = null;
        Socket socket = new Socket();
        try {
            // 查找MX记录
            Lookup lookup = new Lookup(hostName, Type.MX);
            lookup.run();
            if (lookup.getResult() != Lookup.SUCCESSFUL) {
                logger.info("找不到MX记录");
                return false;
            } else {
                result = lookup.getAnswers();
                logger.info("answers:{}", JSON.toJSONString(result));
                int i = 1;
                for (Record rec : result) {
                    logger.info("解析出ip地址{}>>>>>{}",i++,rec.rdataToString());
                }
            }
            // 连接到邮箱服务器
            String mxHost = ((MXRecord) result[0]).getTarget().toString();
            if (result.length > 1) { // 优先级排序
                List<Record> arrRecords = new ArrayList<Record>();
                Collections.addAll(arrRecords, result);
                Collections.sort(arrRecords, new Comparator<Record>() {
                    public int compare(Record o1, Record o2) {
                        return new CompareToBuilder().append(((MXRecord) o1).getPriority(), ((MXRecord) o2).getPriority()).toComparison();
                    }
                });
                mxHost = ((MXRecord) arrRecords.get(0)).getTarget().toString();
            }
            logger.info("mxHost:{}",mxHost);
            logger.info("开始连接服务器");
            socket.connect(new InetSocketAddress(mxHost, 25));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream())));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // 超时时间(毫秒)
            long timeout = 6000;
            // 睡眠时间片段(50毫秒)
            int sleepSect = 1000;

            // 连接(服务器是否就绪)
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 220) {
                logger.info("连接服务器失败");
                return false;
            }

            logger.info("连接服务器成功");
            // 握手
            bufferedWriter.write("HELO " + domain + "\r\n");
            bufferedWriter.flush();
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
                return false;
            }
            logger.info("开始验证身份 domain:{}",domain);
            // 身份
            bufferedWriter.write("MAIL FROM: <check@" + domain + ">\r\n");
            bufferedWriter.flush();
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
                logger.info("验证身份失败");
                return false;
            }
            logger.info("验证身份成功");
            logger.info("开始验证发送邮件 toMail:{}",toMail);
            // 验证
            bufferedWriter.write("RCPT TO: <" + toMail + ">\r\n");
            bufferedWriter.flush();
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
                logger.info("验证发送邮件失败");
                return false;
            }
            // 断开
            bufferedWriter.write("QUIT\r\n");
            bufferedWriter.flush();
            return true;
        } catch (Exception e) {
            logger.info("checkEmail_error ex={}",e.toString(),e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

    private static int getResponseCode(long timeout, int sleepSect, BufferedReader bufferedReader) throws InterruptedException, NumberFormatException, IOException {
        int code = 0;
        for (long i = sleepSect; i < timeout; i += sleepSect) {
            Thread.sleep(sleepSect);
            if (bufferedReader.ready()) {
                String outline = bufferedReader.readLine();
                logger.info(outline);
                code = Integer.parseInt(outline.substring(0, 3));
                break;
            }
        }
        return code;
    }

    public static void main(String[] args) {
        System.out.println(checkEmail("yinruyue@evergrande.com","hdfax.com"));
//        System.out.println(checkEmail("wangxianhe@evergrande.com","hdfax.com"));
//        System.out.println(checkEmail("lilei1031@evergrande.com","hdfax.com"));
//        System.out.println(checkEmail("menghong@evergrande.com","hdfax.com"));
//        System.out.println(checkEmail("hebiao@hdfax.com","hdfax.com"));
//        System.out.println(checkEmail("lianghougao@hdfax.com","hdfax.com"));
//        System.out.println(checkEmail("wangxianhe@evergrande.com","evergrande.com"));
//        System.out.println(checkEmail("lilei1031@evergrande.com","evergrande.com"));
//        System.out.println(checkEmail("menghong@evergrande.com","evergrande.com"));
//        System.out.println(checkEmail("yinruyue@evergrande.com","evergrande.com"));
//        System.out.println(checkEmail("hebiao@hdfax.com","evergrande.com"));
//        System.out.println(checkEmail("hebiao@hdfax.com","hdfax.com"));
//        System.out.println(checkEmail("carl_hb@163.com","evergrande.com"));
//        System.out.println(checkEmail("carl_hb@163.com","163.com"));
//        System.out.println(checkEmail("carl_hb@163.com","hdfax.com"));
//        System.out.println(checkEmail("yinruyue@evergrande.com","163.com"));
    }
}
