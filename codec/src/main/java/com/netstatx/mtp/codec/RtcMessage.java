package com.netstatx.mtp.codec;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class RtcMessage extends Message {
    public RtcMessage(FixedHeader fixedHeader) {
        super(fixedHeader, null);
    }
}
