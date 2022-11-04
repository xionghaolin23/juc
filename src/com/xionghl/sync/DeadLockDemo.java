package com.xionghl.sync;

/**
 * 死锁Demo
 *
 * @Author:xionghl
 * @Date:2021/12/19 5:39 下午
 */
public class DeadLockDemo {
    /**
     * 资源1
     */
    static Object o1 = new Object();
    /**
     * 资源2
     */
    static Object o2 = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (o1) {
                System.out.println(Thread.currentThread().getName() + " 持有锁o1，试图获取锁o2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o2) {
                    System.out.println(Thread.currentThread().getName() + " 获取锁o2");

                }
            }
        }, "线程1").start();

        new Thread(() -> {
            synchronized (o2) {
                System.out.println(Thread.currentThread().getName() + " 持有锁o2，试图获取锁o1");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o1) {
                    System.out.println(Thread.currentThread().getName() + " 获取锁o1");

                }
            }
        }, "线程2").start();
    }
}
