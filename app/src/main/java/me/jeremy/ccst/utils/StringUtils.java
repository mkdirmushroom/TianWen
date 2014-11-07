package me.jeremy.ccst.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qiugang on 14/10/30.
 */
public class StringUtils {
    /*
    Analyzing input is numeric or string
     */
    public static boolean isString(String s) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher((CharSequence) s);
        return matcher.matches();
    }
}
