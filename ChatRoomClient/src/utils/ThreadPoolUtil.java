package utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {
    public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    public static void execute(Runnable task) {
        THREAD_POOL.execute(task);
    }
}
