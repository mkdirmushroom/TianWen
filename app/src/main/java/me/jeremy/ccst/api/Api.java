package me.jeremy.ccst.api;


/**
 * Created by admin on 2014/9/25.
 */
public class Api {
    //http://115.29.208.47:8080/tianwen/s

    public static String Host_LOCAL = "http://192.168.112.122:8080/tianwen/";

    public static String Host_ALIYUN = "http://115.29.208.66:8080/tianwen/";

    public static String Host_ALIYUN_SLAVE = "http://115.29.208.47:8080/tianwen/";

    public static String[] HOSTS = {Host_ALIYUN, Host_ALIYUN_SLAVE};

    public static int HOST_POSITION = 0;

    public static String NEWS = "news/";

    public static String DETAIL = "detail/";

    public static String QUESTIONARIES = "questionnaires/";

    public static String POSTDATA = "doquestionnaire";

    public static String CATEGORY = "CategoryQnSearch/";

    public static String UPDATEUSER = "updateuser";

    public static String getApiParams(int page, int pageSize) {
       return page + "/" + pageSize;
    }

    public static String getApiParams(int userId, int page, int pageSize) {
        return userId + "/" + page + "/" + pageSize;
    }


    public static String getApiParams(int userId, int categoryId, int page, int pageSize) {
        return userId + "/" + categoryId + "/" + page + "/" + pageSize;
    }

}
