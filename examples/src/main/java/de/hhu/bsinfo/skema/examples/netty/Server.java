package de.hhu.bsinfo.skema.examples.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import de.hhu.bsinfo.skema.Skema;
import de.hhu.bsinfo.skema.examples.netty.handler.SkemaInboundHandler;
import de.hhu.bsinfo.skema.examples.netty.handler.SkemaOutboundHandler;

public class Server {

    public static void main(String[] p_args) throws InterruptedException {
        Skema.register(RoundTripTime.class);

        EpollEventLoopGroup boosGroup = new EpollEventLoopGroup();
        EpollEventLoopGroup workerGroup = new EpollEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boosGroup, workerGroup);
        bootstrap.channel(EpollServerSocketChannel.class);

        final EventExecutorGroup group = new DefaultEventExecutorGroup(1500);

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel p_channel) {
                ChannelPipeline pipeline = p_channel.pipeline();
                pipeline.addLast("idleStateHandler", new IdleStateHandler(0,0,1));
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
        public void channelRead(ChannelHandlerContext p_ctx, Object p_msg) {
            System.out.println(p_msg);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext p_ctx, Object p_evt) {
            if (p_evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) p_evt;
                if (event.state() == IdleState.ALL_IDLE) {
                    p_ctx.writeAndFlush(Skema.newRandomInstance(RoundTripTime.class));
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
