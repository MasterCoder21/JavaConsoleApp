package wtf.godlydev.spotify.util;

import java.util.concurrent.*;

public class Multithreading
{
    private static final ExecutorService SERVICE;
    
    public static void runAsync(final Runnable task) {
        Multithreading.SERVICE.execute(task);
    }
    
    static {
        SERVICE = Executors.newFixedThreadPool(50, Thread::new);
    }
}
