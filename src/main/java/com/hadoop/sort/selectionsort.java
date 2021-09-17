package com.hadoop.sort;

import java.util.Arrays;

public class selectionsort {
    public static void main(String[] args) {

        int[] array = {3, 23, 54, 5, 65, 7, 878, 7};
        selectionSort(array);
        System.out.println(Arrays.toString(array));
    }

    public static void selectionSort(int[] array) {

        if (array == null || array.length <= 1) {
            return;
        }

        int length = array.length;

        for (int i = 0; i < length - 1; i++) {
            // 保存最小数的索引
            int minIndex = i;

            for (int j = i + 1; j < length; j++) {
                // 找到最小的数
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }

            // 交换元素位置
            if (i != minIndex) {
                swap1(array, minIndex, i);

            }
        }
    }

    private static void swap1(int[] array, int minIndex, int i) {
        int temp = array[minIndex];
        array[minIndex] = array[i];
        array[i] = temp;
    }
}
