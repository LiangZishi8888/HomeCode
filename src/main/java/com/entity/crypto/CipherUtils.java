package com.entity.crypto;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

@Slf4j
public class CipherUtils {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String ENCODING = "UTF-8";
    public static final String ALGORITHM_NAME = "SM4";
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
    public static final String key = "70ed3a57ab3857c5fd70041745f9674b";
    public static final byte[] keyBytes = ByteUtils.fromHexString(key);
    private static final Key sm4Key = new SecretKeySpec(keyBytes, ALGORITHM_NAME);


    private static byte[] handleEcbPadding(byte[] data, int mode) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING, BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(mode, sm4Key);
        return cipher.doFinal(data);
    }

    public static String decryptWithSysKey(String cipherText) {
        String decryptStr = "";
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        byte[] srcData;
        try {
            srcData = handleEcbPadding(cipherData, Cipher.DECRYPT_MODE);
            decryptStr = new String(srcData, ENCODING);
        } catch (Exception e) {
            log.error("decrypt key failed", e);
        }
        return decryptStr;
    }

    public static String encryptWithSysKey(String sourceStr) {
        String decryptStr="";
        try {
            byte[] srcData = sourceStr.getBytes(ENCODING);
            byte[] cipherData = handleEcbPadding(srcData, Cipher.ENCRYPT_MODE);
             decryptStr = ByteUtils.toHexString(cipherData);
        } catch (Exception e) {
            log.error("encrypt key failed", e);
        }
        return decryptStr;
    }
}
