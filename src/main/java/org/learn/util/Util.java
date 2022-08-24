package org.learn.util;

import java.util.Random;

public class Util {

    public static String createRandomString() {
        Random random = new Random(System.currentTimeMillis());
        long randomLong = random.nextLong();
        return Long.toHexString(randomLong);
    }
}
