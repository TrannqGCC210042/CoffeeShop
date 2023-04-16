package MVCPattern;

import View.AdminGUI;
import View.ProductGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
//        JFrame frame = new LoginGUI("Login");
//        JFrame frame = new AdminGUI("Management Staff");
        JFrame frame = new ProductGUI("Management Product");

        frame.setLocationRelativeTo(null);
    }


}
