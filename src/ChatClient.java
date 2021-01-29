import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ChatClient {
    public static void main(String[] args) throws IOException{
        try (Socket s = new Socket("localhost", 5000)) {
            try (InputStream inStream = s.getInputStream();
                OutputStream outStream = s.getOutputStream()) {
                Scanner in = new Scanner(inStream,"UTF-8");
                PrintWriter out = new PrintWriter(new OutputStreamWriter(outStream, StandardCharsets.UTF_8), true);
                Scanner key = new Scanner(System.in);
                boolean done = false;
                while(!done && in.hasNextLine()) {
                    System.out.println(in.nextLine());
                    String input = key.nextLine();
                    out.println(input);
                    if(input.trim().equals("EXIT")) done = true;
                }
            }
        }
    }
}
