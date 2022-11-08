package com.xionghl.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author:xionghl
 * @Date:2021/12/19 11:02 上午
 */
public class LTicket {
    public static void main(String[] args) {

        //可重入锁
        final ReentrantLock lock = new ReentrantLock(true);

        //传统方式
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }, "AA").start();
        //lambada创建
        new Thread(() -> {

        }, "BB").start();


    }

}
