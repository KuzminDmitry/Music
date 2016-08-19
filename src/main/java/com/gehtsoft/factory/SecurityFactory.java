package com.gehtsoft.factory;

import com.gehtsoft.configProperties.ConfigProperties;
import com.gehtsoft.crypto.hash.IHash;
import com.gehtsoft.crypto.hash.SHA256;
import com.gehtsoft.crypto.signature.ISignature;
import com.gehtsoft.crypto.signature.SHA256withRSA;

/**
 * Created by dkuzmin on 8/11/2016.
 */
public class SecurityFactory {

    private static String tokenHashAlgorithm = ConfigProperties.getProperties().getProperty("token.signature.algorithm");
    private static String passwordHashAlgorithm = ConfigProperties.getProperties().getProperty("password.hash.algorithm");

    public static ISignature getTokenSignature() {
        if(tokenHashAlgorithm.equals("SHA256withRSA")) {
            return new SHA256withRSA();
        }
        return new SHA256withRSA();
    }

    public static IHash getPasswordHash() {
        if(passwordHashAlgorithm.equals("SHA256")) {
            return new SHA256();
        }
        return new SHA256();
    }
}
