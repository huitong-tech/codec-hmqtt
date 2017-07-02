package com.netstatx.mtp.codec;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class HandshakeMessage extends Message {
    public HandshakeMessage(FixedHeader fixedHeader, HandshakePayload payload) {
        super(fixedHeader, payload);
    }
}
