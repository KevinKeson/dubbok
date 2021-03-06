package org.kevink.dubbok.remoting.nio;

import org.kevink.dubbok.common.dto.RpcResponse;
import org.kevink.dubbok.registry.Registry;
import org.kevink.dubbok.remoting.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer extends RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NioServer.class);

    public NioServer(Registry registry) {
        super(registry);
    }

    @Override
    @SuppressWarnings("all")
    public void start(int port) {
        try (Selector selector = Selector.open();
             ServerSocketChannel server = ServerSocketChannel.open()) {
            server.socket().bind(new InetSocketAddress(port));
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("RPC Server Started ...");
            while (true) {
                if (selector.select() == 0) continue;
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        SocketChannel channel = server.accept();
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        doStart(key);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("IOException Occurred: ", e);
        }
    }

    private void doStart(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int num = channel.read(buffer);
        if (num > 0) {
            logger.info("RPC Client Connected ...");
            try (ByteArrayInputStream in = new ByteArrayInputStream(buffer.array());
                 ObjectInputStream input = new ObjectInputStream(in);
                 ByteArrayOutputStream out = new ByteArrayOutputStream();
                 ObjectOutputStream output = new ObjectOutputStream(out)) {
                // ????????????
                RpcResponse response = handle(input, registry, logger);

                // ????????????IO????????????????????????
                // ???????????????????????????????????????
                // 1. ???????????? ????????????3???
                // 2. ???????????? ??????3??? ??????1??????
                // 3. ???????????? ??????3??? + ??????3??? 1 3 10??????

                // ????????????
                output.writeObject(response);
                channel.write(((ByteBuffer) buffer.clear()).put(out.toByteArray()));
            }
        } else if (num == -1) {
            channel.close();
        }
    }

}
