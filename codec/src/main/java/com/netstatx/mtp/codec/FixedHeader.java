package com.netstatx.mtp.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.StringUtil;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class FixedHeader {
    private final MessageType messageType;
    private final int seqNo;
    private final int remainingLength;

    public FixedHeader(MessageType messageType, int seqNo, int remainingLength) {
        this.messageType = messageType;
        this.seqNo = seqNo;
        this.remainingLength = remainingLength;
    }

    public short magicNumber() {
        return 0x37;
    }

    public MessageType messageType() {
        return messageType;
    }

    public int seqNo() {
        return seqNo;
    }

    public int remainingLength() {
        return remainingLength;
    }

    public ByteBuf toByteBuf() {
        int fixedHeaderLength = 8;
        ByteBuf buf = Unpooled.buffer(fixedHeaderLength);
        buf.writeByte(magicNumber());
        buf.writeByte(messageType.value());
        buf.writeInt(seqNo);
        buf.writeInt(remainingLength);
        return buf;

    }

    public byte[] toBytes() {
        ByteBuf buf = toByteBuf();
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        return bytes;
    }

    @Override
    public String toString() {
        return new StringBuilder(StringUtil.simpleClassName(this))
                .append("{")
                .append("messageType=").append(messageType)
                .append(", seqNo=").append(seqNo)
                .append(", remainingLength=").append(remainingLength)
                .append('}').toString();
    }
}
