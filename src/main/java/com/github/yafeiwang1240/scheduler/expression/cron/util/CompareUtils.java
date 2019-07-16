package com.github.yafeiwang1240.scheduler.expression.cron.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数组处理工具
 */
public class CompareUtils {

    /**
     * 借助set去重
     * @param list
     * @param <T>
     */
    public static <T> void removeDuplicate(List<T> list) {
        Set<T> set = new HashSet<>();
        list.forEach(set::add);
        list.clear();
        list.addAll(set);
    }

    /**
     * 数值区间有效性判定
     * @param left
     * @param right
     * @param value
     * @return
     */
    public static boolean assertRange(int left, int right, int value) {
        if (value > right || value < left) {
            throw new IllegalArgumentException("无效的数值: "
                    + value + "is not int [" + left + ", " + right + "]");
        }
        return true;
    }

    /**
     * 数值区间有效性判定
     * @param left
     * @param right
     * @return
     */
    public static boolean assertRange(int left, int right) {
        if (left > right) {
            throw new IllegalArgumentException("无效的数值区间: "
                    + "[" + left + ", " + right + "]");
        }
        return true;
    }

    public static int compare(int var1, int var2) {
        return var1 - var2;
    }
}
