package com.xionghl.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @Author:xionghl
 * @Date:2022/11/4 22:23
 */
public class FeatureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /**
         * FutureTask  有两个构造方法
         * 1、Callable<V> callable 传一个Callable 接口
         * 2、Runnable runnable, V result 传一个 Runnable 接口
         */
        FutureTask<String> futureTask = new FutureTask<>(new MyCallable());

        Thread t1 = new Thread(futureTask,"t1");
        t1.start();

        //获取返回在
        System.out.println(futureTask.get());

    }

}

/**
 * callable 有返回值
 */
class MyCallable implements Callable<String>{

    @Override
    public String call() throws Exception {
        System.out.println("start juc");
        return "juc";
    }
}
