# 手写轻量级任务调度框架
### 基于服务编程的轻量级服务可接入此服务框架，基于RAM实现，可扩展编程接入db
### 支持cron 表达式

#### 	接口说明

```reStructuredText
public class JobClient {
    /**
     * 五分钟后执行且执行一次
     * @param name
     * @param clazz
     * @return
     * @throws ParseException
     */
    public static boolean submit(String name, Class<? extends Job> clazz) throws ParseException;

    /**
     *  执行一次定时任务
     * @param name
     * @param startTime [yyyy-MM-dd HH:mm:ss]
     * @param clazz [job class]
     * @return
     */
    public static boolean submit(String name, String startTime, Class<? extends Job> clazz) throws ParseException;

    /**
     *
     * @param group
     * @param name [任务名称]
     * @param startTime [yyyy-MM-dd HH:mm:ss]
     * @param clazz [job class]
     * @return
     */
    public static boolean submit(String name, String group, String startTime, Class<? extends Job> clazz) throws ParseException;

    /**
     *  重复执行定时任务
     * @param name
     * @param group
     * @param space
     * @param unit
     * @param clazz [job class]
     * @return
     */
    public static boolean submit(String name, String group, long space, TimeUnit unit, Class<? extends Job> clazz);

    /**
     *  重复执行定时任务
     * @param name
     * @param group
     * @param startTime [yyyy-MM-dd HH:mm:ss]
     * @param space
     * @param unit
     * @param clazz [job class]
     * @return
     */
    public static boolean submit(String name, String group, String startTime, long space, TimeUnit unit, Class<? extends Job> clazz) throws ParseException;

    /**
     * 执行一次定时任务
     * @param name
     * @param startTime
     * @param clazz
     * @param map
     * @return
     * @throws ParseException
     */
    public static boolean submit(String name, String startTime, Class<? extends Job> clazz, Map<?, ?> map) throws ParseException;

    /**
     * 执行一次定时任务
     * @param name
     * @param group
     * @param startTime
     * @param clazz
     * @param map
     * @return
     * @throws ParseException
     */
    public static boolean submit(String name, String group, String startTime, Class<? extends Job> clazz, Map<?, ?> map) throws ParseException;

    /**
     * 重复执行定时任务
     * @param name
     * @param group
     * @param space
     * @param unit
     * @param clazz
     * @param map
     * @return
     */
    public static boolean submit(String name, String group, long space, TimeUnit unit, Class<? extends Job> clazz, Map<?, ?> map);

    /**
     * 重复执行定时任务
     * @param name
     * @param group
     * @param startTime
     * @param space
     * @param unit
     * @param clazz
     * @param map
     * @return
     * @throws ParseException
     */
    public static boolean submit(String name, String group, String startTime, long space, TimeUnit unit, Class<? extends Job> clazz, Map<?, ?> map);

    /**
     * 重复执行定时任务
     * @param name
     * @param group
     * @param startTime
     * @param space
     * @param unit
     * @param clazz
     * @param map
     * @return
     */
    public static boolean submit(String name, String group, long startTime, long space, TimeUnit unit, Class<? extends Job> clazz, Map<?, ?> map);

    /**
     * cron表达式
     * @param name
     * @param group
     * @param cronExpress
     * @param clazz
     * @param map
     * @return
     */
    public static boolean submitCron(String name, String group, String cronExpress, Class<? extends Job> clazz, Map<?, ?> map);

    /**
     * 删除任务
     * @param name
     * @return
     */
    public static boolean remove(String name);

    /**
     * 删除任务
     * @param name
     * @param group
     * @return
     */
    public static boolean remove(String name, String group);

    /**
     * 启动
     * @return
     */
    public static boolean start();

    /**
     * 停止调度
     * @return
     */
    public static boolean shutdown();
}
```

##### 	应用举例

```reStructuredText
public static class JobText implements Job {

	@Override
    public void execute(JobExecutionContext var1) throws JobExecutionException {
    	System.out.println(DateUtils.toString(new Date()));
    }
}

public static void test1() {
	System.out.println( "Hello World!" );
    System.out.println( String.format("这是%s, 这是%s, 这是%d", "hello", "world", 111) );
    Map<String, Object> map = new HashMap<>();
    map.put("1", "这是测试1");
    map.put("2", "这是测试2");
    map.put("3", "这是测试3");
    map.put("4", "这是测试4");
    JobClient.start();

	try {
    JobClient.submitCron("test", "test", "18/5 14-15 1 1 7 * 2019", JobText.class, map);
    } catch (Exception e) {
    	e.printStackTrace();
    }
}
```

