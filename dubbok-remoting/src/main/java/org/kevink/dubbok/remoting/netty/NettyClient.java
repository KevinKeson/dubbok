package org.kevink.dubbok.remoting.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.kevink.dubbok.common.dto.RpcRequest;
import org.kevink.dubbok.common.dto.RpcResponse;
import org.kevink.dubbok.remoting.RpcClient;
import org.kevink.dubbok.serialize.api.Serialization;
import org.kevink.dubbok.serialize.kryo.KryoSerialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClient extends RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private static final Bootstrap b = new Bootstrap();

    static {
        Serialization serialization = new KryoSerialization();
        // 配置客户端信息
        EventLoopGroup group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new KryoDecoder(serialization, RpcRequest.class));
                        p.addLast(new KryoEncoder(serialization, RpcResponse.class));
                        p.addLast(new ClientHandler());
                    }
                });
    }

    public NettyClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected Object send(RpcRequest request) {
        try {
            // 启动客户端连接
            ChannelFuture f = b.connect(host, port).sync();
            logger.info("RPC Client Connecting ...");
            Channel channel = f.channel();
            if (channel != null) {
                // 发起调用
                channel.writeAndFlush(request)
                        .addListener(future -> {
                            if (future.isSuccess()) {
                                logger.info("Message Send Succeeded: {}", request.toString());
                            } else {
                                logger.error("Message Send Failed: ", future.cause());
                            }
                        });
                channel.closeFuture().sync();
                // 获取结果
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("response");
                RpcResponse response = channel.attr(key).get();
                // 返回结果
                return check(request, response, logger);
            }
        } catch (Exception e) {
            logger.error("Exception Occurred: ", e);
        }
        // 默认返回
        return null;
    }

    private static class ClientHandler extends ChannelInboundHandlerAdapter {

        private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

        @Override
        public void channelRead(
                ChannelHandlerContext ctx, Object msg) {
            try {
                RpcResponse response = (RpcResponse) msg;
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("response");
                ctx.channel().attr(key).set(response);
                ctx.channel().close();
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }

        @Override
        public void exceptionCaught(
                ChannelHandlerContext ctx, Throwable cause) {
            log.error("Exception Occurred: ", cause);
            ctx.close();
        }

    }

}
