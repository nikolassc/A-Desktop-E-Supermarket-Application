package api;

import java.io.IOException;
import java.util.*;

public class SupermarketAPI {
    private List<Product> products; // Λίστα με όλα τα προϊόντα
    private Map<String, List<String>> categories; // Κατηγορίες και υποκατηγορίες

    // Constructor: Φορτώνει δεδομένα από τα αρχεία
    public SupermarketAPI() {
        try {
            this.products = ProductHandler.loadProducts(); // Φόρτωση προϊόντων
            this.categories = ProductHandler.loadCategories(); // Φόρτωση κατηγοριών
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά τη φόρτωση δεδομένων: " + e.getMessage());
        }
    }

    // Εμφανίζει όλα τα προϊόντα
    public void displayProducts() {
        for (Product product : products) {
            System.out.println(product.displayP());
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    // Επιστρέφει προϊόντα βάσει κατηγορίας
    public List<Product> getProductsByCategory(String category) {
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    // Προσθέτει νέο προϊόν
    public void addProduct(Product product) {
        products.add(product); // Προσθήκη στη λίστα
        try {
            ProductHandler.saveProducts(products); // Αποθήκευση στο αρχείο
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την αποθήκευση: " + e.getMessage());
        }
    }

    // Ενημερώνει την ποσότητα ενός προϊόντος βάσει τίτλου
    public void updateProductQuantity(String title, int newQuantity) {
        for (Product product : products) {
            if (product.getTitle().equalsIgnoreCase(title)) {
                product.setQuantity(newQuantity); // Ενημέρωση ποσότητας
                try {
                    ProductHandler.saveProducts(products); // Αποθήκευση αλλαγών
                } catch (IOException e) {
                    System.err.println("Σφάλμα κατά την αποθήκευση: " + e.getMessage());
                }
                return;
            }
        }
        System.out.println("Το προϊόν δεν βρέθηκε: " + title);
    }

    public Product getProductByTitle(String title){
        for(Product pr : products){
            if(pr.getTitle().equalsIgnoreCase(title)){
                return pr;
            }
        }
        return null;
    }

    // Επιστρέφει τις κατηγορίες
    public Map<String, List<String>> getCategories() {
        return categories;
    }

    // Εμφάνιση κατηγοριών
    public void displayCategories() {
        for (Map.Entry<String, List<String>> entry : categories.entrySet()) {
            System.out.println("Κατηγορία: " + entry.getKey());
            System.out.println("Υποκατηγορίες: " + String.join(", ", entry.getValue()));
        }
    }
}
