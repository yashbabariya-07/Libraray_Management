package com.lbm.utils;

import java.util.regex.Pattern;

public class SearchUtil {
    public static String searchData(String input){
        String data = input.replaceAll("\\s+", "").toLowerCase();
        StringBuilder patternBuilder = new StringBuilder();

        for (char c : data.toCharArray()) {
            patternBuilder.append("(?=.*").append(Pattern.quote(String.valueOf(c))).append(")");
        }
        return patternBuilder.append(".*").toString();
    }
}
