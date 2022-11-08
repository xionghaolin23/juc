# JUC并发编程





```java
 public static void main(String[] args) {
        //传统方式
        new Thread(new Runnable() {
            @Override
            public void run() {
                
            }
        },"AA").start();
        //lambada创建
        new Thread(()->{
            
        },"BB").start();
    }
```

问题一：

## 调用start方法是否立即创建一个线程？

不是，分析源码

![image-20211219110606244](/Users/xionghaolin/Library/Application Support/typora-user-images/image-20211219110606244.png)

  start方法先调用start0()方法创建线程，

```java
    private native void start0();
```

start0()方法用native修饰，也就是此时调用的是操作系统的方法，和jvm无关了，具体线程的创建权利是到了操作系统这边，如果操作系统不繁忙，响应就会很快的创建，如果操作系统繁忙，可能会有等待。

## **synchronized与lock的异同**

- synchronized是java内置关键字，而Lock是一个java的接口，可以实现同步访问且比synchronized中的方法更加丰富

- synchronized在发生异常时会自动释放线程占有的锁，因此不会导致死锁情况，而lock需要手动通过调用unlock方法释放锁（为了防止业务异常导致不能释放锁，通常释放锁的操作在 finally 块中完成）

- lock可以让等待锁的线程响应中断，而synchronized却不行，会一直等待下去
- 通过 Lock 可以知道有没有成功获取锁，而 synchronized 却无法办到

- Lock 可以提高多个线程进行读操作的效率（当多个线程竞争的时候）

## 用两个线程交替打印 0 1

```java
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

```

## 线程之间的定制化通信 实现效果 三个线程 第一个线程打印5次 第二个线程打印10次 第三个线程打印15次

```java
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


```

## 死锁Demo

```java
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

```

验证死锁

1、通过jps -l 查看当前运行进程

![image-20211219174820555](/Users/xionghaolin/Library/Application Support/typora-user-images/image-20211219174820555.png)

2、通过jstack命令查看是否发生死锁

![image-20211219174903082](/Users/xionghaolin/Library/Application Support/typora-user-images/image-20211219174903082.png)

![image-20211219174929695](/Users/xionghaolin/Library/Application Support/typora-user-images/image-20211219174929695.png)

证明发生死锁


CompletableFuture 与 Feature 之前比较 

Feature

![img.png](img.png)

