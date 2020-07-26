package com.yuqi.protocol.command;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/7/20 21:33
 **/
public class CommandHandlerCollection {
    public static final Map<Byte, CommandHandler> COMMAND_TO_HANDLER =
            Maps.newHashMap();
}
