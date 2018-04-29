package nl.shanelab.kwetter.util;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Sha256 {

    private static final String METHOD = "SHA-256";

    public static String hash(String input) {
        String result = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(METHOD);
            messageDigest.update(input.getBytes(Charset.forName("UTF-8")));
            byte[] hashBytes = messageDigest.digest();

            BigInteger bigInt = new BigInteger(1, hashBytes);
            result = bigInt.toString(16);
        } catch (NoSuchAlgorithmException ex) { }

        return result;
    }
}
