package com.netstatx.mtp.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.netstatx.mtp.codec.Constant.*;

/**
 * @author wangle<thisiswangle@gmail.com>
 */
public final class Decoder extends ReplayingDecoder<Decoder.DecoderState> {
    private static final Logger LOG = LoggerFactory.getLogger(Decoder.class);

    enum DecoderState {
        READ_FIXED_HEADER,
        READ_PAYLOAD,
        BAD_MESSAGE
    }

    private FixedHeader fixedHeader;
    private int bytesRemaining;
    private final int maxBytesInMessage;

    public Decoder() {
        this(DEFAULT_MAX_BYTES_IN_MESSAGE);
    }

    public Decoder(int maxBytesInMessage) {
        super(DecoderState.READ_FIXED_HEADER);
        this.maxBytesInMessage = maxBytesInMessage;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case READ_FIXED_HEADER:
                fixedHeader = decodeFixedHeader(in);
                bytesRemaining = fixedHeader.remainingLength();
                checkpoint(DecoderState.READ_FIXED_HEADER);
                // Fall through

            case READ_PAYLOAD:
                try {
                    if (bytesRemaining > maxBytesInMessage) {
                        throw new DecoderException("too large message: " + bytesRemaining + " bytes");
                    }
                    final Result<?> decodedPayload =
                            decodePayload(
                                    in,
                                    fixedHeader.messageType(),
                                    bytesRemaining);
                    bytesRemaining -= decodedPayload.numberOfBytesConsumed;
                    if (bytesRemaining != 0) {
                        throw new DecoderException(
                                "non-zero remaining payload bytes: " +
                                        bytesRemaining + " (" + fixedHeader.messageType() + ')');
                    }
                    checkpoint(DecoderState.READ_FIXED_HEADER);
                    Message message = MessageFactory.craftMessage(fixedHeader, decodedPayload.value);
                    fixedHeader = null;
                    out.add(message);
                    break;
                } catch (Exception e) {
                    out.add(invalidMessage(e));
                    return;
                }

            case BAD_MESSAGE:
                in.skipBytes(actualReadableBytes());
                break;

            default:
                throw new Error();
        }
    }


    private FixedHeader decodeFixedHeader(ByteBuf in) {
        short magicNumber = in.readUnsignedByte();
        if (magicNumber != Constant.MAGIC_NUMBER) {
            throw new IllegalArgumentException();
        }

        MessageType messageType = MessageType.valueOf(in.readUnsignedByte());
        int seqNo = in.readInt();
        int remainingLength = in.readUnsignedShort();

        return new FixedHeader(messageType, seqNo, remainingLength);
    }

    private Message invalidMessage(Throwable cause) {
        checkpoint(DecoderState.BAD_MESSAGE);
        return MessageFactory.craftInvalidMessage(cause);
    }

    private static Result<?> decodePayload(
            ByteBuf in,
            MessageType messageType,
            int bytesRemaining) {
        switch (messageType) {
            case READY:
            case BYE:
            case KEEP_ALIVE:
            case RTC:
                return new Result<>();
            case ACK:
                return new Result<>(in.readInt(), 4);

            case AUTH:
                return decodeAuthPayload(in);

            case RETURN_CODE:
                return decodeReturnCodePayload(in);

            case PUB:
            case PUB_NEED_ACK:
                return decodePubAndPubNeedAck(in, bytesRemaining);

            default:
                // Unknown payload , no byte consumed
                return new Result<>(null, 0);
        }
    }

    private static Result<byte[]> decodePubAndPubNeedAck(ByteBuf in, int bytesRemaining) {
        byte[] bytes = new byte[bytesRemaining];
        in.readBytes(bytes);
        return new Result<>(bytes, bytesRemaining);
    }

    private static Result<String> decodeString(ByteBuf in) {
        final Result<Integer> decodedSize = decodeShort(in);
        int size = decodedSize.value;
        int numberOfBytesConsumed = decodedSize.numberOfBytesConsumed;
        String s = in.toString(in.readerIndex(), size, CharsetUtil.UTF_8);
        in.skipBytes(size);
        numberOfBytesConsumed += size;
        return new Result<>(s, numberOfBytesConsumed);
    }

    private static Result<Integer> decodeShort(ByteBuf in) {
        return new Result<>(in.readUnsignedShort(), 2);
    }

    private static Result<ReturnCodePayload> decodeReturnCodePayload(ByteBuf in) {
        Result<Integer> returnCode = decodeShort(in);
        return new Result<>(new ReturnCodePayload(returnCode.value), returnCode.numberOfBytesConsumed);
    }

    private static Result<AuthPayload> decodeAuthPayload(ByteBuf in) {
        Result<String> clientIdentifier = decodeString(in);
        Result<String> encryptedKey = decodeString(in);
        int numberOfBytesConsumed = clientIdentifier.numberOfBytesConsumed + encryptedKey.numberOfBytesConsumed;
        return new Result<>(new AuthPayload(clientIdentifier.value, encryptedKey.value), numberOfBytesConsumed);
    }

    private static final class Result<T> {
        private final T value;
        private final int numberOfBytesConsumed;

        Result(T value, int numberOfBytesConsumed) {
            this.value = value;
            this.numberOfBytesConsumed = numberOfBytesConsumed;
        }

        Result() {
            this(null, 0);
        }
    }
}
