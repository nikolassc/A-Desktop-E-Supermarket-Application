package gui;

import api.Product;
import api.Role;
import api.ShoppingCart;
import api.SupermarketAPI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProductForm extends JFrame {
    private JComboBox<String> productCombo;
    private JButton viewDetailsButton, addProductButton, editProductButton, addToCartButton, viewCartButton, historyButton;
    private SupermarketAPI supermarketAPI;
    private Role userRole;
    private ShoppingCart cart;
    private String userId; // Το userId είναι String

    public ProductForm(Role userRole, SupermarketAPI supermarketAPI, ShoppingCart cart, String userId) {
        this.userRole = userRole;
        this.supermarketAPI = supermarketAPI;
        this.cart = cart;
        this.userId = userId; // Αποθήκευση του userId ως String

        setTitle("Supermarket e-shop");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Αναζήτηση Προϊόντων
        JLabel searchTitleLabel = new JLabel("Αναζήτηση Προϊόντων:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(searchTitleLabel, gbc);

        JTextField searchField = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 1;
        add(searchField, gbc);

        JComboBox<String> categoryCombo = new JComboBox<>();
        categoryCombo.addItem("");
        supermarketAPI.getCategories().keySet().forEach(categoryCombo::addItem);
        gbc.gridx = 2;
        gbc.weightx = 0;
        add(categoryCombo, gbc);

        JComboBox<String> subcategoryCombo = new JComboBox<>();
        subcategoryCombo.addItem("");
        gbc.gridx = 3;
        add(subcategoryCombo, gbc);

        JButton searchButton = new JButton("Αναζήτηση");
        gbc.gridx = 4;
        add(searchButton, gbc);

        categoryCombo.addActionListener(e -> {
            subcategoryCombo.removeAllItems();
            subcategoryCombo.addItem("");
            String selectedCategory = (String) categoryCombo.getSelectedItem();
            if (selectedCategory != null && !selectedCategory.isEmpty()) {
                List<String> subcategories = supermarketAPI.getCategories().get(selectedCategory);
                if (subcategories != null) {
                    for (String subcategory : subcategories) {
                        subcategoryCombo.addItem(subcategory);
                    }
                }
            }
        });

        searchButton.addActionListener(e -> {
            String titleKeyword = searchField.getText().trim().toLowerCase();
            String category = (String) categoryCombo.getSelectedItem();
            String subcategory = (String) subcategoryCombo.getSelectedItem();

            List<Product> products = supermarketAPI.getProducts();
            productCombo.removeAllItems();

            for (Product product : products) {
                boolean matchesTitle = titleKeyword.isEmpty() || product.getTitle().toLowerCase().contains(titleKeyword);
                boolean matchesCategory = category == null || category.isEmpty() || product.getCategory().equalsIgnoreCase(category);
                boolean matchesSubcategory = subcategory == null || subcategory.isEmpty() || product.getSubcategory().equalsIgnoreCase(subcategory);

                if (matchesTitle && matchesCategory && matchesSubcategory) {
                    productCombo.addItem(product.getTitle());
                }
            }

            if (productCombo.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "Δεν βρέθηκαν προϊόντα.", "Αναζήτηση", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JLabel productLabel = new JLabel("Επιλέξτε Προϊόν:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(productLabel, gbc);

        productCombo = new JComboBox<>();
        updateProducts(supermarketAPI.getProducts());
        gbc.gridx = 1;
        add(productCombo, gbc);

        viewDetailsButton = new JButton("Προβολή Πληροφοριών");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(viewDetailsButton, gbc);

        viewDetailsButton.addActionListener(e -> {
            String selectedProduct = (String) productCombo.getSelectedItem();
            Product product = supermarketAPI.getProductByTitle(selectedProduct);
            if (product != null) {
                new ProductDetailsForm(product);
            } else {
                JOptionPane.showMessageDialog(this, "Το προϊόν δεν βρέθηκε.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            }
        });

        if (userRole == Role.ADMIN) {
            addProductButton = new JButton("Καταχώρηση Προϊόντων");
            gbc.gridy = 3;
            add(addProductButton, gbc);

            addProductButton.addActionListener(e -> new ProductFormAdmin(supermarketAPI));

            editProductButton = new JButton("Επεξεργασία Προϊόντος");
            gbc.gridy = 4;
            add(editProductButton, gbc);

            editProductButton.addActionListener(e -> {
                String selectedProduct = (String) productCombo.getSelectedItem();
                Product product = supermarketAPI.getProductByTitle(selectedProduct);
                if (product != null) {
                    new ProductFormAdmin(supermarketAPI, product);
                } else {
                    JOptionPane.showMessageDialog(this, "Το προϊόν δεν βρέθηκε.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        if (userRole == Role.CUSTOMER) {
            addToCartButton = new JButton("Προσθήκη στο Καλάθι");
            gbc.gridy = 3;
            add(addToCartButton, gbc);

            addToCartButton.addActionListener(e -> {
                String selectedProduct = (String) productCombo.getSelectedItem();
                Product product = supermarketAPI.getProductByTitle(selectedProduct);
                if (product != null) {
                    try {
                        String quantityStr = JOptionPane.showInputDialog("Ποσότητα:");
                        int quantity = Integer.parseInt(quantityStr);
                        cart.addCart(product, quantity);
                        JOptionPane.showMessageDialog(this, "Προστέθηκε στο καλάθι.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JButton cartButton = new JButton("Καλάθι");
            gbc.gridy = 4;
            add(cartButton, gbc);

            cartButton.addActionListener(e -> {
                new CartForm(cart);
            });

            historyButton = new JButton("Ιστορικό Παραγγελιών");
            gbc.gridy = 5;
            add(historyButton, gbc);

            historyButton.addActionListener(e -> {
                new OrderHistoryForm(userId); // Περνάμε το userId ως String
            });

            setVisible(true);
        }
    }

    private void updateProducts(java.util.List<Product> products) {
        productCombo.removeAllItems();
        for (Product product : products) {
            productCombo.addItem(product.getTitle());
        }
    }
}
