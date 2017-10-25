package fr.univtln.cgaiton.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel clientSocket = SocketChannel.open(new InetSocketAddress("localhost", 5700));

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bout);

        oos.writeInt(1);
        oos.flush();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(bout.toByteArray());
        int read = clientSocket.write(buffer);
        System.out.println(read);

        oos.close();
    }
}
