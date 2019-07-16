package com.github.yafeiwang1240.scheduler.core;

import com.github.yafeiwang1240.scheduler.expression.cron.Cron;
import com.github.yafeiwang1240.scheduler.expression.cron.analysis.CronArray;
import com.github.yafeiwang1240.scheduler.expression.cron.analysis.CronField;
import com.github.yafeiwang1240.scheduler.expression.time.TimeExpression;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期解析工具
 */
public class TimeDecoder {

    public static final long IS_STOP_TIME = -1L;

    public static final Long IS_DEFAULT_TIME = -2L;

    public static long getNextTime(long time, TimeExpression dto) {

        if(dto.getSpace() < 0) {
            return IS_STOP_TIME;
        }

        long space = dto.getUnit().toMillis(dto.getSpace());
        return time + space;
    }

    /**
     * 暂时不做周的匹配
     */
    public static long getNextTime(long time, Cron cron) {
        if (overdue(time, cron.getCronField())) return IS_STOP_TIME;
        return doNext(time, cron.getCronField());
    }

    /**
     * 计算下次运行时间
     *
     * @param time
     * @param cronFields
     * @return
     */
    private static long doNext(long time, List<CronField> cronFields) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        /**
         * <算法思想>从后向前搜索，并做两端初始化</算法思想>
         * <复杂度>O(n)</复杂度>
         * <说明>笛卡尔积搜索算法</说明>
         */
        // 年升级，所有前序降级
        if (!cronFields.get(CronArray.YEAR.getPosition()).contains(year)) {
            calendar.set(Calendar.YEAR, cronFields.get(CronArray.YEAR.getPosition()).findUp(year));
            calendar.set(Calendar.SECOND, cronFields.get(CronArray.SECOND.getPosition()).min());
            calendar.set(Calendar.MINUTE, cronFields.get(CronArray.MINUTE.getPosition()).min());
            calendar.set(Calendar.HOUR_OF_DAY, cronFields.get(CronArray.HOUR.getPosition()).min());
            calendar.set(Calendar.DAY_OF_MONTH, cronFields.get(CronArray.DAY.getPosition()).min());
            calendar.set(Calendar.MONTH, cronFields.get(CronArray.MONTH.getPosition()).min());
            return calendar.getTimeInMillis();
        }

        // 月升级，前序降级
        if (!cronFields.get(CronArray.MONTH.getPosition()).contains(month)) {
            calendar.set(Calendar.SECOND, cronFields.get(CronArray.SECOND.getPosition()).min());
            calendar.set(Calendar.MINUTE, cronFields.get(CronArray.MINUTE.getPosition()).min());
            calendar.set(Calendar.HOUR_OF_DAY, cronFields.get(CronArray.HOUR.getPosition()).min());
            calendar.set(Calendar.DAY_OF_MONTH, cronFields.get(CronArray.DAY.getPosition()).min());

            if (cronFields.get(CronArray.MONTH.getPosition()).max() > month) {
                calendar.set(Calendar.MONTH, cronFields.get(CronArray.MONTH.getPosition()).findUp(month));
                return calendar.getTimeInMillis();
            }
            calendar.set(Calendar.MONTH, cronFields.get(CronArray.MONTH.getPosition()).min());

            if (cronFields.get(CronArray.YEAR.getPosition()).max() > year) {
                calendar.set(Calendar.YEAR, cronFields.get(CronArray.YEAR.getPosition()).findUp(year));
                return calendar.getTimeInMillis();
            }
            return IS_STOP_TIME;
        }

        // 日升级，前序降级
        if (!cronFields.get(CronArray.DAY.getPosition()).contains(day)) {
            calendar.set(Calendar.SECOND, cronFields.get(CronArray.SECOND.getPosition()).min());
            calendar.set(Calendar.MINUTE, cronFields.get(CronArray.MINUTE.getPosition()).min());
            calendar.set(Calendar.HOUR_OF_DAY, cronFields.get(CronArray.HOUR.getPosition()).min());

            if (cronFields.get(CronArray.DAY.getPosition()).max() > day) {
                int nextDay = cronFields.get(CronArray.DAY.getPosition()).findUp(day);
                Calendar _calendar = Calendar.getInstance();
                _calendar.setTime(calendar.getTime());
                _calendar.set(Calendar.DAY_OF_MONTH, nextDay);
                // 日期要加一层判断
                if (_calendar.get(Calendar.DAY_OF_MONTH) == nextDay) {
                    return _calendar.getTimeInMillis();
                }
            }
            calendar.set(Calendar.DAY_OF_MONTH, cronFields.get(CronArray.DAY.getPosition()).min());

            if (cronFields.get(CronArray.MONTH.getPosition()).max() > month) {
                calendar.set(Calendar.MONTH, cronFields.get(CronArray.MONTH.getPosition()).findUp(month));
                return calendar.getTimeInMillis();
            }
            calendar.set(Calendar.MONTH, cronFields.get(CronArray.MONTH.getPosition()).min());

            if (cronFields.get(CronArray.YEAR.getPosition()).max() > year) {
                calendar.set(Calendar.YEAR, cronFields.get(CronArray.YEAR.getPosition()).findUp(year));
                return calendar.getTimeInMillis();
            }
            return IS_STOP_TIME;
        }

