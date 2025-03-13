package gui;

import api.Product;
import api.SupermarketAPI;
import api.ProductHandler;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ProductFormAdmin extends JFrame {
    private JTextField titleText, descText, priceText, quantText;
    private JComboBox<String> unitCombo, catCombo, subcatCombo;
    private Map<String, List<String>> categories;
    private JButton saveButton;
    private SupermarketAPI supermarketAPI;

    public ProductFormAdmin(SupermarketAPI supermarketAPI) {
        this(supermarketAPI, null);
    }

    public ProductFormAdmin(SupermarketAPI supermarketAPI, Product product) {
        this.supermarketAPI = supermarketAPI;
        this.categories = supermarketAPI.getCategories();

        setTitle(product == null ? "Καταχώρηση Προϊόντος" : "Επεξεργασία Προϊόντος");
        setSize(500, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 10, 10));

        add(new JLabel("Τίτλος:"));
        titleText = new JTextField(product == null ? "" : product.getTitle());
        add(titleText);

        add(new JLabel("Περιγραφή:"));
        descText = new JTextField(product == null ? "" : product.getDescription());
        add(descText);

        add(new JLabel("Κατηγορία:"));
        catCombo = new JComboBox<>(categories.keySet().toArray(new String[0]));
        catCombo.setSelectedItem(product == null ? null : product.getCategory());
        add(catCombo);

        add(new JLabel("Υποκατηγορία:"));
        subcatCombo = new JComboBox<>();
        add(subcatCombo);
        updateSubcategory();
        catCombo.addActionListener(e -> updateSubcategory());

        add(new JLabel("Τιμή:"));
        priceText = new JTextField(product == null ? "" : String.valueOf(product.getPrice()));
        add(priceText);

        add(new JLabel("Ποσότητα:"));
        quantText = new JTextField(product == null ? "" : String.valueOf(product.getQuantity()));
        add(quantText);

        add(new JLabel("Μονάδα:"));
        unitCombo = new JComboBox<>(new String[]{"τεμάχια", "kg"});
        unitCombo.setSelectedItem(product == null ? null : product.getUnit());
        add(unitCombo);

        saveButton = new JButton("Καταχώρηση");
        add(saveButton);

        saveButton.addActionListener(e -> {
            try {
                String title = titleText.getText().trim();
                String description = descText.getText().trim();
                String category = (String) catCombo.getSelectedItem();
                String subcategory = (String) subcatCombo.getSelectedItem();
                String unit = (String) unitCombo.getSelectedItem();
                double price = Double.parseDouble(priceText.getText().trim());
                int quantity = Integer.parseInt(quantText.getText().trim());

                if (title.isEmpty() || category == null || subcategory == null) {
                    throw new IllegalArgumentException("Ο τίτλος, η κατηγορία και η υποκατηγορία είναι υποχρεωτικά.");
                }

                // Δημιουργία νέου προϊόντος
                Product updatedProduct = new Product(title, description, category, subcategory, price, quantity, unit);

                // Ενημέρωση του προϊόντος στη λίστα προϊόντων
                List<Product> products = supermarketAPI.getProducts();
                boolean productFound = false;

                for (int i = 0; i < products.size(); i++) {
                    if (products.get(i).getTitle().equalsIgnoreCase(title)) {
                        products.set(i, updatedProduct); // Αντικατάσταση του παλιού προϊόντος με το νέο
                        productFound = true;
                        break;
                    }
                }

                if (!productFound) {
                    JOptionPane.showMessageDialog(this, "Το προϊόν δεν βρέθηκε για ενημέρωση. Προστίθεται ως νέο προϊόν.");
                    products.add(updatedProduct); // Αν δεν βρεθεί, προστίθεται ως νέο προϊόν
                }

                // Αποθήκευση της ενημερωμένης λίστας στο αρχείο
                ProductHandler.saveProducts(products);

                JOptionPane.showMessageDialog(this, "Το προϊόν ενημερώθηκε επιτυχώς!");
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Παρακαλώ εισάγετε έγκυρους αριθμούς για την τιμή και την ποσότητα.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Σφάλμα κατά την ενημέρωση του προϊόντος: " + ex.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            }
        });


        setVisible(true);
    }

    private void updateSubcategory() {
        // Παίρνουμε την επιλεγμένη κατηγορία από το catCombo
        String selCategory = (String) catCombo.getSelectedItem();

        // Debugging: Εκτυπώνουμε την επιλεγμένη κατηγορία και το Map
        System.out.println("Επιλεγμένη κατηγορία: " + selCategory);
        System.out.println("Κατηγορίες: " + categories);

        // Αδειάζουμε το subcatCombo για να φορτώσουμε νέες υποκατηγορίες
        subcatCombo.removeAllItems();

        // Ελέγχουμε αν η κατηγορία είναι επιλεγμένη και αν υπάρχουν υποκατηγορίες για αυτή
        if (selCategory != null && categories.containsKey(selCategory)) {
            List<String> subcategories = categories.get(selCategory);

            // Προσθέτουμε τις υποκατηγορίες αν υπάρχουν
            if (subcategories != null && !subcategories.isEmpty()) {
                for (String subcategory : subcategories) {
                    subcatCombo.addItem(subcategory);
                }
            } else {
                // Αν δεν υπάρχουν υποκατηγορίες, εμφανίζουμε μήνυμα
                subcatCombo.addItem("Δεν υπάρχουν υποκατηγορίες");
            }
        } else {
            // Αν η κατηγορία δεν υπάρχει στο Map, εμφανίζουμε μήνυμα
            subcatCombo.addItem("Δεν υπάρχουν υποκατηγορίες");
        }
    }


}
