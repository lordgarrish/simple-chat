import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static List<ClientConnection> connections;

    public static List<ClientConnection> getConnections() {
        return Collections.unmodifiableList(connections);
    }

    private static class ThreadedConnectionHandler implements Runnable {
        private final Socket incoming;
        private final int number;

        public ThreadedConnectionHandler(Socket incoming, int number) {
            this.incoming = incoming;
            this.number = number;
        }

        @Override
        public void run() {
            ClientConnection client = new ClientConnection(incoming, number);
            connections.add(client);
            client.join();
        }
    }

    public static void main(String[] args) throws IOException{
        try (ServerSocket s = new ServerSocket(5000)) {
            int i = 1;
            connections = new ArrayList<>();

            while(true) {
                Socket incoming = s.accept();
                System.out.println("Connecting " + i);
                Runnable r = new ThreadedConnectionHandler(incoming, i);
                Thread t = new Thread(r);
                t.start();
                i++;
            }
        }
    }
}