        // 小时升级，前序降级
        if (!cronFields.get(CronArray.HOUR.getPosition()).contains(hour)) {
            calendar.set(Calendar.SECOND, cronFields.get(CronArray.SECOND.getPosition()).min());
            calendar.set(Calendar.MINUTE, cronFields.get(CronArray.MINUTE.getPosition()).min());

            if (cronFields.get(CronArray.HOUR.getPosition()).max() > hour) {
                calendar.set(Calendar.HOUR_OF_DAY, cronFields.get(CronArray.HOUR.getPosition()).findUp(hour));
                return calendar.getTimeInMillis();
            }
            calendar.set(Calendar.HOUR_OF_DAY, cronFields.get(CronArray.HOUR.getPosition()).min());

            if (cronFields.get(CronArray.DAY.getPosition()).max() > day) {
                int nextDay = cronFields.get(CronArray.DAY.getPosition()).findUp(day);
                Calendar _calendar = Calendar.getInstance();
                _calendar.setTime(calendar.getTime());
                _calendar.set(Calendar.DAY_OF_MONTH, nextDay);
                // 日期要加一层判断
                if (_calendar.get(Calendar.DAY_OF_MONTH) == nextDay) {
                    return _calendar.getTimeInMillis();
                }
            }
            calendar.set(Calendar.DAY_OF_MONTH, cronFields.get(CronArray.DAY.getPosition()).min());

            if (cronFields.get(CronArray.MONTH.getPosition()).max() > month) {
                calendar.set(Calendar.MONTH, cronFields.get(CronArray.MONTH.getPosition()).findUp(month));
                return calendar.getTimeInMillis();
            }
            calendar.set(Calendar.MONTH, cronFields.get(CronArray.MONTH.getPosition()).min());

            if (cronFields.get(CronArray.YEAR.getPosition()).max() > year) {
                calendar.set(Calendar.YEAR, cronFields.get(CronArray.YEAR.getPosition()).findUp(year));
                return calendar.getTimeInMillis();
            }
            return IS_STOP_TIME;
        }

        // 分钟升级，前序降级
        if (!cronFields.get(CronArray.MINUTE.getPosition()).contains(minute)) {
            calendar.set(Calendar.SECOND, cronFields.get(CronArray.SECOND.getPosition()).min());
            if (cronFields.get(CronArray.MINUTE.getPosition()).max() > minute) {
                calendar.set(Calendar.MINUTE, cronFields.get(CronArray.MINUTE.getPosition()).findUp(minute));
                return calendar.getTimeInMillis();
            }
            calendar.set(Calendar.MINUTE, cronFields.get(CronArray.MINUTE.getPosition()).min());

            if (cronFields.get(CronArray.HOUR.getPosition()).max() > hour) {
                calendar.set(Calendar.HOUR_OF_DAY, cronFields.get(CronArray.HOUR.getPosition()).findUp(hour));
                return calendar.getTimeInMillis();
            }
            calendar.set(Calendar.HOUR_OF_DAY, cronFields.get(CronArray.HOUR.getPosition()).min());

            if (cronFields.get(CronArray.DAY.getPosition()).max() > day) {
                int nextDay = cronFields.get(CronArray.DAY.getPosition()).findUp(day);
                Calendar _calendar = Calendar.getInstance();
                _calendar.setTime(calendar.getTime());
                _calendar.set(Calendar.DAY_OF_MONTH, nextDay);
                // 日期要加一层判断
                if (_calendar.get(Calendar.DAY_OF_MONTH) == nextDay) {
                    return _calendar.getTimeInMillis();
                }
            }
            calendar.set(Calendar.DAY_OF_MONTH, cronFields.get(CronArray.DAY.getPosition()).min());

            if (cronFields.get(CronArray.MONTH.getPosition()).max() > month) {
                calendar.set(Calendar.MONTH, cronFields.get(CronArray.MONTH.getPosition()).findUp(month));
                return calendar.getTimeInMillis();
            }
            calendar.set(Calendar.MONTH, cronFields.get(CronArray.MONTH.getPosition()).min());

            if (cronFields.get(CronArray.YEAR.getPosition()).max() > year) {
                calendar.set(Calendar.YEAR, cronFields.get(CronArray.YEAR.getPosition()).findUp(year));
                return calendar.getTimeInMillis();
            }
            return IS_STOP_TIME;
        }

