package com.javaNIO.test;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * Created by Ivan on 2016/6/27.
 */
public class SelectSockets {
    public static int PORT_NUMBER = 9999;

    public static void main(String[] argv) throws Exception {
        new SelectSockets().go(argv);
    }

    public void go(String[] argv) throws Exception {
        int port = PORT_NUMBER;
        if (argv.length > 0) {
            port = Integer.parseInt(argv[0]);
        }
        System.out.println("Listening on port " + port);
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverChannel.socket();
        serverSocket = serverChannel.socket();
        Selector selector = Selector.open();
        serverSocket.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            System.out.println(":11");
            //selector.close();
           // selector.wakeup();
            // int n = selector.selectNow();//selectNow()过程是非阻塞的,执行结果类似在select()方法执行前添加selector.wakeup()
            int n = selector.select();//服务器启动时，执行到这里(没有就绪的channel时）会阻塞，不断去轮询，直到有连接进来时才会继续往下执行，以后每次selector被唤醒时，循环执行一次，否则继续阻塞。
            System.out.println("就绪的channel个数为：" + n);
            if (n == 0) {
                System.out.println(":0");
                continue;
            }
            Iterator it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = (SelectionKey) it.next();
                if (key.isAcceptable()) {//如果有用户接入，就必须消费掉这个请求（处理请求)，否则会陷入死循环，因为此时selector.select()返回值始终为1
                    System.out.println(":1");
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel channel = server.accept();
                    System.out.println(channel);
                    registerChannel(selector, channel, SelectionKey.OP_READ);
                    sayHello(channel);
                }
                if (key.isReadable()) {
                    readDataFromSocket(key);
                }

                it.remove();
            }
        }
    }

    protected void registerChannel(Selector selector, SelectableChannel channel, int ops) throws Exception {
        if (channel == null) {
            System.out.println("channel is null");
            return;
        }
        channel.configureBlocking(false);
        channel.register(selector, ops);
    }

    private ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

    protected void readDataFromSocket(SelectionKey key) throws Exception {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        int count;
        buffer.clear();
        while ((count = socketChannel.read(buffer)) > 0) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
            buffer.clear();
        }
        if (count < 0) {
            socketChannel.close();
        }
    }

    private void sayHello(SocketChannel channel) throws Exception {
        buffer.clear();
        buffer.put("Hi there!\r\n".getBytes());
        buffer.flip();
        channel.write(buffer);
    }
}

