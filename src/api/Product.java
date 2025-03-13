package api;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Product {
    private String title; // Τίτλος
    private String description; // Περιγραφή προϊόντος
    private String category; // Κατηγορία
    private String subcategory; // Υποκατηγορία
    private double price; // Τιμή προϊόντος
    private int quantity; // Διαθέσιμα τεμάχια ή κιλά του προϊόντος
    private String unit; // Μονάδα μέτρησης

    // Constructor
    public Product(String title, String description, String category, String subcategory, double price, int quantity, String unit) {
        if (title == null || title.isEmpty())
            throw new IllegalArgumentException("Ο τίτλος είναι υποχρεωτικός"); // Έλεγχος για εισαγωγή τίτλου
        if (price < 0)
            throw new IllegalArgumentException("Η τιμή πρέπει να είναι θετικός αριθμός"); // Έλεγχος για λάθος τιμή προϊόντος
        if (quantity < 0)
            throw new IllegalArgumentException("Η ποσότητα δεν μπορεί να είναι αρνητική"); // Έλεγχος για λάθος ποσότητα προϊόντος
        if (unit == null || (!unit.equals("τεμάχια") && !unit.equals("kg")))
            throw new IllegalArgumentException("Η μονάδα πρέπει να είναι 'τεμάχια' ή 'κιλά'"); // Έλεγχος για αποδεκτή μονάδα

        this.title = title;
        this.description = description;
        this.category = category;
        this.subcategory = subcategory;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
    }

    // Getters & Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) throw new IllegalArgumentException("Ο τίτλος είναι υποχρεωτικός");
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) throw new IllegalArgumentException("Η τιμή πρέπει να είναι θετικός αριθμός");
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Η ποσότητα δεν μπορεί να είναι αρνητική");
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        if (unit == null || (!unit.equals("τεμάχια") && !unit.equals("kg")))
            throw new IllegalArgumentException("Η μονάδα πρέπει να είναι 'τεμάχια' ή 'κιλά'");
        this.unit = unit;
    }

    // Μέθοδος εμφάνισης προϊόντος
    public String displayP() {
        return "Τίτλος: " + title + "\n" +
                "Περιγραφή: " + description + "\n" +
                "Κατηγορία: " + category + "\n" +
                "Υποκατηγορία: " + subcategory + "\n" +
                "Τιμή: " + price + "€\n" +
                "Ποσότητα: " + quantity + (unit.equals("kg") ? unit : " " + unit) + "\n";
    }

    // Μέθοδοι equals και hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return title.equals(product.title) &&
                category.equals(product.category) &&
                subcategory.equals(product.subcategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, category, subcategory);
    }


}
