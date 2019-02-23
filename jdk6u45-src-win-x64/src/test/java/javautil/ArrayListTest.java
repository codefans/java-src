package javautil;

import java.util.Arrays;

/**
 * @author: codefans
 * @date: 2019-02-23 10:46:08
 */
public class ArrayListTest {

    public static void main(String[] args) {
        ArrayListTest arrayListTest = new ArrayListTest();
        arrayListTest.arrayListTest();
    }

    public void arrayListTest() {

        this.resizeArray();
        this.copyArray();


    }

    public void resizeArray() {

        int[] arr = new int[5];
        System.out.println("oldLength:" + arr.length);
        /**
         * 将arr改为长度为7的newArr，newArr包含arr的所有元素
         */
        int[] newArr = Arrays.copyOf(arr, 7);
        System.out.println("newLength:" + newArr.length);

    }

    public void copyArray() {

        int[] arr = new int[]{1,2,3,7,8,9};
        int[] newArr = new int[3];
        /**
         * 从源数组arr的下标0开始，复制2个数据，到新数组newArr的下标1(包含1)往后的两个位置
         */
        System.arraycopy(arr, 0, newArr, 1, 2);
        System.out.println("originArr:");
        this.print(arr);
        System.out.println("newArr:");
        this.print(newArr);

    }

    public void print(int[] arr) {
        for(int i = 0; i < arr.length; i ++) {
            if(i != arr.length - 1) {
                System.out.print(arr[i] + ", ");
            } else {
                System.out.print(arr[i]);
            }
        }
        System.out.println();
    }



}
