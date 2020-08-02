package com.yuqi.protocol.result;

import org.apache.commons.lang3.StringUtils;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 2/8/20 17:12
 **/
public class ErrorMessage {
    private int errorCode;
    private String detailMessage;

    public static final ErrorMessage OK_MESSAGE = new ErrorMessage(0, StringUtils.EMPTY);

    public ErrorMessage(int errorCode, String detailMessage) {
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
