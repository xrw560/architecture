package nioserverclient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 * Created by ZN on 2018/11/2.
 */
public class NioClient {

    public static void main(String[] args) throws IOException {

        System.out.println("客户端已经被启动");
        //1. 创建socket通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
        //2. 切换异步非阻塞
        sChannel.configureBlocking(false);//jdk1.7以上
        //3. 指定缓冲区大小
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(new Date().toString().getBytes());
        //4. 切换到读取模式
        buffer.flip();
        sChannel.write(buffer);
        buffer.clear();
        //5. 关闭通道
        sChannel.close();


    }


}
