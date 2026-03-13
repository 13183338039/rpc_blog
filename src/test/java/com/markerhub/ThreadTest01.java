package com.markerhub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.Semaphore;

@SpringBootTest
public class ThreadTest01 {
    private static final Semaphore s1=new Semaphore(1);
    private static final Semaphore s2=new Semaphore(0);
    @Test
    public void test01(){
        final int[]a={0};
        Thread t1=new Thread(new Runnable(){
            @Override
            public void run(){
                for(int i=0;i<10;i++){
                    while(a[0]!=0){
                    }
                    System.out.println("abc");
                    a[0]++;
                }
            }
        });
        Thread t2=new Thread(new Runnable(){
            @Override
            public void run(){
                for(int i=0;i<10;i++){
                    while(a[0]!=1){
                    }
                    System.out.println("123");
                    a[0]--;
                }
            }
        });
        t1.start();
        t2.start();

    }
    @Test
    public void test02(){
        Thread t1=new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    for(int i=0;i<10;i++){
                        s1.acquire();
                        System.out.println("abc");
                        s2.release();
                    }
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        });
        Thread t2=new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    for(int i=0;i<10;i++){
                        s2.acquire();
                        System.out.println("123");
                        s1.release();
                    }
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        });
        t1.start();
        t2.start();

    }
}
