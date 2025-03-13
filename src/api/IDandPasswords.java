package api;

import java.io.*;
import java.util.*;

public class IDandPasswords {
    private Map<String, User> users;
    private static final String FILE_PATH = "users.txt";  // Όνομα αρχείου για αποθήκευση χρηστών

    public IDandPasswords() {
        users = new HashMap<>();
        loadUsersFromFile();  // Φορτώνουμε τους χρήστες από το αρχείο κατά την εκκίνηση
        if (users.isEmpty()) {
            // Προσθέτουμε τους admins αν το αρχείο είναι κενό
            addAdminUsers();
            saveUsersToFile();  // Αποθήκευση μετά την προσθήκη
        }
    }

    // Μέθοδος για προσθήκη χρήστη
    public void addUser(String firstName, String lastName, String userID, String password, Role role) {
        User newUser = new User(firstName, lastName, userID, password, role);
        users.put(userID, newUser);
        saveUsersToFile();  // Αποθήκευση χρηστών στο αρχείο μετά την προσθήκη
    }

    // Μέθοδος για επικύρωση του login
    public boolean validateLogin(String userID, String password) {
        User user = users.get(userID);
        return user != null && user.getPassword().equals(password);
    }

    // Μέθοδος για να πάρουμε τον ρόλο του χρήστη
    public Role getUserRole(String userID) {
        User user = users.get(userID);
        return (user != null) ? user.getRole() : null;
    }

    // Μέθοδος για να φορτώσουμε τους χρήστες από το αρχείο
    private void loadUsersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();  // Αφαιρούμε τα περιττά κενά
                if (line.isEmpty()) {
                    continue;  // Παράλειψη κενών γραμμών
                }
                String[] userParts = line.split(",");
                if (userParts.length != 5) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;  // Παράλειψη γραμμών που δεν έχουν τα 5 απαιτούμενα πεδία
                }
                String userID = userParts[0];
                String password = userParts[1];
                String firstName = userParts[2];
                String lastName = userParts[3];
                Role role = Role.valueOf(userParts[4].toUpperCase());

                User user = new User(firstName, lastName, userID, password, role);
                users.put(userID, user);
            }
        } catch (IOException e) {
            System.out.println("No previous user data found or error loading data.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role value in the file.");
        }
    }

    // Μέθοδος για να αποθηκεύσουμε τους χρήστες στο αρχείο
    private void saveUsersToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users.values()) {
                String userLine = user.getUserID() + "," + user.getPassword() + "," +
                        user.getFirstName() + "," + user.getLastName() + "," + user.getRole();
                bw.write(userLine);
                bw.newLine();  // Προσθήκη νέας γραμμής
            }
        } catch (IOException e) {
            System.out.println("Error saving user data.");
        }
    }

    // Μέθοδος για την προσθήκη των admin χρηστών αν το αρχείο είναι κενό
    private void addAdminUsers() {
        addUser("Leandros", "Kelpis", "leke", "2004", Role.ADMIN);
        addUser("Nikolas", "Christoforoy", "niko", "2003", Role.ADMIN);
    }

    // Μέθοδος για τον έλεγχο αν το userID υπάρχει ήδη
    public boolean isUserIDTaken(String userID) {
        return users.containsKey(userID); // Επιστρέφει true αν το userID υπάρχει
    }

    // Εσωτερική κλάση για τον χρήστη
    public static class User {
        private String firstName;
        private String lastName;
        private String userID;
        private String password;
        private Role role;

        public User(String firstName, String lastName, String userID, String password, Role role) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.userID = userID;
            this.password = password;
            this.role = role;
        }

        public String getUserID() {
            return userID;
        }

        public String getPassword() {
            return password;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public Role getRole() {
            return role;
        }
    }
}
