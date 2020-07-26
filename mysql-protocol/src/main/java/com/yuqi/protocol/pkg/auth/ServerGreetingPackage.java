package com.yuqi.protocol.pkg.auth;

import com.yuqi.protocol.pkg.AbstractPackage;
import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;
import lombok.Builder;


/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 30/6/20 21:20
 **/
@Builder
public class ServerGreetingPackage extends AbstractPackage {

    /**
     * Protocol version
     */
    private byte protocalVeriosn;

    /**
     * Server version info
     */
    private String serverVeriosnInfo;

    /**
     * Server thread id
     */
    private int serverThreadId;

    /**
     * Chanllenge randome
     */
    private byte[] saltOne;


    private short serverCapability;

    private byte serverLanguage;

    private short serverStatus;

    private short extendServerCapabilities;

    private byte authencationPluginLenth;

    /**
     * padding * 10
     */
    private byte padding = 0x00;

    /**
     * length 12
     */
    private byte[] saltTwo;

    private String authencationPlugin;


    @Override
    public void read(ByteBuf byteBuf) {

    }

    @Override
    public void write(ByteBuf byteBuf) {
        IOUtils.writeByte(protocalVeriosn, byteBuf);
        IOUtils.writeString(serverVeriosnInfo, byteBuf);
        IOUtils.writeInteger4((int) Thread.currentThread().getId(), byteBuf);

        IOUtils.writeBytes(saltOne, byteBuf);

        IOUtils.writeShort(serverCapability, byteBuf);

        IOUtils.writeByte(serverLanguage, byteBuf);
        IOUtils.writeShort(serverStatus, byteBuf);

        IOUtils.writeShort(extendServerCapabilities, byteBuf);

//        int mergeCapacility = (extendServerCapabilities << 16) + serverCapability;
//        if ((mergeCapacility & CLIENT_PLUGIN_AUTH) != 0) {
//            IOUtils.writeByte(authencationPluginLenth, byteBuf);
//        } else {
//            IOUtils.writeByte((byte) 0, byteBuf);
//        }

        IOUtils.writeByte((byte) 0, byteBuf);

        //10个字节的填充
        for (int i = 0; i < 10; i++) {
            IOUtils.writeByte(padding, byteBuf);
        }

//        if ((mergeCapacility & CLIENT_SECURE_CONNECTION) != 0) {
//            IOUtils.writeBytes(saltTwo, byteBuf);
//        }

        IOUtils.writeBytes(saltTwo, byteBuf);

//        if ((mergeCapacility & CLIENT_PLUGIN_AUTH) != 0) {
//            IOUtils.writeString(authencationPlugin, byteBuf);
//        }
    }
}
