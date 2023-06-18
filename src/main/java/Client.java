import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendMessage(String msg) {
        out.println(msg);
        String response = null;
        try {
            response = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public List<String> getResponses(String response) {
        char jsonResponse[] = response.toCharArray();
        int n = jsonResponse.length, i = 0;
        List<String> responses = new ArrayList<>();
        while (i < n) {
            if (jsonResponse[i] == '{') {
                String buffer = "";
                while (jsonResponse[i - 1] != '}') {
                    buffer += jsonResponse[i];
                    i++;
                }
                responses.add(buffer);
            }
            i++;
        }
        return responses;
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.startConnection("127.0.0.1", 8989);
        Scanner scanner = new Scanner(System.in);
        String response = client.sendMessage(scanner.nextLine());
        List<String> responses = client.getResponses(response);
        if (!responses.isEmpty()) {
            List<JSONObject> jsonObjects = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                JSONObject jsonObject = new JSONObject(responses.get(i));
                jsonObjects.add(jsonObject);
            }
           // Collections.sort(responses,Collections.reverseOrder()); Почему не мог так отсортировать? НЕ приняли
            for (String r : responses) {
                System.out.println(r);
            }
        } else {
            System.out.println(Collections.emptyList());
        }
    }
}
