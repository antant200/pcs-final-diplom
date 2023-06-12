import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        File file = new File("pdfs");
        try (ServerSocket serverSocket = new ServerSocket(8989)) { // стартуем сервер один(!) раз
            while (true) { // в цикле(!) принимаем подключения
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream());
                ) {
                    String word = in.readLine();

                    BooleanSearchEngine booleanSearchEngine = new BooleanSearchEngine(file);

                    List<PageEntry> pages = booleanSearchEngine.search(word.toLowerCase());

                    if (pages != null) {
                        List<JSONObject> jsonObjects = new ArrayList<>();
                        for (int i = 0; i < pages.size(); i++) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("Имя пдф файла: ", pages.get(i).getPdfName());
                            jsonObject.put("Страница: ", pages.get(i).getPage());
                            jsonObject.put("Частота повторения: ", pages.get(i).getCount());
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