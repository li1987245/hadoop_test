package demo.basic;

import util.DateHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by d on 2016/12/8.
 */
public class MultiThread {
    private final int maxSize = 20;
    private volatile double count;
    private volatile Lock lock = new ReentrantLock();
    private volatile Condition writeCondition;
    private volatile Condition readCondition;
    private Queue<String> queue;
    private Queue<String> bakQueue;

    public MultiThread() {
        count = 0;
        queue = new ArrayBlockingQueue<String>(maxSize);
        bakQueue = new ArrayBlockingQueue<String>(maxSize);
        writeCondition = lock.newCondition();
        readCondition = lock.newCondition();
    }

    public void addData(String str) throws InterruptedException, IOException {
        try {
            lock.lock();
            writeCondition.await();
            if (count == maxSize) {
                if(bakQueue.size()==maxSize)
                    flushData();
                bakQueue.add(str);
            }
            else {
                queue.add(str);
            }
            writeCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

//    public String readData() throws InterruptedException {
//        try {
//            lock.lock();
//            readCondition.await();
//            if (count == maxSize) {
//                if(bakQueue.size()==maxSize)
//                    flushData();
//                bakQueue.add(str);
//            }
//            else {
//                queue.add(str);
//            }
//            readCondition.signalAll();
//        } finally {
//            lock.unlock();
//        }
//    }

    private void flushData() throws IOException {
        String fileName = DateHelper.simpleFormat(new Date());
        File file = new File(fileName);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bakQueue.forEach(x ->{
            try {
                bw.write(x);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bw.flush();
        bw.close();
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public Queue<String> getQueue() {
        return queue;
    }

    public void setQueue(Queue<String> queue) {
        this.queue = queue;
    }


    public static void main(String[] args) {
        MultiThread multiThread = new MultiThread();
        ExecutorService producer = Executors.newFixedThreadPool(10);
        ExecutorService consumer = Executors.newFixedThreadPool(10);

    }
}
