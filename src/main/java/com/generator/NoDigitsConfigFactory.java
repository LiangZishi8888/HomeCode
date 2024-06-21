package com.generator;

import com.generator.impl.AuthAssoNoGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * a configuration class to store different bussinessNo digits count with unique prefix
 * if me want modify digits count,we dont need modify each order generater
 * we just modifiy here
 *
 * @see AuthAssoNoGenerator
 */
@Slf4j
public class NoDigitsConfigFactory {

    public static final String AUTH_ASSO_PREFIX = "authAsso";

    private static Map<String, Integer> prefixBounding = new HashMap<>();

    static {
        // we set 4 for demostration
        prefixBounding.put(AUTH_ASSO_PREFIX, 4);
    }

    public static int getDigits(String prefix) {
        Integer digits = prefixBounding.get(prefix);
        if (Objects.isNull(digits)) {
            log.error("prefix is not exists: ", prefix);
            throw new RuntimeException("inital generator failed");
        }
        return digits;
    }
}
