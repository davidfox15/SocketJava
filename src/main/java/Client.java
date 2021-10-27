import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static final int PORT = 19100;
    public static final String HOST = "localhost";

    public static void main(String[] args) {
        Socket socket = null;
        try {
            while (true) {
                socket = new Socket(HOST, PORT);
                try (InputStream in = socket.getInputStream();
                     OutputStream out = socket.getOutputStream()) {

                    Scanner scanner = new Scanner(System.in);
                    String line = null;
                    System.out.print("\n>");
                    line = scanner.nextLine();
                    out.write(line.getBytes(StandardCharsets.UTF_8));
                    out.flush();

                    byte[] data = new byte[32 * 1024];
                    int readBytes = in.read(data);

                    String answer = new String(data, 0, readBytes);
                    System.out.printf("Server> %s", answer);

                    if (answer.equals("exit") || line.equals("exit"))
                        break;
                } finally {
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
