package java.util;

import org.junit.Test;

/**
 * @Author: codefans
 * @Date: 2019-07-20 23:49
 */

public class HashMapTest {

    @Test
    public void test() {

        String altThreshold = java.security.AccessController.doPrivileged(
                new sun.security.action.GetPropertyAction(
                        "jdk.map.althashing.threshold"));
        System.out.println("altThreadhold:" + altThreshold);

    }

}
