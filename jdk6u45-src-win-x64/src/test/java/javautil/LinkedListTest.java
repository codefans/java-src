package javautil;

import java.util.LinkedList;

/**
 * @Author: codefans
 * @Date: 2019-02-22 23:05
 */

public class LinkedListTest {

    public static void main(String[] args) {
        LinkedListTest linkedListTest = new LinkedListTest();
        linkedListTest.linkedListTest();
    }

    public void linkedListTest() {

        useAsListTest();

        useAsQueue();

        useAsStack();

    }

    public void useAsListTest() {

        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add("111");
        linkedList.add("222");
        linkedList.add("333");
        linkedList.add("444");

        for(int i = 0; i < linkedList.size(); i ++) {
            System.out.print(linkedList.get(i) + ",");
        }
        System.out.println();

    }

    /**
     * FIFO-先进先出
     * FIFO-first in first out
     */
    public void useAsQueue() {

        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add("111");
        linkedList.add("222");
        linkedList.add("333");
        linkedList.add("444");

        System.out.println(linkedList.pop());
        System.out.println(linkedList.pop());
        System.out.println(linkedList.pop());
        System.out.println(linkedList.pop());

    }

    /**
     * LIFO-后进先出
     * LIFO-last in first out
     */
    public void useAsStack() {

        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.push("1111");
        linkedList.push("2222");
        linkedList.push("3333");
        linkedList.push("4444");

        System.out.println(linkedList.pop());
        System.out.println(linkedList.pop());
        System.out.println(linkedList.pop());
        System.out.println(linkedList.pop());


    }


}
