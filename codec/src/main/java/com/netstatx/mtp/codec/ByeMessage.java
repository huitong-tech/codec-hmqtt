package com.netstatx.mtp.codec;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class ByeMessage extends Message {
    public ByeMessage(FixedHeader fixedHeader) {
        super(fixedHeader, null);
    }
}
