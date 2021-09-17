package com.hadoop.thread;

public class Sample extends Thread {
    private static int ticket = 100; //将其加载在类的静态区，所有线程共享该静态变量

    @Override
    public void run() {
        while (true) {
            if (ticket > 0) {
                //                try {
                //                    sleep(100);
                //                } catch (InterruptedException e) {
                //                    e.printStackTrace();
                //                }
                System.out.println(getName() + "当前售出第" + ticket + "张票");
                ticket--;
            } else {
                break;
            }
        }
    }

    public static void main(String[] args) {
        Sample t1 = new Sample();
        Sample t2 = new Sample();
        Sample t3 = new Sample();

        t1.setName("售票口1");
        t2.setName("售票口2");
        t3.setName("售票口3");

        t1.start();
        t2.start();
        t3.start();
    }
}

