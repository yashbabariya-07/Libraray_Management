package com.lbm.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MongoErrorMessageUtil {
    public static String prepareMongoError(String rawMessage){
        if (rawMessage != null && rawMessage.contains("E11000 duplicate key error")) {
            String field = "field";
            String value = "value";

            Pattern pairPattern = Pattern.compile("dup key:\\s*\\{[^}]*?(\\w+):\\s*\\\\?\"([^\"]+)\"");
            Matcher matcher = pairPattern.matcher(rawMessage);

            if (matcher.find()) {
                field = matcher.group(1);
                value = matcher.group(2);
            }

            return String.format("%s '%s' is already registered.", field, value);
        }
        else {
            return "Something went wrong.";
        }
    }
}
