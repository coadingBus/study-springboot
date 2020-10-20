package com.stie.lmk;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Test;

import javax.xml.parsers.FactoryConfigurationError;
import java.sql.Timestamp;
import java.util.concurrent.*;

public class ThreadPool1 {

    /**
     * public ThreadPoolExecutor(int corePoolSize,
     * int maximumPoolSize,
     * long keepAliveTime,
     * TimeUnit unit,
     * BlockingQueue<Runnable> workQueue,
     * ThreadFactory threadFactory,
     * RejectedExecutionHandler handler
     * )
     */
    @Test
    public void test1() {

        //线程池的参数说明
        //参数一是：int corePoolSize，核心线程数
        //参数二是：int maximumPoolSize，最大线程数
        //参数三是：long keepAliveTime，线程存活时间（但有线程长时间没有使用会对这个线程进行销毁）
        //参数四是：TimeUnit unit，时间单位
        //参数五是：BlockingQueue<Runnable> workQueue，等待队列(要设置队列长度)
        //参数六是：ThreadFactory threadFactory，线程工厂，可以不用填写会默认创建一个默认工厂
        //参数七是：RejectedExecutionHandler handler，拒绝策略

        ExecutorService executorService = new ThreadPoolExecutor(3, 5, 2L,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());



        ThreadFactory threadFactory = new ThreadFactoryBuilder().build();

        ExecutorService executorService1 = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }


    @Test
    public void test2() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<Integer> future = executorService.submit(new Task());
        Integer integer = future.get();
        System.out.println(integer);
        executorService.shutdown();
    }

    static class Task implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("子线程开始计算");
            int sum = 0;
            for (int i = 0; i <= 100; i++) {
                sum += i;
            }
            return sum;
        }

    }

    @Test
    public void test3(){

    }





}
