package com.empresa.transacciones.app.utils.commons;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HashUtil {

    private HashUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String generarHash(String pan) {
        try {
            String fecha = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); //
            String input = pan + fecha;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();

        } catch (Exception e) {
            throw new IllegalArgumentException("Error al generar hash", e);
        }
    }
    
    
    public static String maskPan(String pan) {
        return pan.replaceAll("(\\d{6})\\d+(\\d{4})", "$1****$2");
    }
}
