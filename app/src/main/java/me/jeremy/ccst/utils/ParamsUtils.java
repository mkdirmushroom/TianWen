package me.jeremy.ccst.utils;

import me.jeremy.ccst.api.TypeParams;

/**
 * Created by qiugang on 2014/10/17.
 */
public class ParamsUtils {

    public static String getQuestionTypeParams(String type) {
       if (type.equals(TypeParams.QUESTION_CHOOSE_SINGEL)) {
           return "(单选题)";
       } else if (type.equals(TypeParams.QUESTION_CHOOSE_MULTI)) {
           return "(多选题)";
       } else {
           return "(填空题)";
       }
    }

    public static String getQuestionNums(int num) {
        return "共" + num + "题";
    }

}
