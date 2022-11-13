package com.xionghl.future;

import java.util.concurrent.CompletableFuture;

/**
 * 两者异步线程结果合并计算
 * @Author:xionghl
 * @Date:2022/11/13 22:16
 */
public class CompletableFutureCombileDemo {


    public static void main(String[] args) {

        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });


        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 20;
        });


        //合并结果
        CompletableFuture<Integer> result = future1.thenCombine(future2,(x,y)->{
            System.out.println("两个结果合并");
            return x+y;
        });

        System.out.println(result.join());

    }
}
