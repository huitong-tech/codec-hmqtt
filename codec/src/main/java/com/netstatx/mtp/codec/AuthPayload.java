package com.netstatx.mtp.codec;

import io.netty.util.internal.StringUtil;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class AuthPayload {
    private final String clientIdentifier;
    private final String encryptedKey;

    public AuthPayload(String clientIdentifier, String encryptedKey) {
        this.clientIdentifier = clientIdentifier;
        this.encryptedKey = encryptedKey;
    }

    public String clientIdentifier() {
        return clientIdentifier;
    }

    public String encryptedKey() {
        return encryptedKey;
    }

    @Override
    public String toString() {
        return new StringBuilder(StringUtil.simpleClassName(this))
                .append("{")
                .append("clientIdentifier=").append(clientIdentifier)
                .append(", encryptedKey='").append(encryptedKey)
                .append('}')
                .toString();
    }
}
