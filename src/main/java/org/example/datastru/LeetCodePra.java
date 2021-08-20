package org.example.datastru;

import org.junit.Test;

import java.util.Arrays;

public class LeetCodePra {
    public static void main(String[] args) {
        /*
        二分查找
         */
        int[] arr = {1, 3, 5, 7, 9, 12, 14};
        System.out.println(midFind(arr, 9));
        System.out.println(midFindPra(arr, 9));


        int[] arrSele = {3, 1, 5, 14, 7, 12, 9};
//        选择排序
//        selectSort(arrSele);
//        System.out.println();
//        selectSortPra(arrSele);
//        System.out.println();
//        冒泡排序
//        bubbleSort(arrSele);
//        System.out.println();
//        bulleSortPra(arrSele);
//      插入排序
        insertSort(arrSele);
        System.out.println();
        insertSortPra(arrSele);
    }

    /*
    二分查找
     */
    public static int midFind(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        int mid;
        while (left <= right) {
            mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return -1;
    }

    /*
    二分查找
     */
    public static int midFindPra(int[] arr, int target) {
        int mid;
        int left = 0;
        int right = arr.length;
        while (left <= right) {
            mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return -1;
    }


    /*
    选择排序
    从未排序中永远挑最小的数和当前值替换
     */
    public static void selectSort(int[] arr){
        for (int i = 0; i < arr.length - 1; i++) {
            int minPos = i;
            for (int j = i + 1; j < arr.length; j++) {
                minPos = arr[j] < arr[minPos] ? j : minPos;
            }
            swap(i,minPos,arr);
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }

    /*
    位置替换  i和j互换
     */
    public static void swap(int i, int j, int[] arr) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /*
    选择排序  self pra
     */
    public static void selectSortPra(int[] arr){
        for (int i = 0; i < arr.length - 1; i++) {
            int minPos = i;
            for (int j = i + 1; j < arr.length; j++) {
                minPos = arr[j] < arr[minPos] ? j : minPos;
            }
            swap(arr,i,minPos);
        }

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }

    /*
    交换
     */
    public static void swap(int[] arr,int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    /*
    冒泡排序
     */
    public static void bubbleSort(int[] sourArr){
        int[] tagertArr = Arrays.copyOf(sourArr,sourArr.length);
        for (int i = 0; i < tagertArr.length; i++) {
//      设定一个标记,若为true,则表示此次循环没有进行交换，也就是排列已经有序，排序已完成
            boolean flag = true;
            for (int j = 0; j < tagertArr.length - 1; j++) {
                if(tagertArr[j] > tagertArr[j+1]){
                    int tmp = tagertArr[j];
                    tagertArr[j] = tagertArr[j+1];
                    tagertArr[j+1] = tmp;
                    flag = false;
                }
            }

            if(flag){
                break;
            }
        }
        for (int i = 0; i < tagertArr.length; i++) {
            System.out.print(tagertArr[i] + " ");
        }
    }

/*
  冒泡排序 self pra
 */

    public static void bulleSortPra(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            boolean tag = true;
            for (int j = 0; j < arr.length - 1; j++) {
                if(arr[j] > arr[j+1]){
                    int tmp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = tmp;
                    tag = false;
                }
            }
            if(tag){
                break;
            }
        }

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }

    }

    /*
       插入排序
     */
    public static void insertSort(int[] sourArr){
        int[] arr = Arrays.copyOf(sourArr, sourArr.length);
//      从下标为1的元素开始选择合适的位置插入,因为下标为0的只有一个元素，默认是有序的
        for (int i = 1; i < arr.length; i++) {
//            记录要插入的数据
            int tmp = arr[i];

//            从已经排序的序列最右边的开始比较，找到比其小的数
            int j = i;
            while (j > 0 && tmp < arr[j-1]){
                arr[j] = arr[j - 1];
                j--;
            }

//            存在比其小的数，插入
            if(j != i){
                arr[j] = tmp;
            }
        }

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }

    /*
    插入排序 self pra
     */
    public static void insertSortPra(int[] sourArr){
        int[] arr = Arrays.copyOf(sourArr, sourArr.length);

        for (int i = 1; i < arr.length; i++) {
            int tmp = arr[i];
            int j = i;
            while (j>0 && tmp < arr[j-1]){
                arr[j] = arr[j-1];
                j--;
            }

            if(j != i){
                arr[j] = tmp;
            }
        }

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }








}
