package com.javaNIO.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Ivan on 2016/6/24.
 */
public class JavaNIOTest {
    private static ByteBuffer buffer = ByteBuffer.allocate(1024);

    public static void readDataFromSocket(SelectionKey key) throws Exception {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        buffer.clear();//写入数据之前也要把buffer清空，否则可能读不到数据
        int count = socketChannel.read(buffer);
        System.out.println("bufs:" + buffer.limit());
        System.out.println("bytes:" + socketChannel.read(buffer));
        while (count > 0) {
            System.out.println("bufs:" + buffer.hasRemaining());
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
                //socketChannel.write(buffer);//把客户端发来的数据返回给客户端
            }
            buffer.clear(); // Empty buffer

            count = socketChannel.read(buffer);
        }
        if (count < 0) { // Close channel on EOF, invalidates the key
            socketChannel.close();
        }
    }

    static void sayHello(SocketChannel channel) throws Exception {
        buffer.clear();
        buffer.put("Hi there!\r\n".getBytes());
        buffer.flip();
        channel.write(buffer);
    }

    public static void getBuffer(SocketChannel sc) {
        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = 0; //read into buffer.
        try {
            bytesRead = sc.read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("bytes:" + bytesRead);
        System.out.println("bufs:" + buf.hasRemaining());
        while (bytesRead != -1) {
            buf.flip();  //make buffer ready for read

            while (buf.hasRemaining()) {
                System.out.print((char) buf.get()); // read 1 byte at a time
            }
            buf.clear(); //make buffer ready for writing
            try {
                bytesRead = sc.read(buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void registerChannel(Selector selector, SelectableChannel channel, int ops) throws Exception {
        if (channel == null) {
            System.out.println("no channel is gotten !");
            return; // could happen
        }// Set the new channel nonblocking
        channel.configureBlocking(false); // Register it with the selector
        channel.register(selector, ops);
    }

    public static void main(String args[]) {
        ServerSocketChannel ssc = null;
        try {
            ssc = ServerSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ssc.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServerSocket ss = ssc.socket();
        InetSocketAddress address = new InetSocketAddress(8888);
        try {
            ss.bind(address);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*没有选择器的非阻塞写法
         *通过对通道进行轮询，来判断通道所处的状态，如果有连接进来则进行相应处理
         * socket通道是线程安全的，并发访问时无需特别的措施来保证发起访问的多个线程，无论任何时候都只有一个读操作和一个写操作在进行
        while (true) {
            SocketChannel serverChannel = null;
            try {
                serverChannel = ssc.accept();//可以和OldSocket中写法对比，当没有请求过来时，这里不会阻塞，仍会继续往下执行
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (serverChannel == null) {
                System.out.println("1111");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }else{
                System.out.println ("Incoming connection from: " + serverChannel.socket().getRemoteSocketAddress( ));
                buffer.rewind( );
                try {
                    serverChannel.write (buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    serverChannel.close( );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/

        //有选择器的非阻塞写法
        Selector selector = null;
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }


        SelectionKey key1 = null;
        try {
            key1 = ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }


        while (true) {
            int readyChannels = 0;
            try {
                readyChannels = selector.select();
                //System.out.println("size:"+readyChannels);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (readyChannels == 0) {
                System.out.println("1111");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            Set selectedKeys = selector.selectedKeys();
            Iterator keyIterator = selectedKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = (SelectionKey) keyIterator.next();
                if (key.isAcceptable()) {
                    // 接入一个链接
                    System.out.println(1);
                    System.out.println("size:"+readyChannels);
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel channel = null;
                    try {
                        channel = server.accept();
                        System.out.println(channel.getLocalAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        registerChannel(selector, channel, SelectionKey.OP_READ);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        sayHello(channel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (key.isConnectable()) {
                    System.out.println(2);

                    // a connection was established with a remote server.
                } else if (key.isReadable()) {
                    System.out.println(3);
                    try {
                        readDataFromSocket(key);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    key.cancel();//如果一次key读完后没有被取消，则会一直提示key可读
                    // a channel is ready for reading
                } else if (key.isWritable()) {
                    System.out.println(4);
                    // a channel is ready for writing
                }
                keyIterator.remove();
            }
        }


    }
}
