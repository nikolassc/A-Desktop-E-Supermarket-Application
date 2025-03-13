package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import api.*;
import gui.ProductForm;
import gui.ProductFormAdmin;
import gui.RegistrationPage;


public class LoginPage {
    private JTextField userIDField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JPanel loginPanel;
    private IDandPasswords idandPasswords;

    public LoginPage(IDandPasswords idandPasswords) {
        this.idandPasswords = idandPasswords;

        // Αρχικοποίηση των στοιχείων UI
        userIDField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        loginPanel = new JPanel();

        // Προσθήκη των στοιχείων στο JPanel
        loginPanel.add(new JLabel("User ID:"));
        loginPanel.add(userIDField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

        // Listener για το κουμπί Login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userIDField.getText();
                String password = new String(passwordField.getPassword());

                if (idandPasswords.validateLogin(userID, password)) {
                    Role role = idandPasswords.getUserRole(userID);

                    // Αποθήκευση του User ID στην SessionManager
                    SessionManager.setUserId(userID);

                    // Αν ο χρήστης είναι Admin, ανοίγει το ProductFormAdmin
                    if (role == Role.ADMIN) {
                        navigateToAdminProductForm();
                    }
                    // Αν ο χρήστης είναι Customer, ανοίγει το ProductForm
                    else if (role == Role.CUSTOMER) {
                        navigateToCustomerProductForm(userID); // Περνάμε το userId
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
                }
            }
        });

        // Listener για το κουμπί Register
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateToRegistrationPage();
            }
        });
    }

    // Μέθοδος που ανοίγει το ProductFormAdmin για Admin
    private void navigateToAdminProductForm() {
        JOptionPane.showMessageDialog(null, "Welcome Admin!");
        SupermarketAPI supermarketAPI = new SupermarketAPI();
        ShoppingCart cart = new ShoppingCart();
        new ProductFormAdmin(supermarketAPI);
        dispose();
    }

    // Μέθοδος που ανοίγει το ProductForm για Customer
    private void navigateToCustomerProductForm(String userId) {
        JOptionPane.showMessageDialog(null, "Welcome Customer!");
        SupermarketAPI supermarketAPI = new SupermarketAPI();
        ShoppingCart cart = new ShoppingCart();
        new ProductForm(Role.CUSTOMER, supermarketAPI, cart, userId);
        dispose();
    }

    // Μέθοδος για την πλοήγηση στη σελίδα εγγραφής
    private void navigateToRegistrationPage() {
        JFrame frame = new JFrame("Registration");
        RegistrationPage registrationPage = new RegistrationPage(idandPasswords);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(registrationPage.getRegistrationPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Μέθοδος για να επιστρέψετε το login panel
    public JPanel getLoginPanel() {
        return loginPanel;
    }

    // Μέθοδος για να κλείσετε την login page
    private void dispose() {
        ((JFrame) loginPanel.getTopLevelAncestor()).dispose();
    }
}
