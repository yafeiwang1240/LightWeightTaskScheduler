package com.github.yafeiwang1240.scheduler.job;

import com.github.yafeiwang1240.scheduler.expression.Expression;
import com.github.yafeiwang1240.scheduler.Job;
import com.github.yafeiwang1240.scheduler.core.TimeDecoder;

public class JobTriggerImpl implements JobTrigger {

    private TriggerState state;

    private JobDetail jobDetail;

    private Expression expression;

    private long startTime;

    /**
     * -1L exit
     * -2L new
     */
    private long nextTime = TimeDecoder.IS_DEFAULT_TIME;

    @Override
    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public int compareTo(JobTrigger o) {
        return this.getJobKey().compareTo(o.getJobKey());
    }

    @Override
    public TriggerState getState() {
        return state;
    }

    @Override
    public void updateState(TriggerState state) {
        this.state = state;
    }

    @Override
    public JobKey getJobKey() {
        return jobDetail.getJobKey();
    }

    @Override
    public String getDescription() {
        return jobDetail.getDescription();
    }

    @Override
    public JobDataMap getJobDataMap() {
        return jobDetail.getJobDataMap();
    }

    @Override
    public Class<? extends Job> getJobClass() {
        return jobDetail.getJobClass();
    }

    public String getFullName() {
        return jobDetail.getFullName();
    }

    @Override
    public long getNextTime() {
        return nextTime;
    }

    @Override
    public void compute() {
        if(nextTime == TimeDecoder.IS_STOP_TIME) {
            throw new IllegalArgumentException("过时的任务，请及时回收");
        }
        long now = System.currentTimeMillis();
        if(nextTime == TimeDecoder.IS_DEFAULT_TIME) {
            // 第一次执行的时间
            if(startTime >= now) {
                nextTime = startTime;
            } else {
                nextTime = startTime;
                while(nextTime < now) {
                    nextTime = expression.getNextTime(nextTime);
                    if(nextTime == TimeDecoder.IS_STOP_TIME) {
                        throw new IllegalArgumentException("无效的起始时间");
                    }
                }
            }
            return;
        }
        while(nextTime <= now && nextTime != TimeDecoder.IS_STOP_TIME) {
            nextTime = expression.getNextTime(nextTime);
        }

    }

}
