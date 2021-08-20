package org.example.datastru;

import org.junit.Test;

public class LeetCodePra {
    public static void main(String[] args) {
        /*
        二分查找
         */
        int[] arr = {1, 3, 5, 7, 9, 12, 14};
        System.out.println(midFind(arr, 9));
        System.out.println(midFindPra(arr, 9));


        int[] arrSele = {3, 1, 5, 14, 7, 12, 9};
        selectSort(arrSele);
        System.out.println();
        selectSortPra(arrSele);
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
                minPos = arr[j] < arr[i] ? j : minPos;
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











}
