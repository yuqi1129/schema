package com.yuqi.protocol.result;

import org.apache.commons.lang3.StringUtils;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 2/8/20 17:12
 **/
public class ErrorMessage {
    private int errorCode;
    private String detailMessage;

    public static final ErrorMessage OK_MESSAGE = new ErrorMessage(0, StringUtils.EMPTY);

    /**
     * For example if you create table like
     * ```
     * create table if not exsits xxx (....)
     * and table `xxx` does exists, we then return and ignore result
     * ```
     */
    public static final ErrorMessage OK_MESSAGE_AND_RETURN = new ErrorMessage(0, StringUtils.EMPTY);

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
