package com.xionghl.lock;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  线程之间的定制化通信 实现效果 三个线程 第一个线程打印5次 第二个线程打印10次 第三个线程打印15次
 *
 *  启动三个线程，按照如下要求：
 *  AA打印5次，BB打印10次，CC打印15次，一共进行10轮
 * @Author:xionghl
 * @Date:2021/12/19 11:40 上午
 */
public class ThreadDemo2 {
    public static void main(String[] args) {
        ShareResource share = new ShareResource();
        new Thread(()->{
            for(int i = 0; i < 10;i++){
                share.print5(i+1);
            }
        },"AA").start();

        new Thread(()->{
            for(int i = 0; i < 10;i++){
                share.print10(i+1);
            }
        },"BB").start();

        new Thread(()->{
            for(int i = 0; i < 10;i++){
                share.print15(i+1);
            }
        },"CC").start();
    }
}

class ShareResource{
    /**
     *  AA 2 BB 2 CC 3
     */
    private int flag = 1;

    private Lock lock = new ReentrantLock();
    /**
     * Condition是在java 1.5中才出现的，它用来替代传统的Object的wait()、notify()实现线程间的协作
     * 相比使用Object的wait()、notify()，使用Condition的await()、signal()这种方式实现线程间协作更加安全和高效
     * Condition是个接口，基本的方法就是await()和signal()方法
     */
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    /**
     * 打印5轮 第一个线程给一个标记位1 第一个线程标记位 2 第三个线程标记位 3
     */
    public void print5(int loop){
        lock.lock();
        try {
            while (flag != 1){
                c1.await();
            }
            //打印五次
            for (int i = 1; i <=5; i++) {
                System.out.println(Thread.currentThread().getName()+" :: "+i+" ：轮数："+loop);
            }
            //修改标记位
            flag = 2;
            //通知BB线程
            c2.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print10(int loop){
        lock.lock();
        try {
            while (flag != 2){
                c2.await();
            }
            //打印五次
            for (int i = 1; i <=10; i++) {
                System.out.println(Thread.currentThread().getName()+" :: "+i+" ：轮数："+loop);
            }
            //修改标记位
            flag = 3;
            //通知CC线程
            c3.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print15(int loop){
        lock.lock();
        try {
            while (flag != 3){
                c3.await();
            }
            //打印五次
            for (int i = 1; i <=10; i++) {
                System.out.println(Thread.currentThread().getName()+" :: "+i+" ：轮数："+loop);
            }
            //修改标记位
            flag = 1;
            //通知CC线程
            c1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

