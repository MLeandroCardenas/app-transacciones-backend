package com.empresa.transacciones.app.utils.commons;

import java.security.SecureRandom;
public class RandomUtilGenerator {

    private RandomUtilGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public static int generarNumero() {
        return new SecureRandom().nextInt(1,101);
    }
}
