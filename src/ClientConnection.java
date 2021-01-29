import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class ClientConnection {
    private final Socket incoming;
    private final int number;
    private PrintWriter out;

    public ClientConnection(Socket incoming, int number) {
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
            Scanner in = new Scanner(inStream,"UTF-8");
            out = new PrintWriter(new OutputStreamWriter(outStream, StandardCharsets.UTF_8), true);

            out.println("Hello and welcome to the chat! Enter EXIT to exit.");
            boolean done = false;
            while(!done && in.hasNextLine()) {
                String line = in.nextLine();
                for (ClientConnection t : ChatServer.getConnections()) {
                    if (t != null)
                        t.shout("Guest " + number + " says: " + line);
                }
                if(line.trim().equals("EXIT")) done = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
