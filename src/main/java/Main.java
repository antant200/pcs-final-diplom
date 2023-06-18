import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        File file = new File("pdfs");
        BooleanSearchEngine booleanSearchEngine = new BooleanSearchEngine(file);
        try (ServerSocket serverSocket = new ServerSocket(8989)) { // стартуем сервер один(!) раз
            while (true) { // в цикле(!) принимаем подключения
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream());
                ) {
                    String word = in.readLine();
                    List<PageEntry> pages = booleanSearchEngine.search(word.toLowerCase());

                    if (pages != null) {
                        List<JSONObject> jsonObjects = new ArrayList<>();
                        for (int i = 0; i < pages.size(); i++) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("pdfName", pages.get(i).getPdfName());
                            jsonObject.put("page", pages.get(i).getPage());
                            jsonObject.put("count", pages.get(i).getCount());
                            jsonObjects.add(jsonObject);
                        }
                        out.println(jsonObjects);
                    } else {
                        out.println(Collections.emptyList());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}