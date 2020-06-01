package com.github.yafeiwang1240.scheduler.expression.cron.analysis;

/**
 * cron 位组
 */
public enum CronArray {

    SECOND(0, 0, 59),
    MINUTE(1, 0, 59),
    HOUR(2, 0, 23),
    DAY(3, 1, 31),
    MONTH(4, 0, 11),
    WEEK(5, 0, 6),
    YEAR(6, 2019, 2099);

    private int position;
    private int min;
    private int max;

    CronArray(int position, int min, int max) {
        this.max = max;
        this.min = min;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public static CronArray getCronArray(int position) {
        CronArray[] cronArrays = CronArray.values();
        if (position >= 0 && position < cronArrays.length) {
            return cronArrays[position];
        }
        return null;
    }
}
