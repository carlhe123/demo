package com.carl.util.common;

import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;

public class MailUtilTest {

    public static void main(String[] args) {
        try {
            String s = "100135邓迪江2.客户姓名变更说明-（银川）.docx";
            String name = MimeUtility.encodeText(s);
            String javaCharset = MimeUtility.getDefaultJavaCharset();
            System.out.println(name);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
