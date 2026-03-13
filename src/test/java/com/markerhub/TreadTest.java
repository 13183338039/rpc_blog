package com.markerhub;

import org.junit.Test;

import java.util.concurrent.*;

public class TreadTest {

    @Test
    public void a1(){
        int corePoolSize = 2;
        int maximumPoolSize = 4;
        long keepAliveTime = 10L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2);

        // 自定义 ThreadFactory：线程命名+守护线程示例
        ThreadFactory threadFactory = new ThreadFactory() {
            private int count = 1;

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "custom-thread-" + count++);
                t.setDaemon(false); // 设置是否为守护线程
                System.out.println("创建线程: " + t.getName());
                return t;
            }
        };

        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );

        // 提交任务
        for (int i = 0; i < 6; i++) {
            final int taskId = i;
            System.out.println(i);
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " 正在执行任务 " + taskId);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("error");
                    Thread.currentThread().interrupt();
                }
            });
        }
        executor.shutdown();
        try {
            // 等待最多60秒所有任务完成
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.out.println("还有任务没执行完，强制关闭！");
                executor.shutdownNow();
            } else {
                System.out.println("所有任务执行完毕，程序退出");
            }
        } catch (InterruptedException e) {
            System.out.println("主线程中断，立即关闭线程池");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

//    public class T1 extends Thread{
//        @Override
//        public void run(){
//
//        }
//    }
    public void a3(){
        System.out.println("sb");
    }

    @Test
    public void a2(){
        for(int i=0;i<3;i++){
            Thread t=new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("执行");
                    a3();
                }
            });
            t.start();
        }
    }
}
