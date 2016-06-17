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
 * Created by Ivan on 2016/6/17.
 */
public class BufferTest {

    private static ByteBuffer buffer = ByteBuffer.allocate(48);

    public void readBuffer(String path) {
        RandomAccessFile aFile = null;
        try {
            aFile = new RandomAccessFile(path, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileChannel fromChannel = aFile.getChannel();
        RandomAccessFile aFile1 = null;
        try {
            aFile = new RandomAccessFile("E://toFile.txt", "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileChannel toChannel = aFile.getChannel();
        long position = 0;
        try {
            position = toChannel.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long count = 0;
        try {
            count = fromChannel.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            toChannel.transferFrom(fromChannel, position, count);
        } catch (IOException e) {
            e.printStackTrace();
        }


//create buffer with capacity of 48 bytes
        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = 0; //read into buffer.
        try {
            bytesRead = toChannel.read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (bytesRead != -1) {
            buf.flip();  //make buffer ready for read

            while (buf.hasRemaining()) {
                System.out.print((char) buf.get()); // read 1 byte at a time
            }
            buf.clear(); //make buffer ready for writing
            try {
                bytesRead = toChannel.read(buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            aFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void readDataFromSocket(SelectionKey key) throws Exception {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        int count;
        while ((count = socketChannel.read(buffer)) > 0) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }// WARNING: the above loop is evil. Because // it's writing back to the same nonblocking // channel it read the data from, this code can // potentially spin in a busy loop. In real life // you'd do something more useful than this.
            buffer.clear(); // Empty buffer
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

        while (bytesRead != -1) {
            buf.flip();  //make buffer ready for read

            while (buf.hasRemaining()) {
                System.out.print((char) buf.get()); // read 1 byte at a time
            }
//            buf.clear(); //make buffer ready for writing
//            try {
//                bytesRead = sc.read(buf);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (readyChannels == 0) continue;
            Set selectedKeys = selector.selectedKeys();
            Iterator keyIterator = selectedKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = (SelectionKey) keyIterator.next();
                if (key.isAcceptable()) {
                    // 接入一个链接
                    System.out.println(1);
                    ServerSocketChannel ssc1 = (ServerSocketChannel) key.channel();
                    SocketChannel sc = null;
                    try {
                        sc = ssc1.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        sc.configureBlocking(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        SelectionKey newKey = sc.register(selector, SelectionKey.OP_READ);
                        SocketChannel aa = (SocketChannel) newKey.channel();
                        getBuffer(aa);
                        try {
                            sayHello(aa);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        newKey.cancel();
                    } catch (ClosedChannelException e) {
                        e.printStackTrace();
                    }

                } else if (key.isConnectable()) {
                    System.out.println(2);
                    // a connection was established with a remote server.
                } else if (key.isReadable()) {
                    System.out.println(3);
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
