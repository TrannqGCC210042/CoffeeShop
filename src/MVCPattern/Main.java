package MVCPattern;

import View.AdminGUI;
import View.LoginGUI;
import View.ProductGUI;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new ProductGUI("Login");
        Image icon = Toolkit.getDefaultToolkit().getImage("src\\Images\\icon\\titleLogo.png");
        frame.setIconImage(icon);
        frame.setLocationRelativeTo(null);
    }
}
