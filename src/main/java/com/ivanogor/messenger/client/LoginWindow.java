package com.ivanogor.messenger.client;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {
    private final JTextField loginField;
    private final JPasswordField passwordField;

    public LoginWindow() {
        setTitle("Авторизация");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        panel.add(new JLabel("Логин:"));
        loginField = new JTextField();
        panel.add(loginField);

        panel.add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = getButton();
        panel.add(loginButton);

        add(panel);
    }

    private JButton getButton() {
        JButton loginButton = new JButton("Войти");
        loginButton.addActionListener(e -> {
            String login = loginField.getText();
            String password = new String(passwordField.getPassword());


            if (isValidCredentials(login, password)) {
                dispose();
                new MainFrame(login).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(LoginWindow.this, "Неверный логин или пароль");
            }
        });
        return loginButton;
    }

    private boolean isValidCredentials(String login, String password) {
        return "user1".equals(login) && "pass1".equals(password) ||
                "user2".equals(login) && "pass2".equals(password) ||
                "user3".equals(login) && "pass3".equals(password);
    }
}