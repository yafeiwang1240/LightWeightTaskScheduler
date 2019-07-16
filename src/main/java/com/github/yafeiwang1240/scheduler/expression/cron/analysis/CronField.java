package com.github.yafeiwang1240.scheduler.expression.cron.analysis;

import com.github.yafeiwang1240.obrien.lang.Lists;
import com.github.yafeiwang1240.scheduler.expression.cron.util.CompareUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 域解析
 */
public class CronField {
    private static final String STAR = "*";
    private static final String COMMA = ",";
    private static final String HYPHEN = "-";
    private static final String SLASH = "/";

    private CronArray cronArray;
    private String express;
    private List<Integer> pointsCache = null;

    public CronField(CronArray cronArray, String express) {
        this.cronArray = cronArray;
        this.express = express;
        points();
    }

    public CronArray getCronArray() {
        return cronArray;
    }

    public void setCronArray(CronArray cronArray) {
        this.cronArray = cronArray;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    /**
     * 包含所有
     */
    public boolean containsAll() {
        return STAR.equals(getExpress());
    }

    /**
     * 计算域中点
     * @return
     */
    public synchronized List<Integer> points() {
        if(pointsCache != null) return pointsCache;
        pointsCache = Lists.create(ArrayList::new);
        String express = getExpress();
        CronArray cronArray = getCronArray();
        Integer min = cronArray.getMin();
        Integer max = cronArray.getMax();

        // *
        if (STAR.equals(express)) {
            for (int i = min; i <= max; i++) {
                pointsCache.add(i);
            }
            return pointsCache;
        }

        // ,
        if (express.contains(COMMA)) {
            String[] values = express.split(COMMA);
            for (String part : values) {
                pointsCache.addAll(new CronField(getCronArray(), part).points());
            }
            // 去重和排序
            if(pointsCache.size() > 1) {
                CompareUtils.removeDuplicate(pointsCache);
                Collections.sort(pointsCache);
            }
            return pointsCache;
        }

        // min-max/step统一处理
        Integer left;
        Integer right;
        Integer step = 1;
        if (express.contains(HYPHEN)) {
            String[] strings = express.split(HYPHEN);
            left = Integer.valueOf(strings[0]);
            CompareUtils.assertRange(cronArray.getMin(), cronArray.getMax(), left);

            // x-y/s
            if (strings[1].contains(SLASH)) {
                String[] splits = strings[1].split(SLASH);
                right = Integer.valueOf(splits[0]);
                CompareUtils.assertRange(left, right);
                CompareUtils.assertRange(cronArray.getMin(), cronArray.getMax(), right);
                step = Integer.valueOf(splits[1]);
            } else {
                // x-y
                right = Integer.valueOf(strings[1]);
                CompareUtils.assertRange(left, right);
                CompareUtils.assertRange(cronArray.getMin(), cronArray.getMax(), right);
            }
        } else if (express.contains(SLASH)) {
            // x/s
            String[] strings = express.split(SLASH);
            left = Integer.valueOf(strings[0]);
            CompareUtils.assertRange(cronArray.getMin(), cronArray.getMax(), left);
            step = Integer.valueOf(strings[1]);
            right = max;
            CompareUtils.assertRange(left, right);
        } else {
            // x
            Integer signal = Integer.valueOf(express);
            if (CronArray.WEEK == this.cronArray && 7 == signal) {
                signal = 0;
            }
            CompareUtils.assertRange(cronArray.getMin(), cronArray.getMax(), signal);
            pointsCache.add(signal);
            return pointsCache;
        }
        for (int i = left; i <= right; i += step) {
            pointsCache.add(i);
        }
        return pointsCache;
    }

    /**
     * 数值是否超出当前域范围
     * @param value
     * @return
     */
    public int compare(int value) {
        return CompareUtils.compare(value, pointsCache.get(pointsCache.size() - 1));
    }

    /**
     *  当期域最大值
     * @return
     */
    public int max() {
        return pointsCache.get(pointsCache.size() - 1);
    }

    /**
     * 当前域最小值
     * @return
     */
    public int min() {
        return pointsCache.get(0);
    }

    /**
     * @return
     */
    public int findUp(int value) {
        CompareUtils.assertRange(value, max());
        for (int i = 0; i < pointsCache.size(); i++) {
            if (pointsCache.get(i) > value) {
                return pointsCache.get(i);
            }
        }
        return value;
    }

    public boolean contains(int value) {
        return pointsCache.contains(value);
    }

    @Override
    public String toString() {
        return "CronField{" + "cronArray"
                + cronArray + ", express='"
                + express + '\'' + '}';
    }
}
