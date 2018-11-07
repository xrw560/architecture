package netty5;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by ZN on 2018/11/3.
 */
public class ServerHandler extends ChannelHandlerAdapter {


    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String value = (String) msg;
        System.out.println("服务器端收到客户端msg: " + value);

        //回复客户端
        ctx.writeAndFlush("好啊");

    }
}
