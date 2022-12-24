import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ChatServer {
    private static final List<ThreadedConnectionHandler> connections = new ArrayList<>();

    private static class ThreadedConnectionHandler implements Runnable {
        private final Socket incoming;
        private final int number;
        private PrintWriter out;

        public ThreadedConnectionHandler(Socket incoming, int number) {
            this.incoming = incoming;
            this.number = number;
        }

        private void shout(String line) {
            out.println(line);
        }

        public void join() {
            try (InputStream inStream = incoming.getInputStream();
                 OutputStream outStream = incoming.getOutputStream())
            {
                Scanner in = new Scanner(inStream, StandardCharsets.UTF_8);
                out = new PrintWriter(new OutputStreamWriter(outStream, StandardCharsets.UTF_8), true);

                out.println("Hello, Guest " + number + " and welcome to the chat! Enter EXIT to exit.");
                boolean done = false;
                while(!done && in.hasNextLine()) {
                    String line = in.nextLine();
                    for (var client : connections) {
                        if (client != null && client.number != number)
                            if(!line.isEmpty()) {
                                client.shout("Guest " + number + " says: " + line);
                            }
                    }
                    if(line.trim().equals("EXIT")) done = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            ThreadedConnectionHandler client = new ThreadedConnectionHandler(incoming, number);
            connections.add(client);
            client.join();
        }
    }

    public static void main(String[] args) throws IOException{
        try (ServerSocket s = new ServerSocket(5000)) {
            int i = 1;

            while(true) {
                Socket incoming = s.accept();
                System.out.println("Connecting client №" + i);
                Runnable r = new ThreadedConnectionHandler(incoming, i);
                Thread t = new Thread(r);
                t.start();
                i++;
            }
        }
    }
}

