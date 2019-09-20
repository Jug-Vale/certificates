package org.jugvale.certificate.generator.fetcher.impl.csv;

import java.util.function.Function;

public class CSVUtilities {
    
    private static final String SEPARATOR = ",";

    private CSVUtilities() {}
    
    public static String[] columns(String line) {
        return line.split(SEPARATOR);
    }
    
    public static boolean getBooleanValue(int index, String columns[]) {
        return getValue(index, columns, Boolean::parseBoolean);
    }
    
    public static String getStringValue(int index, String columns[]) {
        return getValue(index, columns, s -> s);
    }
    
    public static  Long getLongValue(int index, String columns[]) {
        return getValue(index, columns, Long::parseLong);
    }
    
    public static  <T> T getValue(int index, String columns[], Function<String, T> parseFunction) {
        String str = columns[index].trim();
        if (str.startsWith("\"")) {
            str = str.substring(1, str.length() - 1);
        }
        return parseFunction.apply(str);
    }

}
