package fr.univtln.cgaiton.test;

import java.io.*;
import java.nio.ByteBuffer;

public class SansSocket {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // simulation Client
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bout);
        oos.writeInt(1215451);
        oos.flush();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(bout.toByteArray());
        //-----------------------

        //on fait comme si on avait récupérer buffer via le serveur
        //puis on essaye de le lire

        //Simulation Serveur
        ByteArrayInputStream binp = new ByteArrayInputStream(buffer.array());
        System.out.println(binp.available());
        ObjectInputStream ois = new ObjectInputStream(binp);
        int i = ois.readInt();
        System.out.println(i);
        //-----------------------
    }
}
