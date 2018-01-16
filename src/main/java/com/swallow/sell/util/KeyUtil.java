package com.swallow.sell.util;

import java.util.Random;

/**
 * 生成唯一主键
 *
 * @author jdd
 */
public class KeyUtil {


    /**
     * 生成唯一主键
     * @return
     */
    public static synchronized String getUniqueKey(){
        Random random = new Random();

        Integer number = random.nextInt(900000)+100000;

        return System.currentTimeMillis()+String.valueOf(number);
    }

}
