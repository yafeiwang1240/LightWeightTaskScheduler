package com.github.yafeiwang1240.scheduler.expression.cron.util;

import com.github.yafeiwang1240.obrien.lang.Maps;
import com.github.yafeiwang1240.scheduler.expression.cron.analysis.CronArray;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyafei
 */
public class CronShapingUtils {

    private static final Map<String, String> MONTH_MAP = Maps.create(HashMap::new);
    private static final Map<String, String> WEEK_MAP = Maps.create(HashMap::new);
    static {
        MONTH_MAP.put("JAN", "1");
        MONTH_MAP.put("FEB", "2");
        MONTH_MAP.put("MAR", "3");
        MONTH_MAP.put("ARP", "4");
        MONTH_MAP.put("MAY", "5");
        MONTH_MAP.put("JUN", "6");
        MONTH_MAP.put("JUL", "7");
        MONTH_MAP.put("AUG", "8");
        MONTH_MAP.put("SEP", "9");
        MONTH_MAP.put("OCT", "10");
        MONTH_MAP.put("NOV", "11");
        MONTH_MAP.put("DEC", "12");

        WEEK_MAP.put("SUN", "0");
        WEEK_MAP.put("MON", "1");
        WEEK_MAP.put("TUE", "2");
        WEEK_MAP.put("WED", "3");
        WEEK_MAP.put("THU", "4");
        WEEK_MAP.put("FRI", "5");
        WEEK_MAP.put("SAT", "6");
    }
    public static String shaping(String express, CronArray cronArray) {
        if (cronArray == CronArray.MONTH) {
            express = shapingMonth(express);
        }

        if (cronArray == CronArray.WEEK) {
            express = shapingWeek(express);
        }

        if (cronArray == CronArray.WEEK || cronArray == CronArray.DAY) {
            express = express.replace("?", "*");
        }

        return express;
    }

    private static String shapingMonth(String express) {
        for (Map.Entry<String, String> entry : MONTH_MAP.entrySet()) {
            express = express.toUpperCase().replace(entry.getKey(), entry.getValue());
        }
        return express;
    }

    private static String shapingWeek(String express) {
        for (Map.Entry<String, String> entry : WEEK_MAP.entrySet()) {
            express = express.toUpperCase().replace(entry.getKey(), entry.getValue());
        }
        return express;
    }
}
