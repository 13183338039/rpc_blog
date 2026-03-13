package com.markerhub;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class LockTest {

    public synchronized String func(){
        for(int i=0;i<=5;i++){
            System.out.println("run..."+i);
        }
        return "complete...";
    }
    @Test
    public void test(){
        for(int i=0;i<3;i++){
            Thread t=new Thread(new Runnable(){
                @Override
                public void run(){
                    func();
                }
            });
            t.start();
        }
    }

    @Test
    public void test01(){
        ExecutorService pool=new ThreadPoolExecutor(
                2,
                4,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
//        ExecutorService pool1=Executors.newCachedThreadPool();//都是非核心线程
//        ExecutorService pool2=Executors.newFixedThreadPool(2);//定长线程池
//        ExecutorService pool3=Executors.newScheduledThreadPool(2);//定时
//        ExecutorService pool4=Executors.newSingleThreadExecutor();//单线程池
        for(int i=0;i<3;i++){
//            pool.execute(()->{
//                func();
//            });
            try{
                Future<?> f1=pool.submit(()->{
                    return func();
                });
                System.out.println(f1.get());
            }catch(Exception e){
                System.out.println(e);
            }

        }
        pool.shutdown();
    }


}
