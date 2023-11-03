import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClient {
    private Socket clientSocket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private String clientIP;

    public ChatClient() {
        try {
            clientSocket = new Socket("<ip_address>", 2323);
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Mendapatkan alamat IP pengguna
            clientIP = InetAddress.getLocalHost().getHostAddress();

            // Membuat GUI
            JFrame frame = new JFrame("Chat Client - IP " + clientIP);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);

            chatArea = new JTextArea();
            chatArea.setEditable(false);

            // Inisialisasi chatArea
            JScrollPane scrollPane = new JScrollPane(chatArea);
            frame.add(scrollPane, BorderLayout.CENTER);

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new BorderLayout());

            messageField = new JTextField();
            messageField.setText("Send Message");
            inputPanel.add(messageField, BorderLayout.CENTER);

            sendButton = new JButton("Kirim");
            inputPanel.add(sendButton, BorderLayout.EAST);

            frame.add(inputPanel, BorderLayout.SOUTH);

            messageField.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                    if (messageField.getText().equals("Send Message")) {
                        messageField.setText("");
                    }
                }

                public void focusLost(FocusEvent e) {
                    if (messageField.getText().isEmpty()) {
                        messageField.setText("Send Message");
                    }
                }
            });

            messageField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sendMessage(messageField.getText());
                }
            });

            sendButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sendMessage(messageField.getText());
                }
            });

            frame.setVisible(true);

            messageField.requestFocusInWindow();

            Thread receiveThread = new Thread(new ReceiveMessage());
            receiveThread.start();

            chatArea.append("Connected To Server. Send A Message.\n");

        } catch (ConnectException e) {
            System.out.println("Server disconnected. Please try again later.");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server disconnected. Please try again later.");
            System.exit(0);
        }
    }

    private void sendMessage(String message) {
        try {
            writer.write(message + "\n");
            writer.flush();
            messageField.setText("");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server disconnected. Please try again later.");
            System.exit(0);
        }
    }

    private class ReceiveMessage implements Runnable {
        @Override
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    if (message.equals("Server disconnected")) {
                        System.out.println("Server disconnected. Please try again later.");
                        System.exit(0);
                    } else {
                        chatArea.append(message + "\n");
                    }
                }
            } catch (IOException e) {
                System.out.println("Server disconnected. Please try again later.");
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChatClient();
            }
        });
    }
}
