package com.netstatx.mtp.codec;

/**
 * Message Types.
 *
 * @author wangle<thisiswangle@gmail.com>
 */
public enum MessageType {
    READY(0x10),
    HANDSHAKE(0x00),
    AUTH(0x01),
    BYE(0x02),
    RTC(0x03),
    RTC_ACK(0x04),
    KEEP_ALIVE(0x05),
    PUB(0x06),
    PUB_NEED_ACK(0x07),
    ACK(0x08),
    RETURN_CODE(0xFF);

    private final int value;

    MessageType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static MessageType valueOf(int type) {
        for (MessageType t : values()) {
            if (t.value == type) {
                return t;
            }
        }
        throw new IllegalArgumentException("unknown message type: " + type);
    }
}

