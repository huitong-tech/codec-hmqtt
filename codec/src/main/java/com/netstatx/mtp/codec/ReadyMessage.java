package com.netstatx.mtp.codec;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class ReadyMessage extends Message {
    public ReadyMessage(FixedHeader fixedHeader) {
        super(fixedHeader);
    }
}
