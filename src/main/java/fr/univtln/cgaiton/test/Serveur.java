package fr.univtln.cgaiton.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;



public class Serveur
{

    public static void main( String[] args ) throws IOException, ClassNotFoundException, SQLException {

        // creation sélecteur
        Selector selector = Selector.open();

        // creation socket et binding avec addresse
        ServerSocketChannel serverSockect = ServerSocketChannel.open();
        String hostname = "localhost";
        int port = 5700;
        InetSocketAddress address = new InetSocketAddress(hostname, port);
        serverSockect.bind(address);

        System.out.println("Serveur lancé sur le port " + port);


        // on met le channel en mode non-bloquant
        serverSockect.configureBlocking(false);


        // on récupère les opérations autorisées par le socket
        // donc l'autorisation des nouvelles connexions (OP_ACCEPT)
        int validops = serverSockect.validOps();


        SelectionKey selectionKey = serverSockect.register(selector, validops, null);

        Iterator<SelectionKey> selectionKeyIterator;

        boolean server_running = true;
        while (server_running) {
            //on prends les socket prets pour des opération d'IO
            selector.select();


            // on crée l'ensemble des opérations en attente
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            selectionKeyIterator = selectionKeys.iterator();


            // on fait une boucle pour traiter toutes les ops en attente
            while (selectionKeyIterator.hasNext()) {
                SelectionKey key = selectionKeyIterator.next();


                if (key.isAcceptable()) {
                    SocketChannel clientSocket = serverSockect.accept();

                    // non bloquant
                    clientSocket.configureBlocking(false);


                    // on l'enregistre avec une action valide de lecture
                    clientSocket.register(selector, SelectionKey.OP_READ);

                    System.out.println("Connexion aceptée: " + clientSocket.getLocalAddress());


                } else if (key.isReadable()) {
                    SocketChannel clientSockect = (SocketChannel) key.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int read = clientSockect.read(buffer);

                    System.out.println(read);

                    System.out.println("creation binp");
                    ByteArrayInputStream binp = new ByteArrayInputStream(buffer.array());
                    System.out.println(binp.available());

                    System.out.println("creation ois");
                    ObjectInputStream ois = new ObjectInputStream(binp);

                    System.out.println("read int");
                    int i = ois.readInt();
                    System.out.println(i);

                    ois.close();
                }
                selectionKeyIterator.remove();
            }
        }
        serverSockect.close();
    }
}