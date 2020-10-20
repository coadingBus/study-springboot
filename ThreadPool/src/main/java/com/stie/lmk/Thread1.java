package com.stie.lmk;



public class Thread1 extends Thread {

    /**
     * 在run方法中实现业务代码
     */
    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName()+"我是子线程");

    }

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName()+"我是主线程");
        //不能直接使用run()方法，这就不属于开启线程，而是调用一个方法
        new Thread1().start();
    }
}
