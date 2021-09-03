package org.example.datastru;

import java.util.Arrays;

public class LeetCodeSelfPra {
    public static void main(String[] args) {
        int[] arr = {3,1,5,4,5,9,10,2};
//        selectSort(arr);
//        bubSort(arr);
//        shellSort(arr);
        shellSort2(arr);
    }

    public static void printArr(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    /*
    选择排序
     */
    public static void selectSort(int[] sourArr){
        int[] arr = Arrays.copyOf(sourArr, sourArr.length);
        for (int i = 0; i <arr.length; i++) {
            int minPos = i;
            for (int j = i+1; j < arr.length; j++) {
                minPos = arr[j] < arr[minPos] ? j : minPos;
            }
            swap(arr,i,minPos);
        }
        printArr(arr);
    }

    public static void swap(int[] arr,int i,int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /*
    冒泡排序
     */
    public static void bubSort(int[] sourArr){
        int[] arr = Arrays.copyOf(sourArr, sourArr.length);
        for (int i = 0; i < arr.length; i++) {
            boolean tag = true;
            for (int j = 0; j < arr.length - 1; j++) {
                if(arr[j+1] < arr[j]){
                    swap(arr,j+1,j);
                    tag = false;
                }
            }
            if(tag){
                break;
            }
        }
        printArr(arr);
    }

    /*
    插入排序
     */
    public static void insertSort(int[] sourArr){
        int[] arr = Arrays.copyOf(sourArr, sourArr.length);
        for (int i = 0; i < arr.length; i++) {
            int tmp = arr[i];
            int j = i;
            while (j > 0 && tmp < arr[j-1]){
                arr[j] = arr[j-1];
                j--;
            }
            if(j != i){
                arr[j] = tmp;
            }
        }
    }


    /*
    希尔排序
     */
    public static void shellSort(int[] sourArr){
        int[] arr = Arrays.copyOf(sourArr, sourArr.length);
        int length = arr.length;
        int temp;
        for (int step = length / 2; step >=1 ; step /= 2) {
            for (int i = step; i < length; i++) {
                temp = arr[i];
                int j = i - step;
                while (j>=0 && temp < arr[j]){
                    arr[j+step] = arr[j];
                    j -= step;
                }
                arr[j + step] = temp;
            }
        }
        printArr(arr);
    }

    /*
    希尔排序2
     */
    public static void shellSort2(int[] sourArr){
        int[] arr = Arrays.copyOf(sourArr, sourArr.length);
//        初始化一个间隔
        int h = 1;
//        计算最大间隔
        while (h < arr.length /3){
            h = h * 3 + 1;
        }

        while (h > 0){
//        进行插入排序
            for (int i = h; i < arr.length; i++) {
                int tmp = arr[i];
                int j = i;
                while (j > h - 1 && tmp <= arr[j - h] ){
                    arr[j] = arr[j - h];
                    j -= h;
                }
                arr[j] = tmp;
            }
            h = (h - 1) / 3;
        }

        printArr(arr);
    }






}
