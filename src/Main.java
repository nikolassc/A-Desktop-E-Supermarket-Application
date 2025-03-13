import api.*;

import java.util.List;

/**
 * Το πρόγραμμά σας πρέπει να έχει μόνο μία main, η οποία πρέπει να είναι η παρακάτω.
 * <p>
 * <p>
 * ************* ΜΗ ΣΒΗΣΕΤΕ ΑΥΤΗ ΤΗΝ ΚΛΑΣΗ ************
 */


import api.SupermarketAPI;
import gui.LoginPage;
import gui.ProductForm;



import api.IDandPasswords;
import gui.LoginPage;
import gui.RegistrationPage;

import javax.swing.*;
import java.awt.*;



import api.IDandPasswords;

import javax.swing.*;


import gui.LoginPage;
import api.IDandPasswords;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Δημιουργία του αντικειμένου IDandPasswords
        IDandPasswords idandPasswords = new IDandPasswords();

        // Δημιουργία του LoginPage και εγγραφή του στο JFrame
        JFrame frame = new JFrame("Login");
        LoginPage loginPage = new LoginPage(idandPasswords);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(loginPage.getLoginPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);  // Να εμφανίζεται στο κέντρο της οθόνης
        frame.setVisible(true);
    }
}

