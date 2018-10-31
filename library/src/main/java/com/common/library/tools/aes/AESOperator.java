package com.common.library.tools.aes;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 */
public class AESOperator {

    public static final String AES_MODEL = "AES";
    public static final String AES_ENCRYPT_MODEL = "AES/CBC/PKCS5Padding";
    public static final String ENCODE_UTF8 = "utf-8";
    public static final String PASSWORD = "diyueapk20181030";

    private AESOperator() {

    }

    /**
     * AES加密
     *
     * @param content  需要加密的内容
     * @param password 密码
     * @param vector   偏移量
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String password, String vector) throws Exception {
        if (password == null) {
            throw new RuntimeException("密码不能为null");
        }
        if (password.length() != 16) {
            throw new RuntimeException("密码必须为16位");
        }
        Cipher cipher = Cipher.getInstance(AES_ENCRYPT_MODEL);
        byte[] raw = password.getBytes(ENCODE_UTF8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES_MODEL);
        IvParameterSpec iv = new IvParameterSpec(vector.getBytes(ENCODE_UTF8));// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes(ENCODE_UTF8));
        return Base64.encode(encrypted);// 此处使用BASE64做转码。
    }

    /**
     * AES解密
     *
     * @param encrypted 需要解密的内容
     * @param password  密码
     * @param vector    偏移量
     * @return
     * @throws Exception
     */
    public static String decrypt(String encrypted, String password, String vector) throws Exception {
        if (password == null) {
            throw new RuntimeException("密码不能为null");
        }
        if (password.length() != 16) {
            throw new RuntimeException("密码必须为16位");
        }
        byte[] raw = password.getBytes(ENCODE_UTF8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES_MODEL);
        Cipher cipher = Cipher.getInstance(AES_ENCRYPT_MODEL);
        IvParameterSpec iv = new IvParameterSpec(vector.getBytes(ENCODE_UTF8));
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] encrypted1 = Base64.decode(encrypted);// 先用base64解密
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original, ENCODE_UTF8);
        return originalString;
    }

    public static void main(String[] args) throws Exception {
        // 需要加密的字串
//        String cSrc = "中国[{\"request_no\":\"1001\",\"service_code\":\"FS0001\",\"contract_id\":\"100002\",\"order_id\":\"0\",\"phone_id\":\"13913996922\",\"plat_offer_id\":\"100094\",\"channel_id\":\"1\",\"activity_id\":\"100045\"}]";
        String cSrc = "4pFKt2Z861CV8C4V2EQ9zFJ1t8yi9Mg60JejD1iG13P6VRTTw9h06AFBB8mR SQl6cWcomUiqbwMRXV14AVGj7gNAI1T1WQrrOX3IF+TgiwD7Lv2MruADQSsa U2wOiMXLGLy8RAzH3b94TRSw1BSnrjgmyUj9lxxipdCcMq1zfiCh4MdsyH0G OCXP0qqPPZG7o/JAn4CbN6TT63G3AsPErHv6g0yKTG0j62jFU6ZmhW7SiAeM c6n9ZoP0iqnhbbByNLaUW3ytuJn2E2D3yLDYYfm44iBQOk4MxHV1YBQ6AIat kKzuOc08kjHh+I3mJicLpr1DwRRp8ktJneGPxS2I9Q==\n";
//        String r1=AESOperator.encrypt(cSrc, "1234567891234567", "1234567891234567");
//        System.out.println(r1);
        String r2 = AESOperator.decrypt(cSrc, "1234567891234567", "1234567891234567");
//        System.out.println("原文:"+cSrc);
        System.out.println("解密:" + r2);
    }

}