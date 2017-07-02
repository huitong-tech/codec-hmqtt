package com.netstatx.mtp.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * FixMe: how to generate seq no?
 * @author wangle<thisiswangle@gmail.com>
 */
@ChannelHandler.Sharable
public final class Encoder extends MessageToMessageEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, List<Object> out) throws Exception {
        out.add(doEncode(ctx.alloc(), message));
    }

    static ByteBuf doEncode(ByteBufAllocator byteBufAllocator, Message message) {
        switch (message.fixedHeader().messageType()) {
            case HANDSHAKE:
                return encodeHandShakeMessage(byteBufAllocator, (HandshakeMessage) message);
            case RTC_ACK:
                return encodeRtcAckMessage(byteBufAllocator, (RtcAckMessage) message);
            case PUB:
                return encodePubMessage(byteBufAllocator, (PubMessage) message);
            case PUB_NEED_ACK:
                return encodePubNeedAckMessage(byteBufAllocator, (PubNeedAckMessage) message);
            default:
                throw new IllegalArgumentException(
                        "Unknown message type: " + message.fixedHeader().messageType().value());
        }
    }

    private static ByteBuf encodeRtcAckMessage(
            ByteBufAllocator byteBufAllocator,
            RtcAckMessage message) {
        int fixedHeaderBufferSize = 8;
        int payloadBufferSize = 4;
        FixedHeader fixedHeader = new FixedHeader(MessageType.RTC_ACK, 0, payloadBufferSize);

        ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + payloadBufferSize);
        buf.writeBytes(fixedHeader.toByteBuf());
        buf.writeInt((int) (System.currentTimeMillis() / 1000L));
        return buf;
    }

    private static ByteBuf encodePubMessage(
            ByteBufAllocator byteBufAllocator,
            PubMessage message) {
        byte[] bytes = message.payload();
        int fixedHeaderBufferSize = 8;
        int payloadBufferSize = bytes.length;

        FixedHeader fixedHeader = new FixedHeader(MessageType.PUB, 0, payloadBufferSize);
        ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + payloadBufferSize);
        buf.writeBytes(fixedHeader.toByteBuf());
        buf.writeBytes(bytes);
        return buf;
    }

    private static ByteBuf encodePubNeedAckMessage(
            ByteBufAllocator byteBufAllocator,
            PubNeedAckMessage message) {
        byte[] bytes = message.payload();
        int fixedHeaderBufferSize = 8;
        int payloadBufferSize = bytes.length;

        FixedHeader fixedHeader = new FixedHeader(MessageType.PUB_NEED_ACK, 0, payloadBufferSize);
        ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + payloadBufferSize);
        buf.writeBytes(fixedHeader.toByteBuf());
        buf.writeBytes(bytes);
        return buf;
    }


    private static ByteBuf encodeHandShakeMessage(
            ByteBufAllocator byteBufAllocator,
            HandshakeMessage message) {
        int fixedHeaderBufferSize = 8;
        int payloadBufferSize = 0;

        HandshakePayload payload = message.payload();

        byte[] saltBytes = encodeStringUtf8(payload.salt());
        payloadBufferSize += 4 + 2 + saltBytes.length;
        FixedHeader fixedHeader = new FixedHeader(MessageType.HANDSHAKE, 0, payloadBufferSize);

        ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + payloadBufferSize);
        buf.writeBytes(fixedHeader.toByteBuf());
        buf.writeInt(payload.version());
        buf.writeBytes(saltBytes);
        return buf;
    }

    private static byte[] encodeStringUtf8(String s) {
        return s.getBytes(CharsetUtil.UTF_8);
    }
}
