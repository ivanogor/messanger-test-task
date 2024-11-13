package com.ivanogor.messenger.client;

import javax.swing.*;

public class MainFrame extends JFrame {
    private final String currentUser;
    private final ChatPanel chatPanel;

    public MainFrame(String currentUser) {
        this.currentUser = currentUser;

        setTitle("Мессенджер");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(new ContactListPanel(this));
        chatPanel = new ChatPanel(this);
        splitPane.setRightComponent(chatPanel);

        add(splitPane);
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public ChatPanel getChatPanel() {
        return chatPanel;
    }
}