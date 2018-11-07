package netty5;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by ZN on 2018/11/3.
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("客户端已经启动。。。");
        NioEventLoopGroup pGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(pGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ByteBuf buf = Unpooled.copiedBuffer("_mayi".getBytes());
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });
        ChannelFuture cf = b.connect("127.0.0.1", 8080).sync();
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("itmayiedu_mayi".getBytes()));
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("ncut_mayi".getBytes()));
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("itmayiedu_mayi".getBytes()));
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("ncut_mayi".getBytes()));
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("itmayiedu_mayi".getBytes()));
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("ncut_mayi".getBytes()));
        //等待客户端端口号关闭
        cf.channel().closeFuture().sync();
        pGroup.shutdownGracefully();


    }


}
