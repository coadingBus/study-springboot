package com.stie.lmk;

public class Thread2 implements Runnable {
    /**
     * 在run方法中实现业务代码
     */
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "我是子线程");

    }

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "我是主线程");
        //这里要通过new Thread()的方式来开启
        new Thread(new Thread2()).start();

        //简化版 lambdas
        new Thread(() -> System.out.println(Thread.currentThread().getName() + "我是子线程")).start();
    }
}
