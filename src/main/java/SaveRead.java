import java.io.*;
import java.util.HashMap;

public class SaveRead implements Serializable {
    private static final String PATH = "scores.txt";

    public void create(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Lisitsyn-David","18-IVT-1-PO-B");
        hashMap.put("Kondrukov-Ilya","18-IVT-1-PO-B");
        hashMap.put("Person1","19-IS-1-B");
        hashMap.put("Person2","18-G");
        hashMap.put("Person3","20-IVT-2-SAPR-B");
        try {
            saveFile(hashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFile(HashMap<String, String> users)
            throws IOException {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(PATH))) {
            os.writeObject(users);
        }
    }

    public HashMap<String, String> readFile()
            throws ClassNotFoundException, IOException {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(PATH))) {
            return (HashMap<String, String>) is.readObject();
        }
    }
}