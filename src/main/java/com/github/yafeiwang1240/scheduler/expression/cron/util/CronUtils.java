package com.github.yafeiwang1240.scheduler.expression.cron.util;

import com.github.yafeiwang1240.obrien.lang.Lists;
import com.github.yafeiwang1240.scheduler.expression.cron.analysis.CronArray;
import com.github.yafeiwang1240.scheduler.expression.cron.analysis.CronField;

import java.util.List;

/**
 * cron解析工具类
 */
public class CronUtils {

    /**
     * Cron切割
     * @param cron
     * @return
     */
    public static List<String> cut(String cron) {
        cron.trim();
        String[] array = cron.replaceAll("\\s+ ", " ").split("\\s");
        return Lists.asList(array);
    }

    public static List<CronField> convertCron(String cron) {
        List<String> cut = cut(cron);
        int size = cut.size();
        if (size != 6 && size != 7) {
            throw new IllegalArgumentException("cron 表达式域为6个或着7个（最后一个为年）");
        }

        // 加上年做get next算法
        if (size == 6) {
            cut.add("*");
        }

        size = cut.size();
        List<CronField> cronFields = Lists.newArrayList(size);
        for (int i = 0; i < size; i++) {
            CronArray cronArray = CronArray.getCronArray(i);
            cronFields.add(new CronField(cronArray,
                    CronShapingUtils.shaping(cut.get(i), cronArray)));
        }
        return cronFields;
    }
}
