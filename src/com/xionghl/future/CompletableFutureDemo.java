package com.xionghl.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @Author:xionghl
 * @Date:2022/11/8 21:01
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(8);
        /**
         * CompletableFuture 四大静态方法
         */
        /**
         * 无返回值 无线程池
         */
        completableFutureVoid();
        /**
         * 无返回值 有线程池
         */
       // completableFutureExecutor(executorService);

        /**
         * 有返回值 无线程池
         */
        // completableFutureNoReturn();

        /**
         * 有返回值 有线程池
         */
       // completableFutureReturn(executorService);
       // completableFutureReturn();

        executorService.shutdown();

        System.out.println("主线程处理");

    }

    public static void completableFutureVoid(){
        //无返回值
        CompletableFuture.runAsync(()->{
            try {
                //模拟处理业务
                Thread.sleep(1000);
                System.out.println("异步线程处理");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


    public static void completableFutureExecutor(ExecutorService executorService){
        //无返回值
        CompletableFuture.runAsync(()->{
            try {
                //模拟处理业务
                Thread.sleep(1000);
                System.out.println("异步线程处理");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },executorService);
    }



    public static void completableFutureNoReturn(){
        //无线程池
        CompletableFuture.supplyAsync(()->{
            try {
                //模拟处理业务
                Thread.sleep(1000);
                System.out.println("异步线程处理");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "7";
        }).whenComplete((result,e)->{
            /**
             * 如果异常为null  上面异步任务的得到执行结果后  回调此
             */
            if(e == null){
                System.out.println("拿到异步线程的返回值"+result);
            }
        }).exceptionally(e->{
            System.out.println("出现异常");
            return e.getMessage();
        });
    }



    public static void completableFutureReturn(ExecutorService executorService){
        //有线程池
        CompletableFuture.supplyAsync(()->{
            try {
                //模拟处理业务
                Thread.sleep(1000);
                System.out.println("异步线程处理");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "7";
        },executorService).whenComplete((result,e)->{
            /**
             * 如果异常为null
             */
            if(e == null){
                System.out.println("拿到异步线程的返回值"+result);
            }
        }).exceptionally(e->{
            System.out.println("出现异常");
            return e.getMessage();
        });
    }



    public static void completableFutureReturn(){
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        //有线程池
        CompletableFuture.runAsync(()->{
            try {
                //模拟处理业务
                Thread.sleep(1000);
                System.out.println("异步线程处理");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },executorService).whenComplete((result,e)->{
            /**
             * 如果异常为null
             */
            if(e == null){
                System.out.println("拿到异步线程的返回值"+result);
            }
        }).exceptionally(e->{
            System.out.println("出现异常");
            return null;
        });

        executorService.shutdown();
    }





}
