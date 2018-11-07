package nio01;

import io.netty.buffer.ByteBuf;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by ZN on 2018/11/1.
 */
public class BufferTest {


    /**
     * position 缓冲区正在操作的位置，默认从0开始
     * limit 界面(缓冲区可用大小)
     * capacity 缓冲区最大容量，一旦声明不能改变
     * <p>
     * 核心方法：
     * put() 往buffer存放数据
     * get() 获取数据
     */
    @Test
    public void test001() {
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
        System.out.println(new String(bytes, 0, bytes.length));
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        System.out.println("----------重复读取值---------");
        byteBuffer.rewind();//倒回，重复读取
        byte[] bytes2 = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes2);
        System.out.println(new String(bytes2, 0, bytes.length));

        System.out.println("-------------清空缓冲区----------------");
        //清空缓冲区，只是数据被遗忘
        byteBuffer.clear();
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
    }

    @Test
    public void test002() {

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        String str = "abcd";
        byteBuffer.put(str.getBytes());
        //开启读的模式
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes, 0, 2);
        byteBuffer.mark();//打印标记
        System.out.println(new String(bytes, 0, 2));
        System.out.println(byteBuffer.position());
        System.out.println("----------------------------------");
        byteBuffer.reset();//还原到mark位置
        byteBuffer.get(bytes, 0, 2);
        System.out.println(new String(bytes, 0, 2));
        System.out.println(byteBuffer.position());
        byteBuffer.reset();
        System.out.println(byteBuffer.position());
    }

    /**
     * 非直接缓冲区
     */
    @Test
    public void test003() throws IOException {
        long startTime = System.currentTimeMillis();
        FileInputStream fst = new FileInputStream("data/1.png");

        FileOutputStream fos = new FileOutputStream("data/2.png");

        //创建通道
        FileChannel inChannel = fst.getChannel();
        FileChannel outChannel = fos.getChannel();
        //分配指定大小缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (inChannel.read(byteBuffer) != -1) {
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

        long endTime = System.currentTimeMillis();

        System.out.println("操作非直接缓冲区完毕,耗时：" + (endTime - startTime));
    }


    /**
     * 直接缓冲区
     */
    @Test
    public void test004() throws IOException {
        long startTime = System.currentTimeMillis();
        //创建管道
        FileChannel inChannel = FileChannel.open(Paths.get("data/1.png"), StandardOpenOption.READ);

        FileChannel outChannel = FileChannel.open(Paths.get("data/2.png"),
                StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        //定义映射文件
        MappedByteBuffer inMappedByte = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMappedByte = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());
        //直接对缓冲区操作
        byte[] dsf = new byte[inMappedByte.limit()];
        inMappedByte.get(dsf);
        outMappedByte.put(dsf);

        inChannel.close();
        outChannel.close();

        long endTime = System.currentTimeMillis();

        System.out.println("操作直接缓冲区完毕,耗时：" + (endTime - startTime));
    }

    @Test
    public void test005() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("data/test.txt", "rw");
        //获取通道
        FileChannel channel = raf.getChannel();
        //分配指定大小指定缓冲区
        ByteBuffer buffer1 = ByteBuffer.allocate(100);
        ByteBuffer buffer2 = ByteBuffer.allocate(1024);

        //分散读取
        ByteBuffer[] bufs = {buffer1, buffer2};
        channel.read(bufs);

        for (ByteBuffer byteBuffer : bufs) {
            //切成读模式
            byteBuffer.flip();
        }

        System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
        System.out.println("*********************");
        System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));
        System.out.println("-------------聚集读取-----------");

        RandomAccessFile raf2 = new RandomAccessFile("data/test2.txt", "rw");
        //获取通道
        FileChannel channel2 = raf2.getChannel();
        channel2.write(bufs);
        raf2.close();
        raf.close();
    }






}
