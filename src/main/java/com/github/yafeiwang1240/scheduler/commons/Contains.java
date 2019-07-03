package com.github.yafeiwang1240.scheduler.commons;

public class Contains {

    public static final String EXECUTE = "execute";

    private static final String DEFAULT_GROUP = "DEFAULT_GROUP";

    public static final int DEFAULT_LOGGER_SIZE = 10;

    public static String getFullName(String name, String group) {
        return group == null ? DEFAULT_GROUP : group + "." + name;
    }

}
