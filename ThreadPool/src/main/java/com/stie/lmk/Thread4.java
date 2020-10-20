package com.stie.lmk;

public class Thread4 {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (true) {
            }
        });
        //true 守护线程 默认是false 用户线程
        thread.setDaemon(true);
        thread.start();
        System.out.println(Thread.currentThread().getName() + "线程执行结束");
    }
}
