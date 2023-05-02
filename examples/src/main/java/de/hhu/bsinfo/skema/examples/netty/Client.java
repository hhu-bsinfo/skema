package de.hhu.bsinfo.skema.examples.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.SocketChannel;

import de.hhu.bsinfo.skema.Skema;
import de.hhu.bsinfo.skema.examples.netty.handler.SkemaInboundHandler;
import de.hhu.bsinfo.skema.examples.netty.handler.SkemaOutboundHandler;

public class Client {

    public static void main(String[] p_args) {
        Skema.register(RoundTripTime.class);

        EpollEventLoopGroup workerGroup = new EpollEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(EpollSocketChannel.class);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel p_channel) {
                p_channel.pipeline().addLast(
                        new SkemaInboundHandler(),
                        new ClientHandler(),
                        new SkemaOutboundHandler());
            }
        });

        String serverIp = "127.0.0.1";
        bootstrap.connect(serverIp, 19000);
    }

    public static final class ClientHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext p_ctx, Object p_msg) {
            System.out.println(p_msg);
            p_ctx.writeAndFlush(p_msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext p_ctx, Throwable p_cause) {
            p_cause.printStackTrace();
            p_ctx.close();
        }
    }
}
