import org.apache.tomcat.util.codec.binary.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;


/*
 * 生成密钥对、生成证书、查看证书的辅助小程序
 * */
public class Main {
    public static void main(String[] args) {
        //生成密钥对
        //generatorKey();

        //生成证书
        //createAuthority();

        //查看证书内容
        showAuthorityContent();
    }

    private static void createAuthority() {
        File file = new File("E:/images/sms/security/privateKey.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuffer sb = new StringBuffer();
            String str = null;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(sb.toString())));
            encodeByRSAPrivateKey("MFS##" +  new Date().getTime() +"##admin",privateKey);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static void encodeByRSAPrivateKey(String content,PrivateKey privateKey) {
        try {
            Cipher rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.ENCRYPT_MODE,privateKey,new SecureRandom());
            byte[] code = rsa.doFinal(content.getBytes());
            String s = Base64.encodeBase64URLSafeString(code);
            BufferedWriter br = new BufferedWriter(new FileWriter(new File("E:/images/sms/security/证书.txt")));
            br.write(s);
            br.close();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showAuthorityContent(){
        File file = new File("E:/images/sms/security/publicKey.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuffer sb = new StringBuffer();
            String str = null;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(sb.toString())));

            file = new File("E:/images/sms/security/证书.txt");
            br = new BufferedReader(new FileReader(file));
            sb = new StringBuffer();
            str = null;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
            System.out.println(decodeByRSAPublicKey(sb.toString(),publicKey));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static String decodeByRSAPublicKey(String code,PublicKey publicKey) {
        String content = null;
        try {
            Cipher rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.DECRYPT_MODE,publicKey,new SecureRandom());
            byte[] res = rsa.doFinal(Base64.decodeBase64(code));
            content = new String(res);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return content;
    }

    private static void generatorKey(){
        try {
            KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();
            File file = new File("E:/images/sms/security/privateKey.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(Base64.encodeBase64String(privateKey.getEncoded()));
            bw.close();
            file = new File("E:/images/sms/security/publicKey.txt");
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(Base64.encodeBase64String(publicKey.getEncoded()));
            bw.close();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
