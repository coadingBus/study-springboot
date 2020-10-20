package com.stie.lmk;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Thread3 implements Callable<String> {
    @Override
    public String call() throws Exception {
        try {
            System.out.println(Thread.currentThread().getName() + "我是子线程");
            Thread.sleep(2000);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "异步调用成功";
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //可以获得返回结果
        FutureTask<String> futureTask = new FutureTask<>(new Thread3());
        new Thread(futureTask).start();
        System.out.println(futureTask.get());
    }
}
