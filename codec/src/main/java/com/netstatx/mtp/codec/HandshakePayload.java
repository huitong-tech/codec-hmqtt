package com.netstatx.mtp.codec;

import io.netty.util.internal.StringUtil;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class HandshakePayload {
    private final int version;
    private final String salt;

    public HandshakePayload(int version, String salt) {
        this.version = version;
        this.salt = salt;
    }

    public int version() {
        return version;
    }

    public String salt() {
        return salt;
    }

    @Override
    public String toString() {
        return new StringBuilder(StringUtil.simpleClassName(this))
                .append("{")
                .append("version=").append(version)
                .append(", salt=").append(salt)
                .append('}')
                .toString();
    }
}
