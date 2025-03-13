package gui;

import javax.swing.*;

public class CustomerDashboard extends JFrame {
    public CustomerDashboard() {
        setTitle("Customer Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel label = new JLabel("Welcome to Customer Dashboard!");
        label.setBounds(100, 100, 300, 25);
        add(label);

        setVisible(true);
    }
}

