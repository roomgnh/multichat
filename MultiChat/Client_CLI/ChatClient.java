import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("<ip_address>", 2323);
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Scanner scanner = new Scanner(System.in);

            Thread receiveThread = new Thread(new ReceiveMessage(reader));
            receiveThread.start();

            InetAddress clientAddress = clientSocket.getInetAddress();
            String clientName = "Client-" + clientAddress.getHostAddress(); // Menggunakan alamat IP sebagai nama
            System.out.println("Connected To Server . Your Ip " + InetAddress.getLocalHost().getHostAddress());

            while (true) {
                String message = scanner.nextLine();
                writer.println(message); // Mengirim pesan dengan nama pengirim sebagai alamat IP
            }
        } catch (ConnectException e) {
            System.out.println("Server disconnected. Please try again later.");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ReceiveMessage implements Runnable {
        private BufferedReader reader;

        public ReceiveMessage(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    if (message.equals("Server disconnected")) {
                        System.out.println("Server disconnected. Please try again later.");
                        System.exit(0);
                    } else {
                        System.out.println(message);
                    }
                }
            } catch (IOException e) {
                System.out.println("Server disconnected. Please try again later.");
                System.exit(0);
            }
        }
    }
}