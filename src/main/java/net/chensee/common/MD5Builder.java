package net.chensee.common;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author : shibo
 * @date : 2019/6/5 9:19
 */
public class MD5Builder {
    static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public MD5Builder() {
    }

    public static String getMd5(File file) throws IOException, NoSuchAlgorithmException {
        try {
            FileInputStream fis = new FileInputStream(file);
            Throwable var2 = null;

            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] buffer = new byte[2048];
                boolean var5 = true;

                int length;
                while((length = fis.read(buffer)) != -1) {
                    md.update(buffer, 0, length);
                }

                byte[] b = md.digest();
                String var7 = byteToHexString(b);
                return var7;
            } catch (Throwable var17) {
                var2 = var17;
                throw var17;
            } finally {
                if (fis != null) {
                    if (var2 != null) {
                        try {
                            fis.close();
                        } catch (Throwable var16) {
                            var2.addSuppressed(var16);
                        }
                    } else {
                        fis.close();
                    }
                }

            }
        } catch (FileNotFoundException var19) {
            var19.printStackTrace();
            throw var19;
        }
    }

    public static String getMD5(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(message.getBytes("UTF-8"));
            return byteToHexString(b);
        } catch (NoSuchAlgorithmException var3) {
            var3.printStackTrace();
            throw var3;
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
            throw var4;
        }
    }

    private static String byteToHexString(byte[] tmp) {
        char[] str = new char[32];
        int k = 0;

        for(int i = 0; i < 16; ++i) {
            byte byte0 = tmp[i];
            str[k++] = hexDigits[byte0 >>> 4 & 15];
            str[k++] = hexDigits[byte0 & 15];
        }

        String s = new String(str);
        return s;
    }
}

