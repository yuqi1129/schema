package com.yuqi.schema.common.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 24/2/20 17:26
 **/
@Slf4j
public class ReflectionUtils {
    public static Field getField(Class c, String fieldName) {
        try {
            Field f = c.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }
}
