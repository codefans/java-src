package javautil;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * @author codefans
 * @date 2019-02-25 17:06
 *
 * headMap+lastKey可以实现逆时针的一致性hash算法
 * tailMap+firstKey可以实现顺时针的一致性hash算法
 *
 */
public class TreeMapTest {

    private TreeMap data;

    @Before
    public void before() {
        data = new TreeMap<String, String>();
        data.put("111", "val_111");
        data.put("333", "val_333");
        data.put("555", "val_555");
        data.put("222", "val_222");
        data.put("444", "val_444");

    }

    @Test
    public void treeMapTest() {

    }

    @Test
    public void iteratorTreeMapTest() {
        this.print(data);
    }

    /**
     * TreeMap修改后, subMap中的值也会跟着变;
     * subMap修改后, TreeMap中的值也会跟着变;
     * 原理：subMap其实包含了TreeMap的引用, 所以无论修改的是subMap还是修改TreeMap, 最终修改的都是同一份数据。
     */
    @Test
    public void subMapTest() {

        /**
         * subMap方法, 返回大于等于222, 小于444的元素, 即[222, 444)
         */
        SortedMap<String, String> subData = data.subMap("222", "444");
        /**
         * TreeMap修改后, subMap中的值也会跟着变
         */
        data.put("222", "val_222_reset");
        /**
         * 输出
         * [222, val_222_reset], [333, val_333]
         */
        this.print(subData);

        /**
         * subMap修改后, TreeMap中的值也会跟着变
         */
        subData.put("333", "val_333_reset");
        /**
         * 输出
         * [111, val_111], [222, val_222_reset], [333, val_333_reset], [444, val_444], [555, val_555]
         */
        this.print(data);

    }

    /**
     * headMap+lastKey可以实现逆时针的一致性hash算法
     */
    @Test
    public void headMapTest() {

        /**
         * headMap方法, 返回小于333的元素, 即[111, 333)
         */
        SortedMap<String, String> headData = data.headMap("444");
        /**
         * 输出
         *  [111, val_111], [222, val_222], [333, val_333]
         */
        this.print(headData);

        headData = data.headMap("333.5");
        /**
         * 输出
         * lastKey=333
         */
        System.out.println("lastKey=" + headData.lastKey());

        /**
         * TreeMap修改后, headMap中的值也会跟着变
         */
        data.put("222", "val_222_reset");
        /**
         * 输出
         * [111, val_111], [222, val_222_reset], [333, val_333]
         */
        this.print(headData);

        /**
         * headMap修改后, TreeMap中的值也会跟着变
         */
        headData.put("333", "val_333_reset");
        /**
         * 输出
         * [111, val_111], [222, val_222_reset], [333, val_333_reset], [444, val_444], [555, val_555]
         */
        this.print(data);


    }

    /**
     * tailMap+firstKey可以实现顺时针的一致性hash算法
     */
    @Test
    public void tailMapTest() {

        SortedMap tailMap = data.tailMap("222");
        /**
         * 输出
         * firstKey=222
         */
        System.out.println("firstKey=" + tailMap.firstKey());

        tailMap = data.tailMap("222.5");
        /**
         * 输出
         * firstKey=333
         */
        System.out.println("firstKey=" + tailMap.firstKey());


    }

    @Test
    public void firstKeyTest() {

        String firstKey = (String)data.firstKey();
        /**
         * 输出
         * firstKey=111
         */
        System.out.println("firstKey=" + firstKey);

        SortedMap subMap = data.subMap("222", "444");
        /**
         * 输出
         * subMap->firstKey=222
         */
        System.out.println("subMap->firstKey=" + subMap.firstKey());

        SortedMap headMap = data.headMap("333");
        /**
         * 输出
         * headMap->firstKey=111
         */
        System.out.println("headMap->firstKey=" + headMap.firstKey());

        SortedMap tailMap = data.tailMap("444");
        /**
         * 输出
         * tailMap->firstKey=444
         */
        System.out.println("tailMap->firstKey=" + tailMap.firstKey());


    }

    @Test
    public void lastKeyTest() {

        String lastKey = (String)data.lastKey();
        /**
         * 输出
         * lastKey=555
         */
        System.out.println("lastKey=" + lastKey);

        SortedMap subMap = data.subMap("111", "333");
        /**
         * 输出
         * subMap->lastKey=222
         */
        System.out.println("subMap->lastKey=" + subMap.lastKey());

        SortedMap headMap = data.headMap("444");
        /**
         * 输出
         * headMap->lastKey=333
         */
        System.out.println("headMap->lastKey=" + headMap.lastKey());

        SortedMap tailMap = data.tailMap("555");
        /**
         * 输出
         * tailMap->lastKey=555
         */
        System.out.println("tailMap->lastKey=" + tailMap.lastKey());



    }

    public void print(Set<String> sets) {
        Iterator<String> iterator = sets.iterator();
        boolean isFirst = true;
        String element = "";
        while(iterator.hasNext()) {
            element = iterator.next();
            if(isFirst) {
                System.out.print(element);
                isFirst = false;
            } else {
                System.out.print(", " + element);
            }
        }
        System.out.println();
    }

    public void print(Map<String, String> map) {
        Iterator<String> iterator = map.keySet().iterator();
        boolean isFirst = true;
        String key = "";
        while(iterator.hasNext()) {
            key = iterator.next();
            if(isFirst) {
                System.out.print("[" + key + ", " + map.get(key) + "]");
                isFirst = false;
            } else {
                System.out.print(", [" + key + ", " + map.get(key) + "]");
            }
        }
        System.out.println();
    }



}
