package com.gehtsoft.crypto.signature;

import java.security.*;

/**
 * Created by dkuzmin on 8/9/2016.
 */
public class SHA256withRSA implements ISignature {

    @Override
    public String sign(String text) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(text.getBytes());
        byte[] signatureBytes = signature.sign();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < signatureBytes.length; i++) {
            stringBuffer.append(Integer.toString((signatureBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }
}
