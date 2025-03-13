package gui;

import api.Product;
import api.SupermarketAPI;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class SearchBar extends JPanel {
    private JTextField titleField;
    private JComboBox<String> categoryCombo, subcategoryCombo;
    private JButton searchButton;
    private JComboBox<String> productCombo;
    private SupermarketAPI supermarketAPI;

    public SearchBar(SupermarketAPI supermarketAPI, JComboBox<String> productCombo) {
        this.supermarketAPI = supermarketAPI;
        this.productCombo = productCombo;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Τίτλος
        JLabel titleLabel = new JLabel("Τίτλος:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        titleField = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 1;
        add(titleField, gbc);

        // Κατηγορία
        JLabel categoryLabel = new JLabel("Κατηγορία:");
        gbc.gridx = 2;
        gbc.weightx = 0;
        add(categoryLabel, gbc);

        categoryCombo = new JComboBox<>(supermarketAPI.getCategories().keySet().toArray(new String[0]));
        categoryCombo.addActionListener(e -> updateSubcategories());
        gbc.gridx = 3;
        add(categoryCombo, gbc);

        // Υποκατηγορία
        JLabel subcategoryLabel = new JLabel("Υποκατηγορία:");
        gbc.gridx = 4;
        add(subcategoryLabel, gbc);

        subcategoryCombo = new JComboBox<>();
        updateSubcategories();
        gbc.gridx = 5;
        add(subcategoryCombo, gbc);

        // Κουμπί Αναζήτησης
        searchButton = new JButton("Αναζήτηση");
        gbc.gridx = 6;
        gbc.weightx = 0;
        add(searchButton, gbc);

        // Λογική Αναζήτησης
        searchButton.addActionListener(e -> performSearch());
    }

    private void updateSubcategories() {
        String selectedCategory = (String) categoryCombo.getSelectedItem();
        subcategoryCombo.removeAllItems();

        if (selectedCategory != null) {
            List<String> subcategories = supermarketAPI.getCategories().get(selectedCategory);
            if (subcategories != null) {
                for (String subcategory : subcategories) {
                    subcategoryCombo.addItem(subcategory);
                }
            }
        }
    }

    private void performSearch() {
        String titleKeyword = titleField.getText().trim().toLowerCase();
        String category = (String) categoryCombo.getSelectedItem();
        String subcategory = (String) subcategoryCombo.getSelectedItem();

        List<Product> products = supermarketAPI.getProducts();
        List<Product> filteredProducts = products.stream()
                .filter(product -> (titleKeyword.isEmpty() || product.getTitle().toLowerCase().contains(titleKeyword))
                        && (category == null || product.getCategory().equalsIgnoreCase(category))
                        && (subcategory == null || product.getSubcategory().equalsIgnoreCase(subcategory)))
                .collect(Collectors.toList());

        productCombo.removeAllItems();
        for (Product product : filteredProducts) {
            productCombo.addItem(product.getTitle());
        }

        if (filteredProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Δεν βρέθηκαν προϊόντα.", "Αναζήτηση", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

