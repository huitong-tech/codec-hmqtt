package com.netstatx.mtp.codec;

import io.netty.handler.codec.DecoderResult;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class MessageFactory {
    public static Message craftMessage(FixedHeader fixedHeader, Object payload) {
        return null;
    }

    public static Message craftInvalidMessage(Throwable cause) {
        return new Message(null, null, DecoderResult.failure(cause));
    }

    private MessageFactory() { }
}
