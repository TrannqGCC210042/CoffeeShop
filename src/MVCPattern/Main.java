package MVCPattern;

import View.AdminGUI;
import View.LoginGUI;
import View.ProductGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
//        JFrame frame = new LoginGUI("Login");
        JFrame frame = new ProductGUI("Login");
        frame.setLocationRelativeTo(null);
    }
}
