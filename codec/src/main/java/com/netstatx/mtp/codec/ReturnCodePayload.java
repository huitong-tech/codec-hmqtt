package com.netstatx.mtp.codec;

import io.netty.util.internal.StringUtil;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class ReturnCodePayload {
    private final int returnCode;

    public ReturnCodePayload(int returnCode) {
        this.returnCode = returnCode;
    }

    public int returnCode() {
        return returnCode;
    }

    @Override
    public String toString() {
        return new StringBuilder(StringUtil.simpleClassName(this))
                .append("{")
                .append("returnCode=").append(returnCode)
                .append('}')
                .toString();
    }
}