package com.netstatx.mtp.codec;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class RtcAckMessage extends Message {
    public RtcAckMessage(FixedHeader fixedHeader, RtcAckPayload payload) {
        super(fixedHeader, payload);
    }

    @Override
    public RtcAckPayload payload() {
        return (RtcAckPayload) super.payload();
    }
}
