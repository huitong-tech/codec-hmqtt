package com.netstatx.mtp.codec;

import io.netty.handler.codec.DecoderResult;
import io.netty.util.internal.StringUtil;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public class Message {
    private final FixedHeader fixedHeader;
    private final Object payload;
    private final DecoderResult decoderResult;

    public Message(FixedHeader fixedHeader) {
        this(fixedHeader, null, null);
    }

    public Message(FixedHeader fixedHeader, Object payload) {
        this(fixedHeader, payload, DecoderResult.SUCCESS);
    }

    public Message(FixedHeader fixedHeader, Object payload, DecoderResult decoderResult) {
        this.fixedHeader = fixedHeader;
        this.payload = payload;
        this.decoderResult = decoderResult;
    }

    public FixedHeader fixedHeader() {
        return fixedHeader;
    }

    public Object payload() {
        return payload;
    }

    public DecoderResult decoderResult() {
        return decoderResult;
    }

    @Override
    public String toString() {
        return new StringBuilder(StringUtil.simpleClassName(this))
                .append('{')
                .append("fixedHeader=").append(fixedHeader() != null ? fixedHeader().toString() : "")
                .append(", payload=").append(payload() != null ? payload.toString() : "")
                .append('}')
                .toString();
    }
}
