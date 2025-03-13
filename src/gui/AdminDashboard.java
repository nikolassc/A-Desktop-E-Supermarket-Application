package gui;

import javax.swing.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel label = new JLabel("Welcome to Admin Dashboard!");
        label.setBounds(100, 100, 300, 25);
        add(label);

        setVisible(true);
    }
}


