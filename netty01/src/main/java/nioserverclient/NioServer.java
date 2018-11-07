package nioserverclient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by ZN on 2018/11/2.
 */
public class NioServer {

    public static void main(String[] args) throws IOException {
        System.out.println("服务器端已经被启动。。。");
        //1. 创建服务器端通道
        ServerSocketChannel sChannel = ServerSocketChannel.open();
        //2. 设置异步读取
        sChannel.configureBlocking(false);
        //3. 绑定连接
        sChannel.bind(new InetSocketAddress(8080));
        //4. 获取选择器
        Selector selector = Selector.open();
        //5. 将通道注册到选择器中，并且监听接收事件
        sChannel.register(selector, SelectionKey.OP_ACCEPT);
        //6. 轮询获取已经准备好的事件
        while (selector.select() > 0) {
            //7. 获取当前选择器已经监听到的事件
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                //8. 获取准备就绪事件
                SelectionKey sk = it.next();
                //9. 判断事件准备就绪
                if (sk.isAcceptable()) {
                    //10. 若接收就绪，获取客户端连接
                    SocketChannel socketChannle = sChannel.accept();
                    //11. 设置为阻塞模式
                    socketChannle.configureBlocking(false);//异步非阻塞IO
                    //12. 将该通道注册到服务器上
                    socketChannle.register(selector, SelectionKey.OP_READ);


                } else if (sk.isReadable()) {
                    //13. 获取当前选择器就绪状态通道
                    SocketChannel socketChannel = (SocketChannel) sk.channel();
                    //14. 读取数据
                    int len = 0;
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    while ((len = socketChannel.read(byteBuffer)) >= 0) {
                        System.out.println(new String(byteBuffer.array(), 0, len));
                        byteBuffer.clear();
                    }
                }
                it.remove();
            }
        }

    }

}
