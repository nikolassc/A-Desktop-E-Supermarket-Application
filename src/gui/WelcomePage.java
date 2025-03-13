package gui;

import javax.swing.*;
import java.awt.*;



    public class WelcomePage {
        private JFrame frame;
        private JLabel welcomeLabel;
        private JPanel panel;

        public WelcomePage(String role, String userID) {
            frame = new JFrame("Welcome Page");
            welcomeLabel = new JLabel("Welcome " + role + ": " + userID, JLabel.CENTER);

            // Ορισμός του panel που θα κρατάει το welcomeLabel
            panel = new JPanel();
            panel.setLayout(new BorderLayout());

            // Προσθήκη padding από πάνω για να κατεβάσουμε το μήνυμα πιο χαμηλά
            panel.add(welcomeLabel, BorderLayout.CENTER);

            // Ρύθμιση του frame
            frame.setLayout(new BorderLayout());
            frame.add(panel, BorderLayout.CENTER);

            // Ρυθμίσεις για το JLabel
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
            welcomeLabel.setForeground(Color.BLACK);

            // Ρύθμιση του μεγέθους του παραθύρου
            frame.setSize(400, 300);

            // Κεντραρισμένη εμφάνιση της σελίδας
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }

        public static void main(String[] args) {
            new gui.WelcomePage("Admin", "leke"); // Παράδειγμα χρήστη με ρόλο admin και userID
        }
    }

