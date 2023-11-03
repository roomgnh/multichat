import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static List<PrintWriter> clientWriters = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(2323);
            System.out.println("Server is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.add(writer);

                InetAddress clientAddress = clientSocket.getInetAddress();
                String clientName = "Client-" + clientAddress.getHostAddress(); // Menggunakan alamat IP sebagai nama

                Thread clientThread = new Thread(new ClientHandler(clientSocket, writer, clientName));
                clientThread.start();

                System.out.println(clientName + " Connected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter writer;
        private String clientName;

        public ClientHandler(Socket socket, PrintWriter writer, String clientName) {
            this.socket = socket;
            this.writer = writer;
            this.clientName = clientName;
        }

        @Override
        public void run() {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;

                while ((message = reader.readLine()) != null) {
                    System.out.println(clientName + " Message: " + message);
                    broadcastMessage(clientName + ": " + message);
                }
            } catch (IOException e) {
                System.out.println(clientName + " disconnected.");
                clientWriters.remove(writer);
                broadcastMessage(clientName + " disconnected."); // Mengirim pesan pemberitahuan ke semua klien
            }
        }

        private void broadcastMessage(String message) {
            for (PrintWriter clientWriter : clientWriters) {
                clientWriter.println(message);
            }
        }
    }
}
