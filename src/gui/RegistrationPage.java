package gui;

import api.IDandPasswords;
import api.Role;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



import api.IDandPasswords;
import api.Role;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationPage {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField userIDField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JPanel registrationPanel;
    private IDandPasswords idandPasswords;

    public RegistrationPage(IDandPasswords idandPasswords) {
        this.idandPasswords = idandPasswords;

        // Δημιουργία πεδίων και κουμπιών (αν δεν χρησιμοποιείτε Swing designer)
        registrationPanel = new JPanel();
        registrationPanel.setLayout(new BoxLayout(registrationPanel, BoxLayout.Y_AXIS));

        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        userIDField = new JTextField(20);
        passwordField = new JPasswordField(20);
        registerButton = new JButton("Register");

        // Προσθήκη πεδίων και κουμπιού στο JPanel
        registrationPanel.add(new JLabel("First Name:"));
        registrationPanel.add(firstNameField);
        registrationPanel.add(new JLabel("Last Name:"));
        registrationPanel.add(lastNameField);
        registrationPanel.add(new JLabel("User ID:"));
        registrationPanel.add(userIDField);
        registrationPanel.add(new JLabel("Password:"));
        registrationPanel.add(passwordField);
        registrationPanel.add(registerButton);

        // Συνδέστε τον Listener για το κουμπί
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String userID = userIDField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                // Έλεγχος αν όλα τα πεδία είναι γεμάτα
                if (firstName.isEmpty() || lastName.isEmpty() || userID.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Έλεγχος αν το userID υπάρχει ήδη
                if (idandPasswords.isUserIDTaken(userID)) {
                    JOptionPane.showMessageDialog(null, "User ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Δημιουργία χρήστη με τον ρόλο Customer
                idandPasswords.addUser(firstName, lastName, userID, password, Role.CUSTOMER);

                // Εμφάνιση μηνύματος επιτυχίας
                JOptionPane.showMessageDialog(null, "Registration successful!");

                // Κλείσιμο του παραθύρου εγγραφής
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(registrationPanel);
                frame.dispose();  // Κλείνει το παράθυρο εγγραφής
            }
        });
    }

    public JPanel getRegistrationPanel() {
        return registrationPanel;
    }
}
