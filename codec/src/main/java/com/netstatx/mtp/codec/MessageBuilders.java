package com.netstatx.mtp.codec;

/**
 * TODO: Create builder for message factory to craft message
 * @author wangle<thisiswangle@gmail.com>
 */
public final class MessageBuilders {
    public static final class ReadyBuilder {}
    public static final class HandshakeBuilder {}
    public static final class AuthBuilder {}
    public static final class ByeBuilder {}
    public static final class RtcBuilder {}
    public static final class RtcAckBuilder {}
    public static final class KeepAliveBuilder {}
    public static final class PubBuilder {}
    public static final class PubNeedAckBuilder {}
    public static final class AckBuilder {}
    public static final class ReturnCodeBuilder {}
}
