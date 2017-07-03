package com.netstatx.mtp.codec;

import com.google.common.io.BaseEncoding;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class CodecUtil {
    static String toHex(byte[] bytes) {
        return BaseEncoding.base16().upperCase().encode(checkNotNull(bytes));
    }

    static byte[] toBytes(String hex) {
        return BaseEncoding.base16().upperCase().decode(checkNotNull(hex).toUpperCase());
    }
}
