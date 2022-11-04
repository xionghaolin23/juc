package com.xionghl.sync;

/**
 *  用wait  notifyAll 方法实现两个线程交替打印 0 1
 * @Author:xionghl
 * @Date:2021/12/19 11:40 上午
 */
public class ThreadDemo1 {
    public static void main(String[] args) {
        Share share = new Share();
        new Thread(()->{
            for(int i = 0; i < 10;i++){
                try {
                    share.incr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"+1线程").start();

        new Thread(()->{
            for(int i = 0; i < 10;i++){
                try {
                    share.decr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"-1线程").start();
    }
}

class Share{

    private int number = 0;

    /**
     * +1 操作
     */
    public synchronized void incr() throws InterruptedException {
        //判断number值是否是0，如果不是0，等待 因为只有等于 0 才给+1
        while(number != 0){
            //在哪里睡，就在哪里醒
            //wait 方法外面 一定要用wait循环判断 不然可能存在  虚假唤醒  的情况
            this.wait();
        }
        number++;
        System.out.println(Thread.currentThread().getName()+"::"+number);
        //通知其他线程
        this.notifyAll();
    }

    /**
     *  - 1操作
     * @throws InterruptedException
     */
    public synchronized void decr() throws InterruptedException {
        //判断number值是否是1，如果不是1，等待
        while(number != 1){
            this.wait();
        }
        number--;
        System.out.println(Thread.currentThread().getName()+"::"+number);
        //通知其他线程
        this.notifyAll();
    }
}
