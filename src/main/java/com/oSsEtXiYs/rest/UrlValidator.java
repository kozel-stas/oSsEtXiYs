package com.oSsEtXiYs.rest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class UrlValidator implements ConstraintValidator<UrlValid, URL> {

    private static final Set<String> SUPPORTED_PROTOCOLS = new HashSet<>();

    static {
        SUPPORTED_PROTOCOLS.add("http");
        SUPPORTED_PROTOCOLS.add("https");
    }

    @Override
    public boolean isValid(URL url, ConstraintValidatorContext constraintValidatorContext) {
        return SUPPORTED_PROTOCOLS.contains(url.getProtocol());
    }

}
