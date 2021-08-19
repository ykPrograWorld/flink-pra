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
     */
    public static void selectSort(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if(arr[i] > arr[j]){
                    swap(i,j,arr);
                }
            }

        }
    }
    /*
    位置替换  i和j互换
     */
    public static void swap(int i, int j, int[] arr) {
        int temp;
        temp = arr[i];
        arr[i] = arr[j];
        arr[j] = arr[i];
    }
}
