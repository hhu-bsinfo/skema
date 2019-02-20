package de.hhu.bsinfo.skema.examples.netty;

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
        Skema.enableAutoRegistration();

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel .class);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel p_channel) {
                p_channel.pipeline().addLast(new RoundTripTime.Encoder(), new RoundTripTime.Decoder(), new ClientHandler());
            }
        });

        String serverIp = "127.0.0.1";
        bootstrap.connect(serverIp, 19000);
    }

    public static final class ClientHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext p_ctx, Object p_msg) throws Exception {
            RoundTripTime time = (RoundTripTime) p_msg;
            p_ctx.writeAndFlush(time);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext p_ctx, Throwable p_cause) {
            p_cause.printStackTrace();
            p_ctx.close();
        }
    }
}
