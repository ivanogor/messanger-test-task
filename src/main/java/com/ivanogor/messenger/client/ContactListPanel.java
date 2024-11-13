package com.ivanogor.messenger.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContactListPanel extends JPanel {
    private final JList<String> contactList;

    public ContactListPanel(MainFrame mainFrame) {

        setLayout(new BorderLayout());

        contactList = new JList<>(new String[]{"user1", "user2", "user3"});
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedContact = contactList.getSelectedValue();
                    if (selectedContact != null) {
                        System.out.println("Selected contact: " + selectedContact);
                        // Используем getChatPanel() для получения ChatPanel
                        ChatPanel chatPanel = mainFrame.getChatPanel();
                        if (chatPanel != null) {
                            chatPanel.setCurrentContact(selectedContact);
                        } else {
                            System.out.println("ChatPanel not found");
                        }
                    }
                }
            }
        });

        add(new JScrollPane(contactList), BorderLayout.CENTER);
    }
}