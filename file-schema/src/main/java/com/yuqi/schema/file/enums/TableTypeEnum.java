package com.yuqi.schema.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 14/1/20 20:31
 **/
@AllArgsConstructor
@Getter
public enum TableTypeEnum {
    /**
     *
     */
    CSV(0, "CSV"),

    /**
     *
     */
    JSON(1, "JSON");

    private final int index;
    private final String name;

    public static TableTypeEnum getTableTypeEnumByName(String name) {
        for (TableTypeEnum tableTypeEnum : TableTypeEnum.values()) {
            if (tableTypeEnum.name.equalsIgnoreCase(name)) {
                return tableTypeEnum;
            }
        }

        throw new IllegalArgumentException(String.format("Unsupport table type '%s'", name));
    }
}
