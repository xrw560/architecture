package nio01;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by ZN on 2018/11/1.
 */
public class BufferTest {


    /**
     * position 缓冲区正在操作的位置，默认从0开始
     * limit 界面(缓冲区可用大小)
     * capacity 缓冲区最大容量，一旦声明不能改变
     *
     * 核心方法：
     * put() 往buffer存放数据
     * get() 获取数据
     */
    @Test
    public void test001(){
        //初始化byteBuffer大小
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
        System.out.println("--------往byteBuffer存放数据-----------");
        byteBuffer.put("abcde".getBytes());
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
        System.out.println("----------读取值---------");
        //开启读取模式
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        System.out.println(new String(bytes,0,bytes.length));
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        System.out.println("----------重复读取值---------");
        byteBuffer.rewind();//倒回，重复读取
        byte[] bytes2 = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes2);
        System.out.println(new String(bytes2,0,bytes.length));

        System.out.println("-------------清空缓冲区----------------");
        //清空缓冲区，只是数据被遗忘
        byteBuffer.clear();
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
    }

    @Test
    public void test002(){

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        String str = "abcd";
        byteBuffer.put(str.getBytes());
        //开启读的模式
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes,0,2);
        byteBuffer.mark();//打印标记
        System.out.println(new String(bytes,0,2));
        System.out.println(byteBuffer.position());
        System.out.println("----------------------------------");
        byteBuffer.reset();//还原到mark位置
        byteBuffer.get(bytes,0,2);
        System.out.println(new String(bytes,0,2));
        System.out.println(byteBuffer.position());
        byteBuffer.reset();
        System.out.println(byteBuffer.position());
    }

    /**
     * 非直接缓冲区
     */
    @Test
    public void test003() throws IOException {

        FileInputStream fst = new FileInputStream("data/1.png");

        FileOutputStream fos = new FileOutputStream("data/2.png");

        //创建通道
        FileChannel inChannel = fst.getChannel();
        FileChannel outChannel = fos.getChannel();
        //分配指定大小缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while(inChannel.read(byteBuffer)!=-1){
            //开启读取模式
            byteBuffer.flip();
            //将数据写入到通道中
            outChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        //关闭通道
        inChannel.close();
        outChannel.close();
        fos.close();
        fst.close();


    }

}
