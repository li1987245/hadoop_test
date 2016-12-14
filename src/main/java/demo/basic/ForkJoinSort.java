package demo.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created by d on 2016/12/12.
 */
public class ForkJoinSort {

    static class MergerSort extends RecursiveTask<Integer> {
        private Integer[] arr;

        public MergerSort(Integer[] arr) {
            this.arr = arr;
        }

        @Override
        protected Integer compute() {
            System.out.println();
            return null;
        }
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        List<Integer> lst = new ArrayList<>(5000);
        Random rdm = new Random();
        for(int i=0;i<5000;i++){
            lst.add(rdm.nextInt(5000));
        }
        Integer[] arr = (Integer[]) lst.toArray(new Integer[0]);
        pool.submit(new MergerSort(arr));
    }
}