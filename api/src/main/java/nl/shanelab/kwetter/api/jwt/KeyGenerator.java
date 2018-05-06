package nl.shanelab.kwetter.api.jwt;

import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;

public interface KeyGenerator {

    Key generate();

    SignatureAlgorithm getAlgorithm();
}
