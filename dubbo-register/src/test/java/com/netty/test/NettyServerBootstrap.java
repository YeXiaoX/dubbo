package com.netty.test;

/**
 * Created by Ivan on 2016/6/17.
 */
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

public class NettyServerBootstrap {

    private static Logger logger = Logger.getLogger(NettyServerBootstrap.class);

    private int port;

    public NettyServerBootstrap(int port) {
        this.port = port;
        bind();
    }

    private void bind() {

        EventLoopGroup boss = new NioEventLoopGroup();//接收客户端的输入请求，并把请求分配给worker
        EventLoopGroup worker = new NioEventLoopGroup();//处理boss分配的请求
        //在eventloopGroup中有多少个线程将会被创建取决于，NioEventLoopGroup的实现

        try {

            ServerBootstrap bootstrap = new ServerBootstrap();//具体实现建立一个server

            bootstrap.group(boss, worker);
            bootstrap.channel(NioServerSocketChannel.class);//初始化一个用来接收的channel
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024); //连接数
            bootstrap.option(ChannelOption.TCP_NODELAY, true);  //不延迟，消息立即发送
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); //长连接
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {//辅助server配置一个channel
                @Override
                protected void initChannel(SocketChannel socketChannel)
                        throws Exception {
                    ChannelPipeline p = socketChannel.pipeline();//每一个channel都有一个channelPipeline,一个事件的具体处理过程在它里面发生，channelHandler的交互也是通过它
                    p.addLast(new NettyServerHandler());
                }
            });
            ChannelFuture f = bootstrap.bind(port).sync();
            if (f.isSuccess()) {
                logger.debug("启动Netty服务成功，端口号：" + this.port);
            }
            // 关闭连接
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            logger.error("启动Netty服务异常，异常信息：" + e.getMessage());
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        NettyServerBootstrap server= new NettyServerBootstrap(9999);

    }

}