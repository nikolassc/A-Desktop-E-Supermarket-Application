package gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class OrderHistoryForm extends JFrame {
    private JTextArea historyTextArea;

    public OrderHistoryForm(String userId) {
        setTitle("Ιστορικό Παραγγελιών");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Δημιουργία TextArea για να εμφανίζονται οι παραγγελίες
        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false); // Δεν επιτρέπεται η επεξεργασία
        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        add(scrollPane, BorderLayout.CENTER);

        // Προβολή του ιστορικού παραγγελιών
        loadOrderHistory(userId);

        setVisible(true);
    }

    // Διαβάζει το ιστορικό από το αρχείο χρήστη
    private void loadOrderHistory(String userId) {
        String fileName = "files/order_history_" + userId + ".txt";
        File file = new File(fileName);
        if (!file.exists()) {
            historyTextArea.setText("Το αρχείο ιστορικού δεν υπάρχει.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder historyContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                historyContent.append(line).append("\n");
            }

            if (historyContent.length() == 0) {
                historyTextArea.setText("Το αρχείο ιστορικού είναι κενό.");
            } else {
                historyTextArea.setText(historyContent.toString());
            }
        } catch (IOException e) {
            historyTextArea.setText("Σφάλμα κατά την ανάγνωση του ιστορικού.");
        }
    }

}
