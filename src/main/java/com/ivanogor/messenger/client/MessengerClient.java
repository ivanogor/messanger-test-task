package com.ivanogor.messenger.client;

import com.formdev.flatlaf.FlatDarkLaf;

public class MessengerClient {
    public static void main(String[] args) {
        FlatDarkLaf.setup();

        new LoginWindow().setVisible(true);
    }
}