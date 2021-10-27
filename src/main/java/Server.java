import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

public class Server {
    public static final int PORT = 19100;
    public static final String HOST = "localhost";

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Started wait for connection");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Accepted. " + socket.getInetAddress());
                try (InputStream in = socket.getInputStream();
                     OutputStream out = socket.getOutputStream()) {

                    byte[] buf = new byte[32 * 1024];
                    int readBytes = in.read(buf);

                    String line = new String(buf, 0, readBytes);
                    System.out.printf("Client> %s", line);

                    if (Server.answer(out, line) == true)
                        break;

                } finally {
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean answer(OutputStream out, String line) throws IOException {
        String[] command = line.split(" ");
        switch (command[0]) {
            case "time":
                out.write(("System time:" + new Date()).getBytes(StandardCharsets.UTF_8));
                out.flush();
                return false;
            case "info":
                out.write("Lisitsyn David is Developer".getBytes(StandardCharsets.UTF_8));
                out.flush();
                return false;
            case "exit":
                out.write(line.getBytes(StandardCharsets.UTF_8));
                out.flush();
                return true;
            case "task":
                out.write("Справочник студентов".getBytes(StandardCharsets.UTF_8));
                out.flush();
            case "look":
                if (command.length != 2) {
                    out.write(("Bad Syntax -> " + line).getBytes(StandardCharsets.UTF_8));
                    out.flush();
                    return false;
                }
                String group = personGroup(command[1]);
                if(group != null)
                    out.write((command[1] + " Group: " + group).getBytes(StandardCharsets.UTF_8));
                else
                    out.write((command[1] + " Group: Not Found").getBytes(StandardCharsets.UTF_8));
                out.flush();
                return false;
            case "createFile":
                SaveRead o = new SaveRead();
                o.create();
                out.write(("HashMap create!").getBytes(StandardCharsets.UTF_8));
                out.flush();
            default:
                out.write(("Don't know command -> " + line).getBytes(StandardCharsets.UTF_8));
                out.flush();
                return false;
        }
    }

    private static String personGroup(String name) {
        SaveRead file = new SaveRead();
        HashMap<String, String> hashMap;
        try {
            hashMap = file.readFile();
            return hashMap.get(name);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
