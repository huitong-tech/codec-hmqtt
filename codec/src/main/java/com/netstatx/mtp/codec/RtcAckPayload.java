package com.netstatx.mtp.codec;

import io.netty.util.internal.StringUtil;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class RtcAckPayload {
    private final int timestamp;

    public RtcAckPayload(int timestamp) {
        this.timestamp = timestamp;
    }

    public int timestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return new StringBuilder(StringUtil.simpleClassName(this))
                .append("{")
                .append("timestamp=").append(timestamp)
                .append('}')
                .toString();
    }
}