        if (cronFields.get(CronArray.SECOND.getPosition()).max() > second) {
            calendar.set(Calendar.SECOND, cronFields.get(CronArray.SECOND.getPosition()).findUp(second));
            return calendar.getTimeInMillis();
        }
        // 秒设置为最小值，分将使用同样的判断方案，以此类推直到最高位（年）
        calendar.set(Calendar.SECOND, cronFields.get(CronArray.SECOND.getPosition()).min());

        if (cronFields.get(CronArray.MINUTE.getPosition()).max() > minute) {
            calendar.set(Calendar.MINUTE, cronFields.get(CronArray.MINUTE.getPosition()).findUp(minute));
            return calendar.getTimeInMillis();
        }
        calendar.set(Calendar.MINUTE, cronFields.get(CronArray.MINUTE.getPosition()).min());

        if (cronFields.get(CronArray.HOUR.getPosition()).max() > hour) {
            calendar.set(Calendar.HOUR_OF_DAY, cronFields.get(CronArray.HOUR.getPosition()).findUp(hour));
            return calendar.getTimeInMillis();
        }
        calendar.set(Calendar.HOUR_OF_DAY, cronFields.get(CronArray.HOUR.getPosition()).min());

        if (cronFields.get(CronArray.DAY.getPosition()).max() > day) {
            int nextDay = cronFields.get(CronArray.DAY.getPosition()).findUp(day);
            Calendar _calendar = Calendar.getInstance();
            _calendar.setTime(calendar.getTime());
            _calendar.set(Calendar.DAY_OF_MONTH, nextDay);
            // 日期要加一层判断
            if (_calendar.get(Calendar.DAY_OF_MONTH) == nextDay) {
                return _calendar.getTimeInMillis();
            }
        }
        calendar.set(Calendar.DAY_OF_MONTH, cronFields.get(CronArray.DAY.getPosition()).min());

        if (cronFields.get(CronArray.MONTH.getPosition()).max() > month) {
            calendar.set(Calendar.MONTH, cronFields.get(CronArray.MONTH.getPosition()).findUp(month));
            return calendar.getTimeInMillis();
        }
        calendar.set(Calendar.MONTH, cronFields.get(CronArray.MONTH.getPosition()).min());

        if (cronFields.get(CronArray.YEAR.getPosition()).max() > year) {
            calendar.set(Calendar.YEAR, cronFields.get(CronArray.YEAR.getPosition()).findUp(year));
            return calendar.getTimeInMillis();
        }
        // 默认返回过期
        return IS_STOP_TIME;
    }

    /**
     * 过期判定
     * @param time
     * @param cronFields
     * @return
     */
    private static boolean overdue(long time, List<CronField> cronFields) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // 年过期
        if (cronFields.get(CronArray.YEAR.getPosition()).max() < year) {
            return true;
        } else if(cronFields.get(CronArray.YEAR.getPosition()).max() > year) {
            return false;
        }

        // 月过期
        if (cronFields.get(CronArray.MONTH.getPosition()).max() < month) {
            return true;
        } else if (cronFields.get(CronArray.MONTH.getPosition()).max() > month) {
            return false;
        }

        // 日过期
        if (cronFields.get(CronArray.DAY.getPosition()).max() < day) {
            return true;
        } else if (cronFields.get(CronArray.DAY.getPosition()).max() > day) {
            return false;
        }

        // 小时过期
        if (cronFields.get(CronArray.HOUR.getPosition()).max() < hour) {
            return true;
        } else if (cronFields.get(CronArray.HOUR.getPosition()).max() > hour) {
            return false;
        }

        // 分钟过期
        if (cronFields.get(CronArray.MINUTE.getPosition()).max() < minute) {
            return true;
        } else if (cronFields.get(CronArray.MINUTE.getPosition()).max() > minute) {
            return false;
        }

        // 秒过期
        if (cronFields.get(CronArray.SECOND.getPosition()).max() < second) {
            return true;
        } else if (cronFields.get(CronArray.SECOND.getPosition()).max() > second) {
            return false;
        }

        // 和当前时间相同，默认过期
        return true;
    }
}
