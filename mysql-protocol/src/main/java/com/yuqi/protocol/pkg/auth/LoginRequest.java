package com.yuqi.protocol.pkg.auth;

import com.yuqi.protocol.pkg.AbstractReaderAndWriter;
import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 4/7/20 10:26
 **/
@Data
public class LoginRequest extends AbstractReaderAndWriter {
    /**
     * 客户端capabaility
     */
    private short clientCapability;

    private short extendClientCapability;

    private int maxPackageLength;

    private byte charSet;

    private String userName;

    private String passwordHash;


    @Override
    public void read(ByteBuf byteBuf) {
        this.clientCapability = IOUtils.readShort(byteBuf);
        this.extendClientCapability = IOUtils.readShort(byteBuf);

        this.maxPackageLength = IOUtils.readInteger(byteBuf, 4);
        this.charSet = IOUtils.readByte(byteBuf);

        this.userName = IOUtils.readString(byteBuf);
        this.passwordHash = IOUtils.readString(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        //todo
    }
}
