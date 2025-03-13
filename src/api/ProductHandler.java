package api;

import java.io.*;
import java.util.*;

public class ProductHandler {
    private static final String Dir = "files/";
    private static final String products_file = Dir + "products.txt";
    private static final String category_file = Dir + "categories_subcategories.txt";

    // Φορτώνει τα προϊόντα από το αρχείο
    public static List<Product> loadProducts() throws IOException {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(products_file))) {
            String line;
            String title = null, description = null, category = null, subcategory = null, unit = null;
            double price = 0;
            int quantity = 0;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("Τίτλος:")) {
                    title = line.substring(8).trim();
                }else if(line.startsWith("Περιγραφή:")) {
                    description = line.substring(11).trim();
                }else if(line.startsWith("Κατηγορία:")) {
                    category = line.substring(10).trim();
                }else if(line.startsWith("Υποκατηγορία:")) {
                    subcategory = line.substring(13).trim();
                }else if(line.startsWith("Τιμή:")) {
                    price = Double.parseDouble(line.substring(5).replace("€", "").replace(",", ".").trim());
                }else if(line.startsWith("Ποσότητα:")){
                    String quantUnit = line.substring(9).trim();
                    if(quantUnit.endsWith("kg")){
                        unit = "kg";
                        quantity = Integer.parseInt(quantUnit.replace("kg","").trim());
                    }else if(quantUnit.endsWith("τεμάχια")){
                        unit = "τεμάχια";
                        quantity = Integer.parseInt(quantUnit.replace("τεμάχια", "").trim());
                    }else{
                        throw  new IllegalArgumentException("Άγνωστη μονάδα μέτρησης στη γραμμή: " + line);
                    }
                    products.add(new Product(title, description, category, subcategory, price, quantity, unit));
                }
            }
        }
        return products;
    }

    // Φορτώνει τις κατηγορίες και υποκατηγορίες
    public static Map<String, List<String>> loadCategories() throws IOException {
        Map<String, List<String>> categories = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("files/categories_subcategories.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" \\(");
                String category = parts[0].trim();
                String[] subcategories = parts[1].replace(")", "").split("@");
                categories.put(category, Arrays.asList(subcategories));
            }
        }
        return categories;
    }


    // Αποθήκευση προϊόντων πίσω στο αρχείο
    public static void saveProducts(List<Product> products) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(products_file))) {
            for (Product product : products) {
                bw.write("Τίτλος: " + product.getTitle() + "\n");
                bw.write("Περιγραφή: " + product.getDescription() + "\n");
                bw.write("Κατηγορία: " + product.getCategory() + "\n");
                bw.write("Υποκατηγορία: " + product.getSubcategory() + "\n");
                bw.write("Τιμή: " + String.format("%.2f€", product.getPrice()) + "\n");
                bw.write("Ποσότητα: " + product.getQuantity() + " " + product.getUnit() + "\n\n");
            }
        }
    }
}
