package com.mfs.sms.utils;

import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.util.Base64Utils;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class CryptUtil {
    private static final String DESKEY = "19980621";
    private CryptUtil(){}

    public static String getMessageDigestByMD5(String text){
        try {
            byte[] digest = MessageDigest.getInstance("md5").digest(text.getBytes());
            return Base64Utils.encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String encryptByDES(String info) {
        try {
            SecretKey secretKey = new SecretKeySpec(DESKEY.getBytes(),"DES");
            SecureRandom secureRandom = new SecureRandom();
            Cipher des = Cipher.getInstance("DES");
            des.init(Cipher.ENCRYPT_MODE,secretKey,secureRandom);
            byte[] bytes = des.doFinal(info.getBytes());
            String s = HexUtils.toHexString(bytes);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptByDES(String encode) {
        try {
            SecretKey secretKey = new SecretKeySpec(DESKEY.getBytes(),"DES");
            SecureRandom secureRandom = new SecureRandom();
            Cipher des = Cipher.getInstance("DES");
            des.init(Cipher.DECRYPT_MODE,secretKey,secureRandom);
            byte[] bytes = des.doFinal(HexUtils.fromHexString(encode));
            String s = new String(bytes);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
