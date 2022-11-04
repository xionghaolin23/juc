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

      /*  //可重入锁
        final ReentrantLock lock = new ReentrantLock(true);

        //传统方式
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }, "AA").start();
        //lambada创建
        new Thread(() -> {

        }, "BB").start();*/

        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        if(list1.contains(list1)){
            System.out.println(1);
        }
    }

}
