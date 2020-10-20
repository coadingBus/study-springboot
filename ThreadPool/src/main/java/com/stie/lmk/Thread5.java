package com.stie.lmk;

public class Thread5 extends Thread {
    private volatile boolean flag = true;

    /**
     * 在run方法中实现业务代码
     */
    @Override
    public void run() {
        while (flag) {
        }

    }

    public void stopThread() {
        flag = false;
    }

    public static void main(String[] args) {
        Thread5 thread5 = new Thread5();
        thread5.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread5.stopThread();

    }

}
