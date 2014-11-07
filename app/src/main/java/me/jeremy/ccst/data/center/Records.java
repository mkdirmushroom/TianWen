package me.jeremy.ccst.data.center;

import java.util.HashMap;
import java.util.List;

/**
 * Created by qiugang on 2014/10/7.
 */
public class Records {

    private final static HashMap<Integer, List<Integer>> dataCenter = new HashMap<Integer, List<Integer>>();

    private final static HashMap<Integer, String> stringDataCenter = new HashMap<Integer, String>();
    
    public static HashMap<Integer, List<Integer>> getDataCenter() {
        return dataCenter;
    }

    public static HashMap<Integer, String> getStringDataCenter() {
        return stringDataCenter;
    }

    public static boolean queryInt(Integer integer) {
        return getStringDataCenter().containsKey(integer);
    }


    public static boolean queryInt(List<Integer> records,Integer integer) {
        return records.contains(integer);
    }

    public static boolean deleteInt(List<Integer> records, Integer integer) {
       return records.remove(integer);
    }

    public static boolean addInt(List<Integer> records, Integer integer) {
        return  records.add(integer);
    }

}
