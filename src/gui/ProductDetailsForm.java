package gui;

import api.Product;
import javax.swing.*;
import java.awt.*;

public class ProductDetailsForm extends JFrame {
    public ProductDetailsForm(Product product){
        setTitle("Πληροφορίες Προϊόντος");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Τίτλος:"));
        add(new JLabel(product.getTitle()));

        add(new JLabel("Περιγραφή:"));
        add(new JLabel(product.getDescription()));

        add(new JLabel("Κατηγορία:"));
        add(new JLabel(product.getCategory()));

        add(new JLabel("Υποκατηγορία:"));
        add(new JLabel(product.getSubcategory()));

        add(new JLabel("Τιμή:"));
        add(new JLabel(String.format("%.2f €", product.getPrice())));

        add(new JLabel("Ποσότητα:"));
        add(new JLabel(product.getQuantity() + " " + product.getUnit()));

        setVisible(true);
    }
}
