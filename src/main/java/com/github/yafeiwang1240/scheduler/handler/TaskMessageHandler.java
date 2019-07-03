package com.github.yafeiwang1240.scheduler.handler;

public interface TaskMessageHandler<R, M> {
    R invoke(M message);
    void onFail(M message);
}
