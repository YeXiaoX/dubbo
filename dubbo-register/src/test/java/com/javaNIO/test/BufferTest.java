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

    public static void main(String args[]){
        BufferTest bufferTest = new BufferTest();
        bufferTest.readBuffer("");
    }

}
