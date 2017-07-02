package com.netstatx.mtp.codec;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class AuthMessage extends Message {
    public AuthMessage(FixedHeader fixedHeader, AuthPayload payload) {
        super(fixedHeader, payload);
    }
}
