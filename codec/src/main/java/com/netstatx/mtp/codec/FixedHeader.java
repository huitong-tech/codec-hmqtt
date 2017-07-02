package com.netstatx.mtp.codec;

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
