package com.github.catvod.utils;
import android.util.Base64;
import com.github.catvod.crawler.SpiderDebug;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.zip.GZIPInputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Misc {

    public static Charset CharsetUTF8 = Charset.forName("UTF-8");

    /***
     * DES加密 1 加密
     * @param key
     * @param str
     * @return
     */
    public static String DESEncrypt(String key,String str)
    {
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key.getBytes()));
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return ByteToStr(cipher.doFinal(str.getBytes(Misc.CharsetUTF8)));
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    public static String ByteToStr(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length);
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() < 2) {
                stringBuffer.append(0);
            }
            stringBuffer.append(hexString.toUpperCase());
        }
        return stringBuffer.toString().toLowerCase();
    }

    /***
     * AES解密 2 解密
     * @param key
     * @param str
     * @return
     */
    public static String DESDecrypt(String key,String str)
    {
        try {
            SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key.getBytes()));
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(DESToByte(str)),Misc.CharsetUTF8);
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    public static byte[] DESToByte(String str) {
        if (str.length() % 2 == 0) {
            char[] charArray = str.toCharArray();
            byte[] bArr = new byte[str.length() / 2];
            int length = str.length();
            int i = 0;
            int j = 0;
            while (i < length) {
                StringBuilder sb = new StringBuilder();
                sb.append(charArray[i]);
                sb.append(charArray[i + 1]);
                bArr[j] = Integer.valueOf(Integer.parseInt(sb.toString(), 16) & 255).byteValue();
                i += 2;
                j++;
            }
            return bArr;
        }
        throw new IllegalArgumentException("invalid hex string");
    }


    /***
     * AES加密 1 加密
     * @param key
     * @param iv
     * @param str
     * @return
     */
    public static String AESEncrypt(String key,String iv,String str)
    {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv.getBytes()));
            return Base64.encodeToString(cipher.doFinal(str.getBytes()), Base64.NO_WRAP);
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    /***
     * AES解密 2 解密
     * @param key
     * @param iv
     * @param str
     * @return
     */
    public static String AESDecrypt(String key,String iv,String str)
    {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv.getBytes()));
            return new String(cipher.doFinal(Base64.decode(str, Base64.DEFAULT)), "UTF-8");
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    /***
     * RSA加密 1 加密
     * @param key
     * @param str
     * @return
     */
    public static String RSAEncrypt(String key,String str) {
        try {
            PrivateKey generatePrivate = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(key, Base64.DEFAULT)));
            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, generatePrivate);
            byte[] inData = str.getBytes();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (inData.length <= 256) {
                outputStream.write(cipher.doFinal(inData));
            } else {
                for (int i = 0; i < inData.length; i += 256) {
                    outputStream.write(cipher.doFinal(inData, i, 256));
                }
            }
            outputStream.flush();
            String result = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
            outputStream.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /***
     * RSA解密 2 解密
     * @param key
     * @param str
     * @return
     */
    public static String RSADecrypt(String key,String str) {
        try {
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(key, Base64.DEFAULT)));
            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            byte[] inData = Base64.decode(str, Base64.DEFAULT);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (inData.length <= 256) {
                outputStream.write(cipher.doFinal(inData));
            } else {
                for (int i = 0; i < inData.length; i += 256) {
                    outputStream.write(cipher.doFinal(inData, i, 256));
                }
            }
            outputStream.flush();
            String result = new String(outputStream.toByteArray(), Misc.CharsetUTF8);
            outputStream.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] CBC( byte[] key, byte[] iv ,byte[] str) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(str);
        } catch (Exception e) {
            SpiderDebug.log(e);
            return null;
        }
    }

    public static String GZip(byte[] str) {
        try {
            GZIPInputStream gZIPInputStream = new GZIPInputStream(new ByteArrayInputStream(str));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr2 = new byte[1024];
            while (true) {
                int read = gZIPInputStream.read(bArr2);
                if (read == -1) {
                    return byteArrayOutputStream.toString("UTF-8");
                }
                byteArrayOutputStream.write(bArr2, 0, read);
            }
        }catch (Exception e){
        }
        return null;
    }

}
