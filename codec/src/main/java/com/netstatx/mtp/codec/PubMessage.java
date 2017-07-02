package com.netstatx.mtp.codec;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public class PubMessage extends Message {
    public PubMessage(FixedHeader fixedHeader, AuthPayload payload) {
        super(fixedHeader, payload);
    }

    @Override
    public byte[] payload() {
        return (byte[]) super.payload();
    }
}
