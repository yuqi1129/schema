package com.yuqi.storage.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 10/8/20 19:47
 **/
public class UnsafeUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger(UnsafeUtils.class);

//    public static Unsafe getUnsafe() {
//        try {
//            Field field = Unsafe.class.getDeclaredField("theUnsafe");
//            field.setAccessible(true);
//            return (Unsafe) field.get(null);
//        } catch (Exception e) {
//            LOGGER.error(Throwables.getStackTraceAsString(e));
//            throw new RuntimeException("Get unsafe failed: " + e.getMessage());
//        }
//    }
}
