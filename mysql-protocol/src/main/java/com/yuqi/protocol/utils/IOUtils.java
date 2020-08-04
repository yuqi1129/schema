package com.yuqi.protocol.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 2/7/20 20:16
 **/
public class IOUtils {

    public static final byte END_FLAG = 0x00;

    public static void writeString(String s, ByteBuf byteBuf) {
        if (Objects.isNull(s)) {
            return;
        }

        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);

        byteBuf.writeBytes(bytes);
        byteBuf.writeByte(END_FLAG);
    }

    public static void writeInteger(int s, ByteBuf byteBuf, int length) {
        if (length == 3 && (s > (1 << 23) || s < -(1 << 24))) {
            throw new RuntimeException("s is granter than 2^23 or less then -2^23");
        }

        if (length == 2 && (s > Short.MAX_VALUE || s < Short.MIN_VALUE)) {
            throw new RuntimeException("Number exceed short value: " + s);
        }

        byte[] bytes = new byte[length];

        for (int i = length - 1; i >= 0; i--) {
            bytes[i] = (byte) ((s >> (i * 8)) & 0xff);
        }

        byteBuf.writeBytes(bytes);
    }

    public static void writeInteger4(int s, ByteBuf byteBuf) {
        //最高位
        byte b1 = (byte) ((s >> 24) & 0xff);
        //次高位
        byte b2 = (byte) ((s >> 16) & 0xff);
        byte b3 = (byte) ((s >> 8) & 0xff);
        byte b4 = (byte) (s & 0xff);

        byteBuf.writeByte(b4);
        byteBuf.writeByte(b3);
        byteBuf.writeByte(b2);
        byteBuf.writeByte(b1);
    }

    public static void writeInteger3(int s, ByteBuf byteBuf) {
        if (s > (1 << 23) || s < -(1 << 24)) {
            throw new RuntimeException("s is granter than 2^23 or less then -2^23");
        }

        //次高位
        byte b2 = (byte) ((s >> 16) & 0xff);
        byte b3 = (byte) ((s >> 8) & 0xff);
        byte b4 = (byte) (s & 0xff);


        byteBuf.writeByte(b4);
        byteBuf.writeByte(b3);
        byteBuf.writeByte(b2);
    }


    public static void writeInteger2(int s, ByteBuf byteBuf) {
        if (s > Short.MAX_VALUE || s < Short.MIN_VALUE) {
            throw new RuntimeException("Number exceed short value: " + s);
        }

        byte b3 = (byte) ((s >> 8) & 0xff);
        byte b4 = (byte) (s & 0xff);

        byteBuf.writeByte(b4);
        byteBuf.writeByte(b3);
    }

    public static void writeLengthEncodedInteger(int s, ByteBuf byteBuf) {
        if (s < 251) {
            IOUtils.writeInteger(s, byteBuf, 1);
        } else if (s < 2 << 16) {
            IOUtils.writeByte((byte) 0xfc, byteBuf);
            IOUtils.writeInteger(s, byteBuf, 2);
        } else if (s < 2 << 24) {
            IOUtils.writeByte((byte) 0xfd, byteBuf);
            IOUtils.writeInteger(s, byteBuf, 3);
        } else {
            //
            IOUtils.writeByte((byte) 0xfe, byteBuf);
            IOUtils.writeInteger(s, byteBuf, 8);
        }
    }

    public static void writeShort(short s, ByteBuf byteBuf) {
        byte b3 = (byte) ((s >> 8) & 0xff);
        byte b4 = (byte) (s & 0xff);

        byteBuf.writeByte(b4);
        byteBuf.writeByte(b3);
    }

    public static void writeBytes(byte[] s, ByteBuf byteBuf) {
        byteBuf.writeBytes(s);
        byteBuf.writeByte(END_FLAG);
    }

    public static void writeBytesWithoutEndFlag(byte[] s, ByteBuf byteBuf) {
        byteBuf.writeBytes(s);
    }

    public static void writeByte(byte s, ByteBuf byteBuf) {
        byteBuf.writeByte(s);
    }


    public static void writeLengthEncodedString(ByteBuf byteBuf, String string) {
        if (Objects.isNull(string)) {
            byteBuf.writeByte(0xfb);
            return;
        }

        int length = string.length();
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        IOUtils.writeLengthEncodedInteger(length, byteBuf);
        if (length > 0) {
            IOUtils.writeBytesWithoutEndFlag(bytes, byteBuf);
        }
    }


    //read section
    public static short readShort(ByteBuf byteBuf) {
        byte low = (byte) (byteBuf.readByte() & 0xff);
        byte high = (byte) (byteBuf.readByte() & 0xff);

        return (short) (high << 8 + low);
    }

    public static String readString(ByteBuf byteBuf) {
        ByteBuf tmp = ByteBufAllocator.DEFAULT.buffer(byteBuf.readableBytes());

        byte b;
        while (byteBuf.isReadable() && (b = ((byte) (byteBuf.readByte() & 0xff))) != 0x00) {
            tmp.writeByte(b);
        }

        int length = tmp.readableBytes();
        byte[] byteResult = new byte[length];
        tmp.readBytes(byteResult);
        return new String(byteResult);
    }

    /**
     * length start from 4 to 1
     * @param byteBuf
     * @param length
     * @return
     */
    public static int readInteger(ByteBuf byteBuf, int length) {
        byte[] resultArray = new byte[length];
        for (int i = 0; i < length; i++) {
            resultArray[i] = byteBuf.readByte();
        }

        int result = 0;
        for (int i = 0; i < length; i++) {
            result += (resultArray[i] & 0xff) << (8 * i);
        }

        return result;
    }

    public static byte readByte(ByteBuf byteBuf) {
        return byteBuf.readByte();
    }

    public static int readLengthEncodedInteger(ByteBuf byteBuf) {

        int firstByteInt = IOUtils.readInteger(byteBuf, 1);
        if (firstByteInt < 251) {
            return firstByteInt;
        } else if (firstByteInt == 0xfc) {
            return IOUtils.readInteger(byteBuf, 2);
        } else if (firstByteInt == 0xfd) {
            return IOUtils.readInteger(byteBuf, 3);
        } else {
            return IOUtils.readInteger(byteBuf, 8);
        }
    }


    public static byte[] readBytes(ByteBuf byteBuf, int s) {
        byte[] result = new byte[s];

        for (int i = 0; i < s; i++) {
            result[i] = byteBuf.readByte();
        }

        return result;
    }

    public static String readLengthEncodedString(ByteBuf byteBuf) {
        int firstByteInt = IOUtils.readInteger(byteBuf, 1);
        if (firstByteInt == 0xfb) {
            return null;
        } else if (firstByteInt < 251) {
            return new String(readBytes(byteBuf, firstByteInt));
        } else if (firstByteInt == 0xfc) {
            return new String(readBytes(byteBuf, IOUtils.readInteger(byteBuf, 2)));
        } else if (firstByteInt == 0xfd) {
            return new String(readBytes(byteBuf, IOUtils.readInteger(byteBuf, 3)));
        } else {
            return new String(readBytes(byteBuf, IOUtils.readInteger(byteBuf, 8)));
        }
    }

    public static ByteBuf copyByteBuf(ByteBuf buf) {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer(128);

        int i = 0;
        int length = buf.readableBytes();
        while (i++ < length) {
            byteBuf.writeByte(buf.readByte());
        }

        return byteBuf;
    }
}
