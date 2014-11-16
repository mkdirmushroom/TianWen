package me.jeremy.ccst.utils;

import me.jeremy.ccst.R;
import me.jeremy.ccst.api.TypeParams;

/**
 * Created by qiugang on 2014/10/17.
 */
public class ParamsUtils {
    public static int[] colors = {R.color.color1, R.color.color2, R.color.color3,
            R.color.color4, R.color.color5, R.color.color6, R.color.color7,
            R.color.color8, R.color.color9, R.color.color10};

    public static int[] skins = {R.drawable.skin, R.drawable.skin2, R.drawable.skin3,
            R.drawable.skin4};

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

    public static String getCreateTime(String string) {
        String[] times = string.split("-");
        return times[1] + "月" + times[2] + "日";
    }
}
