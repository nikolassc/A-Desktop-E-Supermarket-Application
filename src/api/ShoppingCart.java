package api;

import java.io.*;
import java.util.*;

public class ShoppingCart {
    private Map<Product, Integer> cart;
    private double totalCost;

    public ShoppingCart() {
        this.cart = new HashMap<>();
        this.totalCost = 0;
    }

    // Προσθήκη προϊόντος
    public void addCart(Product product, int quantity) {
        if (quantity > product.getQuantity()) {
            throw new IllegalArgumentException("Η ζητούμενη ποσότητα υπερβαίνει το διαθέσιμο όριο.");
        }

        // Δημιουργία αντιγράφου
        Product cartProduct = new Product(
                product.getTitle(),
                product.getDescription(),
                product.getCategory(),
                product.getSubcategory(),
                product.getPrice(),
                quantity, // Ποσότητα για το καλάθι
                product.getUnit()
        );

        cart.put(cartProduct, cart.getOrDefault(cartProduct, 0) + quantity);
        product.setQuantity(product.getQuantity() - quantity);
        totalCost += quantity * product.getPrice();
    }


    // Διαγραφή προϊόντος
    public void removeFromCart(Product product) {
        if (cart.containsKey(product)) {
            int quantity = cart.remove(product);
            product.setQuantity(product.getQuantity() + quantity);
            totalCost -= quantity * product.getPrice();
        } else {
            throw new IllegalArgumentException("Το προϊόν δεν υπάρχει στο καλάθι.");
        }
    }

    // Ενημέρωση ποσότητας
    public void updateQuantity(Product product, int newQuantity) {
        if (!cart.containsKey(product)) {
            throw new IllegalArgumentException("Το προϊόν δεν υπάρχει στο καλάθι.");
        }
        int currentQuantity = cart.get(product);
        int diff = newQuantity - currentQuantity;
        if (diff > product.getQuantity()) {
            throw new IllegalArgumentException("Η νέα ποσότητα υπερβαίνει το διαθέσιμο όριο.");
        }
        cart.put(product, newQuantity);
        product.setQuantity(product.getQuantity() - diff);
        totalCost += diff * product.getPrice();
    }

    // Υπολογισμός συνολικού κόστους
    public double calculateTotalCost() {
        return totalCost;
    }

    // Ολοκλήρωση παραγγελίας
    public void checkout(String userId) {
        if (cart.isEmpty()) {
            throw new IllegalStateException("Το καλάθι είναι άδειο.");
        }
        saveOrderHistory(userId);
        cart.clear();
        totalCost = 0;
    }

    // Επιστροφή προϊόντων
    public Map<Product, Integer> getCartItems() {
        return new HashMap<>(cart); // Επιστρέφουμε αντίγραφο για να προστατεύσουμε την εσωτερική δομή
    }

    // Αποθήκευση παραγγελίας στο ιστορικό χρήστη
    private void saveOrderHistory(String userId) {
        String fileName = "files/order_history_" + userId + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write("Παραγγελία: \n");

            for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                writer.write(product.getTitle() + " - Ποσότητα: " + quantity + " " + product.getUnit() + ", Σύνολο: " + (product.getPrice() * quantity) + "€\n");
            }

            writer.write("Συνολικό Κόστος: " + totalCost + "€\n");
            writer.write("----------------------------------------------------\n");
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την καταγραφή της παραγγελίας στο ιστορικό: " + e.getMessage());
        }
    }
}
