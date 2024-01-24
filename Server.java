package FirstTask;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8_001, 50);
        while (true) {
            Socket socket = serverSocket.accept();
            Thread t = new Thread(new ClientHandler(socket));
            t.start(); // используем многопоточку,чтобы сервер работал для большого кол-ва клиентов
        }
    }


    static class ClientHandler implements Runnable {
        String nameClient;

        Date timeConnection;

        int countQuote;

        Date timeDisconnection;

        Socket socket;
        FileWriter fileWriter = new FileWriter("info"); //была попытка реализовать так,чтобы сервер записывал в файл все данные,а не только выводил в консоль
        PrintWriter bf = new PrintWriter(fileWriter); //что-то не вышло. не понимаю,где ошибка

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    PrintWriter out = new PrintWriter(writer, true)
            ) {
                timeConnection = new Date();
                nameClient = socket.toString();
                System.out.println("Соединение установлено." + socket.getLocalSocketAddress());
                while (true) {
                    out.println("Введите логин и пароль(client/0000)"); //проверка пароля(данные в скобках)
                    String name = reader.readLine();
                    String password = reader.readLine();
                    if (CorrectLogIn.logInIsCorrect(name, password)) {
                        out.println("ok\n");
                        break;
                    } else {
                        out.println("Повторите ввод логина и пароля.");
                    }
                }


                String request;//запрос от клиента

                String randomQuote;//случайная цитата

                while (true) {
                    randomQuote = GetRandomQuotes.getRandomQuote();
                    bf.append(randomQuote);
                    out.println(randomQuote);//отправляем сообщением данную цитату
                    System.out.println(randomQuote); //на сервер тоже выводим цитату
                    countQuote++; //считаем количество цитат. если больше 5 - разрываем соединение
                    request = reader.readLine();
                    if (request.equalsIgnoreCase("end") || countQuote >= 5) { //если клиент пишет end останавливаем выдачу цитат
                        timeDisconnection = new Date();
                        break;
                    }
                    System.out.println(request);
                }
                String info = countQuote + "\n" + nameClient + "\n" + timeConnection + "\n" + timeDisconnection;
                System.out.println(info); //выводим в консоль данные о соединении
                bf.append(info); //попытка добавить данные о соединении в файл

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


    }
}


