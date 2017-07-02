package com.netstatx.mtp.codec;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class PubMessage extends Message {
    public PubMessage(FixedHeader fixedHeader, AuthPayload payload) {
        super(fixedHeader, payload);
    }
}
