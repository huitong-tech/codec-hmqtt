package com.netstatx.mtp.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
@ChannelHandler.Sharable
public final class Encoder extends MessageToMessageEncoder<Message> {
    private final static AttributeKey<AtomicInteger> SEQ_NO_GENERATOR = AttributeKey.valueOf("seqNo");

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, List<Object> out) throws Exception {
        out.add(doEncode(ctx.alloc(), message, generateSeqNo(ctx.channel())));
    }

    static ByteBuf doEncode(ByteBufAllocator byteBufAllocator, Message message, int seqNo) {
        switch (message.fixedHeader().messageType()) {
            case HANDSHAKE:
                return encodeHandShakeMessage(byteBufAllocator, (HandshakeMessage) message, seqNo);
            case RTC_ACK:
                return encodeRtcAckMessage(byteBufAllocator, (RtcAckMessage) message, seqNo);
            case PUB:
                return encodePubMessage(byteBufAllocator, (PubMessage) message, seqNo);
            case PUB_NEED_ACK:
                return encodePubNeedAckMessage(byteBufAllocator, (PubNeedAckMessage) message, seqNo);
            default:
                throw new IllegalArgumentException(
                        "unknown message type: " + message.fixedHeader().messageType().value());
        }
    }

    private static int generateSeqNo(Channel ch) {
        ch.attr(SEQ_NO_GENERATOR).setIfAbsent(new AtomicInteger(0));
        return ch.attr(SEQ_NO_GENERATOR).get().getAndIncrement();
    }

    private static ByteBuf encodeRtcAckMessage(
            ByteBufAllocator byteBufAllocator,
            RtcAckMessage message,
            int seqNo) {
        int fixedHeaderBufferSize = Constant.FIXED_HEADER_LENGTH;
        int payloadBufferSize = 4;
        FixedHeader fixedHeader = new FixedHeader(MessageType.RTC_ACK, seqNo, payloadBufferSize);

        ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + payloadBufferSize);
        buf.writeBytes(fixedHeader.toByteBuf());
        buf.writeInt(message.payload().timestamp());
        return buf;
    }

    private static ByteBuf encodePubMessage(
            ByteBufAllocator byteBufAllocator,
            PubMessage message,
            int seqNo) {
        byte[] bytes = message.payload();
        int fixedHeaderBufferSize = Constant.FIXED_HEADER_LENGTH;
        int payloadBufferSize = bytes.length;

        FixedHeader fixedHeader = new FixedHeader(MessageType.PUB, seqNo, payloadBufferSize);
        ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + payloadBufferSize);
        buf.writeBytes(fixedHeader.toByteBuf());
        buf.writeBytes(bytes);
        return buf;
    }

    private static ByteBuf encodePubNeedAckMessage(
            ByteBufAllocator byteBufAllocator,
            PubNeedAckMessage message,
            int seqNo) {
        byte[] bytes = message.payload();
        int fixedHeaderBufferSize = Constant.FIXED_HEADER_LENGTH;
        int payloadBufferSize = bytes.length;

        FixedHeader fixedHeader = new FixedHeader(MessageType.PUB_NEED_ACK, seqNo, payloadBufferSize);
        ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + payloadBufferSize);
        buf.writeBytes(fixedHeader.toByteBuf());
        buf.writeBytes(bytes);
        return buf;
    }


    private static ByteBuf encodeHandShakeMessage(
            ByteBufAllocator byteBufAllocator,
            HandshakeMessage message,
            int seqNo) {
        int fixedHeaderBufferSize = Constant.FIXED_HEADER_LENGTH;
        int payloadBufferSize = 0;

        HandshakePayload payload = message.payload();

        byte[] saltBytes = encodeStringUtf8(payload.salt());
        payloadBufferSize += 4 + 2 + saltBytes.length;
        FixedHeader fixedHeader = new FixedHeader(MessageType.HANDSHAKE, seqNo, payloadBufferSize);

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
