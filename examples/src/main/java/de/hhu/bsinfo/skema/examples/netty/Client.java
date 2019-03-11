package de.hhu.bsinfo.skema.examples.netty;

import de.hhu.bsinfo.skema.examples.netty.handler.SkemaOutboundHandler;
import de.hhu.bsinfo.skema.examples.netty.handler.SkemaInboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import de.hhu.bsinfo.skema.Skema;

public class Client {

    public static void main(String[] p_args) {
        Skema.register(RoundTripTime.class);

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel .class);

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
            System.out.println("channelRead");
            p_ctx.writeAndFlush(p_msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext p_ctx, Throwable p_cause) {
            p_cause.printStackTrace();
            p_ctx.close();
        }
    }
}
