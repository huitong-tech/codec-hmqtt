package com.netstatx.mtp.codec;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class PubNeedAckMessage extends PubMessage {
    public PubNeedAckMessage(FixedHeader fixedHeader, AuthPayload payload) {
        super(fixedHeader, payload);
    }
}
