package me.jeremy.ccst.model;

/**
 * Created by qiugang on 2014/9/21.
 */
public enum Category {

    latest("最新问卷"), Category("问卷分类"), Settings("设置");
    private String mDisplayName;

    Category(String displayName) {
        mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }
}
