package com.yuqi.protocol.pkg.auth;

import com.yuqi.protocol.pkg.AbstractReaderAndWriter;
import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;
import lombok.Builder;

import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_PLUGIN_AUTH;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_SECURE_CONNECTION;
import static com.yuqi.protocol.utils.PackageUtils.getServerCapality;


/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 30/6/20 21:20
 **/
@Builder
public class ServerGreeting extends AbstractReaderAndWriter {

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

    /**
     * Serveer Capability low 2 byte
     */
    private short serverCapability;

    /**
    *  see https://dev.mysql.com/doc/internals/en/character-set.html#packet-Protocol::CharacterSet
     */
    private byte charSet;

    /**
     * see https://dev.mysql.com/doc/internals/en/status-flags.html#packet-Protocol::StatusFlags
     */
    private short serverStatus;

    /**
     * Server Capability high 2 byte
     */
    private short extendServerCapabilities;

    /**
     * see AUTHENCATION_PLUGIN
     */
    private byte authencationPluginLength;

    /**
     * padding * 10
     */
    private byte padding = 0x00;

    /**
     * length 13
     */
    private byte[] saltTwo;

    /**
     * AUTHENCATION_PLUGIN
     */
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

        //filler, see https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#Payload
        // but, in fact, client can't recognize this,
        //IOUtils.writeByte((byte) 0x00, byteBuf);

        IOUtils.writeShort(serverCapability, byteBuf);

        IOUtils.writeByte(charSet, byteBuf);

        IOUtils.writeShort(serverStatus, byteBuf);

        IOUtils.writeShort(extendServerCapabilities, byteBuf);

        //int mergeCapacility = getServerCapacility(serverCapability, extendServerCapabilities);
        int mergeCapacility = getServerCapality();
        int serverStatus = getServerCapality();

        if ((mergeCapacility & CLIENT_PLUGIN_AUTH) != 0) {
            IOUtils.writeByte(authencationPluginLength, byteBuf);
        } else {
            IOUtils.writeByte(padding, byteBuf);
        }

        //10个字节的填充
        for (int i = 0; i < 10; i++) {
            IOUtils.writeByte(padding, byteBuf);
        }

        if ((mergeCapacility & CLIENT_SECURE_CONNECTION) != 0) {
            int len = Math.max(13, authencationPluginLength - 8);
            for (int i = 0;  i < len; i++) {
                IOUtils.writeByte(saltTwo[i], byteBuf);
            }
        }

        if ((mergeCapacility & CLIENT_PLUGIN_AUTH) != 0) {
            IOUtils.writeString(authencationPlugin, byteBuf);
        }

    }

    //TODO, try to restore value, this have bug
    private int getServerCapacility(byte[] lowByte, byte[] highByte) {
        int res = 0;

        res += lowByte[0];
        res += ((lowByte[1] & Integer.MAX_VALUE) << 8);
        res += ((highByte[0] & Integer.MAX_VALUE) << 16);
        res += ((highByte[1] & Integer.MAX_VALUE) << 24);

        return res;
    }
}
