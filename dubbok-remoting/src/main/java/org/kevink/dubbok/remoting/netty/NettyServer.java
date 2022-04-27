package org.kevink.dubbok.remoting.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.ReferenceCountUtil;
import org.kevink.dubbok.common.dto.RpcRequest;
import org.kevink.dubbok.common.dto.RpcResponse;
import org.kevink.dubbok.registry.Registry;
import org.kevink.dubbok.remoting.RpcServer;
import org.kevink.dubbok.serialize.api.Serialization;
import org.kevink.dubbok.serialize.kryo.KryoSerialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer extends RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    public NettyServer(Registry registry) {
        super(registry);
    }

    @Override
    public void start(int port) {
        Serialization serialization = new KryoSerialization();
        // 配置服务器信息
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new KryoDecoder(serialization, RpcRequest.class));
                            p.addLast(new KryoEncoder(serialization, RpcResponse.class));
                            p.addLast(new ServerHandler(registry));
                        }
                    });

            // 启动服务器端口
            ChannelFuture f = b.bind(port).sync();
            logger.info("RPC Server Started ...");

            // 等待到端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("InterruptedException Occurred: ", e);
        } finally {
            // 优雅关闭服务器
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private static class ServerHandler extends ChannelInboundHandlerAdapter {

        private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

        private final Registry reg;

        ServerHandler(Registry reg) {
            this.reg = reg;
        }

        @Override
        public void channelRead(
                ChannelHandlerContext ctx, Object msg) {
            try {
                log.info("RPC Client Connected ...");
                // 服务调用
                RpcResponse response = handle(msg, reg, log);
                // 写回结果
                ChannelFuture f = ctx.writeAndFlush(response);
                // 异步侦听
                f.addListener(ChannelFutureListener.CLOSE);
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
