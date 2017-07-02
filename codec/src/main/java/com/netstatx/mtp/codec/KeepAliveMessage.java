package com.netstatx.mtp.codec;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class KeepAliveMessage extends Message {
    public KeepAliveMessage(FixedHeader fixedHeader) {
        super(fixedHeader, null);
    }
}
