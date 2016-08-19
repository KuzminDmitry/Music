package com.gehtsoft.crypto.hash;

import java.security.MessageDigest;

/**
 * Created by dkuzmin on 8/11/2016.
 */
public class SHA256 implements IHash {

    @Override
    public String hash(String text) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(text.getBytes());
        byte byteData[] = messageDigest.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            stringBuffer.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }
}
