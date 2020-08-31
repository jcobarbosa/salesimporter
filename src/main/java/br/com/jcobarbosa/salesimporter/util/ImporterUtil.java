package br.com.jcobarbosa.salesimporter.util;

import java.math.BigDecimal;

public class ImporterUtil {

    private ImporterUtil() {
        throw new UnsupportedOperationException();
    }

    public static String getStringFromPosition(String[] content, int position) {
        try {
            return content[position].trim();
        } catch (Exception e) {
            return null;
        }
    }

    public static Long getLongFromPosition(String[] content, int position) {
        try {
            return Long.valueOf(content[position].trim());
        } catch (Exception e) {
            return null;
        }
    }

    public static BigDecimal getBigDecimalFromPosition(String[] content, int position) {
        try {
            return BigDecimal.valueOf(Double.valueOf(content[position].trim()));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
