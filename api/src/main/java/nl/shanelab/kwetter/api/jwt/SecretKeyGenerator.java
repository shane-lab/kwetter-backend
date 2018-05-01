package nl.shanelab.kwetter.api.jwt;

import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;

public class SecretKeyGenerator implements KeyGenerator {
    private static final String SECRET = "KweetsMaken";

    @Override
    public Key generate() {
        byte[] secret = SECRET.getBytes(Charset.forName("UTF-8"));

        return new SecretKeySpec(secret, 0, secret.length, "DES");
    }

    @Override
    public SignatureAlgorithm getAlgorithm() {
        return SignatureAlgorithm.ES256;
    }
}
