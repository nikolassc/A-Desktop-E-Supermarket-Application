package gui;

import api.Product;
import api.ShoppingCart;
import javax.swing.*;
import java.awt.*;

public class AddCartForm extends JFrame {
    public AddCartForm(Product product, ShoppingCart cart) {
        setTitle("Προσθήκη στο Καλάθι");
        setSize(300, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Τίτλος:"));
        add(new JLabel(product.getTitle()));

        add(new JLabel("Διαθέσιμη Ποσότητα:"));
        add(new JLabel(product.getQuantity() + " " + product.getUnit()));

        add(new JLabel("Ποσότητα προς αγορά:"));
        JTextField quantityField = new JTextField();
        add(quantityField);

        JButton addButton = new JButton("Προσθήκη");
        add(addButton);

        addButton.addActionListener(e -> {
            try {
                int quantity = Integer.parseInt(quantityField.getText().trim());
                cart.addCart(product, quantity);
                JOptionPane.showMessageDialog(this, "Το προϊόν προστέθηκε στο καλάθι!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
