package com.yuqi.protocol.pkg;

import com.google.common.collect.Maps;
import com.yuqi.protocol.pkg.request.QueryPackage;

import java.util.Map;

import static com.yuqi.protocol.constants.CommandTypeConstants.COM_QUERY;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/7/20 20:37
 **/
public class PackageUtils {

    public static final Map<Byte, Class> COMMAND_TYPE_TO_PACKAGE =
            Maps.newHashMap();


    static {
        COMMAND_TYPE_TO_PACKAGE.put(COM_QUERY, QueryPackage.class);
    }
}
