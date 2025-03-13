package gui;

import api.SessionManager;
import api.ShoppingCart;
import api.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class CartForm extends JFrame {
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private ShoppingCart cart;
    private JLabel totalCostLabel;

    public CartForm(ShoppingCart cart) {
        this.cart = cart;

        setTitle("Καλάθι Αγορών");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Δημιουργία πίνακα με DefaultTableModel
        String[] columnNames = {"Όνομα Προϊόντος", "Ποσότητα", "Τιμή ανά Μονάδα", "Σύνολο"};
        tableModel = new DefaultTableModel(columnNames, 0);
        cartTable = new JTable(tableModel);
        cartTable.setDefaultEditor(Object.class, null); // Read-only
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Μία επιλογή μόνο

        add(new JScrollPane(cartTable), BorderLayout.CENTER);

        // Ενημέρωση του πίνακα με προϊόντα
        updateTable();

        // Κάτω μέρος - Συνολικό Κόστος + Κουμπιά
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2, 1));

        totalCostLabel = new JLabel("Συνολικό Κόστος: " + cart.calculateTotalCost() + "€", SwingConstants.CENTER);
        bottomPanel.add(totalCostLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton updateQuantityButton = new JButton("Αλλαγή Ποσότητας");
        JButton removeProductButton = new JButton("Διαγραφή Προϊόντος");
        JButton checkoutButton = new JButton("Checkout");

        buttonPanel.add(updateQuantityButton);
        buttonPanel.add(removeProductButton);
        buttonPanel.add(checkoutButton);
        bottomPanel.add(buttonPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        // Λειτουργίες Κουμπιών

        // Αλλαγή Ποσότητας
        updateQuantityButton.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Παρακαλώ επιλέξτε ένα προϊόν.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product product = getProductByRow(selectedRow);
            String newQuantityStr = JOptionPane.showInputDialog(this, "Νέα Ποσότητα:");
            try {
                int newQuantity = Integer.parseInt(newQuantityStr);
                cart.updateQuantity(product, newQuantity);
                updateTable();
                totalCostLabel.setText("Συνολικό Κόστος: " + cart.calculateTotalCost() + "€");
                JOptionPane.showMessageDialog(this, "Η ποσότητα ενημερώθηκε!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage());
            }
        });

        // Διαγραφή Προϊόντος
        removeProductButton.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Παρακαλώ επιλέξτε ένα προϊόν.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product product = getProductByRow(selectedRow);
            cart.removeFromCart(product);
            updateTable();
            totalCostLabel.setText("Συνολικό Κόστος: " + cart.calculateTotalCost() + "€");
            JOptionPane.showMessageDialog(this, "Το προϊόν αφαιρέθηκε!");
        });

        // Checkout
        checkoutButton.addActionListener(e -> {
            try {
                updateProductQuantities(); // Ενημέρωση ποσότητας στο αρχείο
                String userId = SessionManager.getUserId();
                if (userId == null || userId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Το User ID δεν είναι διαθέσιμο. Πραγματοποιήστε σύνδεση.");
                    return;
                }
                cart.checkout(userId); // Χρησιμοποίησε το αποθηκευμένο User ID
                updateTable();
                totalCostLabel.setText("Συνολικό Κόστος: 0.0€");
                JOptionPane.showMessageDialog(this, "Η παραγγελία ολοκληρώθηκε!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    // Μέθοδος για ενημέρωση του πίνακα
    private List<Product> displayedProducts = new ArrayList<>();


    private void updateTable() {
        tableModel.setRowCount(0); // Καθαρισμός του πίνακα
        displayedProducts.clear(); // Καθαρισμός της λίστας των προϊόντων

        System.out.println("=== Ενημέρωση Πίνακα ===");

        for (Map.Entry<Product, Integer> entry : cart.getCartItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            displayedProducts.add(product);

            // Εμφάνιση περιεχομένων για debugging
            System.out.println("Προϊόν: " + product.getTitle() + ", Ποσότητα: " + quantity);

            tableModel.addRow(new Object[]{
                    product.getTitle(),
                    product.getPrice(),
                    quantity,
                    product.getPrice() * quantity
            });
        }

        System.out.println("=== Τέλος Ενημέρωσης Πίνακα ===");
    }




    private Product getProductByRow(int row) {
        if (row >= 0 && row < displayedProducts.size()) {
            return displayedProducts.get(row);
        }
        return null;
    }

    // Ενημέρωση ποσότητας στο αρχείο
    private void updateProductQuantities() throws Exception {
        // Άνοιγμα αρχείου για ανάγνωση
        BufferedReader reader = new BufferedReader(new FileReader("files/products.txt"));
        StringBuilder updatedContent = new StringBuilder(); // Κείμενο για ενημέρωση

        String line;
        while ((line = reader.readLine()) != null) {
            // Ελέγχει αν η γραμμή ξεκινάει με "Τίτλος: "
            if (line.startsWith("Τίτλος: ")) {
                String title = line.substring(8).trim(); // Παίρνει τον τίτλο του προϊόντος
                updatedContent.append(line).append("\n"); // Προσθέτει τον τίτλο

                // Διαβάζει τις επόμενες γραμμές για να βρει την ποσότητα
                String description = reader.readLine();
                String category = reader.readLine();
                String subcategory = reader.readLine();
                String price = reader.readLine();
                String quantityLine = reader.readLine(); // Ποσότητα

                // Προσθέτει τις γραμμές χωρίς αλλαγές
                updatedContent.append(description).append("\n");
                updatedContent.append(category).append("\n");
                updatedContent.append(subcategory).append("\n");
                updatedContent.append(price).append("\n");

                // Ελέγχει αν το προϊόν είναι στο καλάθι
                boolean updated = false; // Flag για το αν ενημερώθηκε
                for (Map.Entry<Product, Integer> entry : cart.getCartItems().entrySet()) {
                    Product product = entry.getKey();
                    int purchasedQuantity = entry.getValue();

                    // Αν ο τίτλος ταιριάζει, ενημερώνει την ποσότητα
                    if (product.getTitle().equals(title)) {
                        String[] parts = quantityLine.split(": "); // Διαχωρισμός γραμμής ποσότητας
                        int availableQuantity = Integer.parseInt(parts[1].split(" ")[0]); // Αρχική ποσότητα

                        // Μείωση ποσότητας
                        int newQuantity = availableQuantity - purchasedQuantity;

                        // Ενημέρωση ποσότητας στη γραμμή
                        quantityLine = "Ποσότητα: " + newQuantity + " kg";
                        updated = true;
                        break;
                    }
                }

                // Αν δεν ενημερώθηκε, διατηρεί την ποσότητα ως έχει
                updatedContent.append(quantityLine).append("\n");
            } else {
                // Προσθέτει οποιαδήποτε άλλη γραμμή χωρίς αλλαγές
                updatedContent.append(line).append("\n");
            }
        }
        reader.close();

        // Γράφει τις αλλαγές πίσω στο αρχείο
        BufferedWriter writer = new BufferedWriter(new FileWriter("files/products.txt"));
        writer.write(updatedContent.toString());
        writer.close();
    }
}
