package com.hadoop.thread;

import java.util.concurrent.locks.ReentrantLock;

class Window implements Runnable {
    private int ticket = 100;//定义一百张票
    //1.实例化锁
    private ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {

        while (true) {

            //2.调用锁定方法lock
            lock.lock();

            if (ticket > 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "售出第" + ticket + "张票");
                ticket--;
            } else {
                break;
            }
        }


    }
}

public class LockTest {

    public static void main(String[] args) {
        Window w = new Window();

        Thread t1 = new Thread(w);
        Thread t2 = new Thread(w);
        Thread t3 = new Thread(w);

        t1.setName("窗口1");
        t2.setName("窗口1");
        t3.setName("窗口1");

        t1.start();
        t2.start();
        t3.start();
    }

}
