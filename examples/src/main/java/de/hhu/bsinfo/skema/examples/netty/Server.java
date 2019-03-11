package de.hhu.bsinfo.skema.examples.netty;

import de.hhu.bsinfo.skema.examples.netty.handler.SkemaOutboundHandler;
import de.hhu.bsinfo.skema.examples.netty.handler.SkemaInboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import de.hhu.bsinfo.skema.Skema;

public class Server {

    public static void main(String[] p_args) throws InterruptedException {
        Skema.register(RoundTripTime.class);

        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boosGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);

        final EventExecutorGroup group = new DefaultEventExecutorGroup(1500);

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel p_channel) {
                ChannelPipeline pipeline = p_channel.pipeline();
                pipeline.addLast("idleStateHandler", new IdleStateHandler(0,0,5));
                pipeline.addLast(new SkemaInboundHandler());
                pipeline.addLast(new SkemaOutboundHandler());
                pipeline.addLast(group,"serverHandler", new ServerHandler());
            }
        });

        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.bind(19000).sync();
    }

    private static final class ServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext p_ctx, Object p_msg) throws Exception {
            System.out.println(p_msg.toString());
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext p_ctx, Object p_evt) throws Exception {
            if (p_evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) p_evt;
                if (event.state() == IdleState.ALL_IDLE) {
                    System.out.println("ALL IDLE");
                    p_ctx.writeAndFlush(new RoundTripTime(System.nanoTime()));
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext p_ctx, Throwable p_cause) {
            p_cause.printStackTrace();
            p_ctx.close();
        }
    }

}
