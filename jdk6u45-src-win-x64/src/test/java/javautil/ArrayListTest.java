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
        Arrays.copyOf(arr, 6);
        System.out.println("newLength:" + arr.length);

    }

    public void copyArray() {

        int[] arr = new int[]{1,2,3,7,8,9};
        int[] newArr = new int[3];
        System.arraycopy(arr, 0, newArr, 0, 3);
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
