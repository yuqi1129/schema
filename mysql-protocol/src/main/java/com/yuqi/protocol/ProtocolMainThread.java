package com.yuqi.protocol;

import com.yuqi.protocol.config.ConnectionConfig;
import com.yuqi.protocol.connection.netty.AuthencationHandler;
import com.yuqi.protocol.connection.netty.ByteBufToPackageDecoder;
import com.yuqi.protocol.connection.netty.MysqlPackageHandler;
import com.yuqi.protocol.connection.netty.NettyConnectionHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import static com.yuqi.protocol.constants.Constants.CPU_CORES;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 30/6/20 20:48
 **/
public class ProtocolMainThread implements Runnable {
    private final int port;

    public ProtocolMainThread(int port) {
        this.port = port;
    }

    @Override
    public void run() {
       //
        try {
            final EventLoopGroup boss = new NioEventLoopGroup(1);
            final EventLoopGroup work = new NioEventLoopGroup(CPU_CORES * 2);
            final ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap
                    .group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_RCVBUF, 8196)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    final ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast("open_channler", new NettyConnectionHandler());

                    pipeline.addLast("idle_handler", new IdleStateHandler(
                            ConnectionConfig.readTimeOut,
                            ConnectionConfig.writeTimeout,
                            ConnectionConfig.readTimeOut));

                    pipeline.addLast("decoder", new ByteBufToPackageDecoder());
                    //pipeline.addLast("encoder", new MysqlPackageToByteBufEncoder());
                    pipeline.addLast("authencatin", new AuthencationHandler());

                    pipeline.addLast("handler", new MysqlPackageHandler());
                }
            });

            serverBootstrap.validate();
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            channelFuture.channel().closeFuture().addListener(new GenericFutureListener() {
                @Override
                public void operationComplete(Future future) throws Exception {
                    //todo
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
