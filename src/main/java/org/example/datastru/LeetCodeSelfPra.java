package org.example.datastru;

import java.util.Arrays;

public class LeetCodeSelfPra {
    public static void main(String[] args) {
        int[] arr = {3,1,5,4,5,9,10,2};
        selectSort(arr);
        bubSort(arr);

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
    public static void insertSort(int[] arr){
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






}
