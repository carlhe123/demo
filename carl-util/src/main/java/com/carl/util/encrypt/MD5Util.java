package com.carl.util.encrypt;

import cn.hutool.core.util.HexUtil;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

/**
  * @author Carl.He
  * @date 2020/7/3 15:45
  */
public class MD5Util {

    /**
    　* @Description: 这里主要是遍历8个byte，转化为16位进制的字符，即0-F
    　* @author Carl.He
    　* @date 2020/7/3 15:45
    　*/
    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    /**
    　* @Description: 这里是针对单个byte，256的byte通过16拆分为d1和d2
    　* @return java.lang.String
    　* @author Carl.He
    　* @date 2020/7/3 15:49
    　*/
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 返回大写MD5
     *
     * @param origin
     * @param charsetname
     * @return
     */
    private static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            }
            else{
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        } catch (Exception exception) {
        }
        return resultString.toUpperCase();
    }

    public static String MD5EncodeUtf8(String origin) {
        String salt = "CARL@2020";
        origin = origin + salt;
        return MD5Encode(origin, "utf-8");
    }


    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static void main(String[] args) {
        byte[] a = new byte[] { 106, 70, -110, 125, 39, -20, 52, 56, 85, 9, -19, -72, 52, -53, 52, -45, -6, 119, -63,
                30, 20, -83, -28, 77, 98, 109, -32, -76, 121, -106, 0, -74, -107, -114, -45, 104, -104, -8, 2, 121, 6,
                97, -18, -13, -63, -30, -125, -103, -80, -46, 113, -14, 68, 32, -46, 101, -116, -104, -81, -108, 122,
                89, -106, -109 };
        System.out.println(new BigInteger(1, a).toString(16));
        String hexResult = byteArrayToHexString(a);
        byte[] decodeHexResult = HexUtil.decodeHex(hexResult);
        System.out.println(Arrays.toString(decodeHexResult));
        System.out.println(hexResult);

    }
}
