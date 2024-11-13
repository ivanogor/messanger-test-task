package com.ivanogor.messenger.client;

import com.ivanogor.messenger.server.Correspondent;
import com.ivanogor.messenger.common.HiPacket;
import com.ivanogor.messenger.common.MessagePacket;
import com.ivanogor.messenger.common.Packet;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatPanel extends JPanel {
    private String currentContact;
    private final JTextArea chatArea;
    private final JTextField messageField;
    private PrintWriter writer;
    private BufferedReader reader;
    private final Map<String, List<String>> messagesMap;

    public ChatPanel(MainFrame mainFrame) {
        this.messagesMap = new HashMap<>();

        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        bottomPanel.add(messageField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Отправить");
        sendButton.addActionListener(e -> {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                sendMessage(message);
                messageField.setText("");
            }
        });
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        try {
            System.out.println("Trying to connect to server...");
            Socket socket = new Socket("localhost", 10001);
            System.out.println("Connected to server.");
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Отправка пакета авторизации
            HiPacket hiPacket = new HiPacket();
            hiPacket.login = mainFrame.getCurrentUser();
            hiPacket.writePacket(writer);
            System.out.println("Sent authorization packet.");

            // Запуск потока для чтения сообщений с сервера
            new Thread(() -> {
                try {
                    while (true) {
                        Packet packet = Packet.readPacket(reader);
                        if (packet instanceof MessagePacket messagePacket) {
                            System.out.println("Received message: " + messagePacket.text);
                            addMessage(messagePacket.correspondentId + "", messagePacket.text);
                            chatArea.append(messagePacket.correspondentId + ": " + messagePacket.text + "\n");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setCurrentContact(String contact) {
        this.currentContact = contact;
        chatArea.setText("Чат с " + contact + "\n");
        restoreMessages(contact);
    }

    private void sendMessage(String message) {
        if (currentContact != null) {
            chatArea.append("Вы: " + message + "\n");
            addMessage(currentContact, "Вы: " + message);
            Correspondent correspondent = Correspondent.findCorrespondent(currentContact);
            if (correspondent != null) {
                MessagePacket messagePacket = new MessagePacket();
                messagePacket.correspondentId = correspondent.id;
                messagePacket.text = message;
                System.out.println("Sending message: " + message);
                messagePacket.writePacket(writer);
            } else {
                System.out.println("Correspondent not found: " + currentContact);
            }
        }
    }

    private void addMessage(String contact, String message) {
        System.out.println("Adding message to contact: " + contact + ", message: " + message);
        messagesMap.computeIfAbsent(contact, k -> new ArrayList<>()).add(message);
    }

    private void restoreMessages(String contact) {
        System.out.println("Restoring messages for contact: " + contact);
        List<String> messages = messagesMap.get(contact);
        if (messages != null) {
            for (String message : messages) {
                System.out.println("Restored message: " + message);
                chatArea.append(message + "\n");
            }
        }
    }
}