package FirstTask;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client { //
    public static void main(String[] args) throws IOException {

        try (Socket socket = new Socket("127.0.0.1", 8_001);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             PrintWriter out = new PrintWriter(writer, true);
             BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in))) {

            while (true) {
                System.out.println(reader.readLine());
                String name = keyboardReader.readLine();
                String password = keyboardReader.readLine();
                out.println(name);
                out.println(password);
                String serverResponse = reader.readLine();
                if(serverResponse.equals("ok")){
                    break;
                }
                else {
                    System.out.println("Не правильно введены логин или пароль.");
                }
            }

            while (true) {
                String message = reader.readLine();//принимаем сообщение от сервера
                System.out.println(message);//выводим себе на экран сообщение
                String response = keyboardReader.readLine();
                out.println(response);
                if (response.equalsIgnoreCase("end")) {
                    break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
