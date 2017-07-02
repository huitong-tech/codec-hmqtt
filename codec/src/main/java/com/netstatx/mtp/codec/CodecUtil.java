package com.netstatx.mtp.codec;

import com.google.common.io.BaseEncoding;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class CodecUtil {
    private static final char EOF = '\0';

    static String toHex(byte[] bytes) {
        return BaseEncoding.base16().upperCase().encode(checkNotNull(bytes));
    }

    static String readString(ByteBuf in) {
        ByteBuf buf = Unpooled.buffer();
        byte b;
        while (EOF != (b = in.readByte())) {
            buf.writeByte(b);
        }

        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        return new String(data);
    }
}
