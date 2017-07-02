package com.netstatx.mtp.codec;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class ReturnCodeMessage extends Message {
    public ReturnCodeMessage(FixedHeader fixedHeader) {
        super(fixedHeader, null);
    }
}
