package View;

import Lib.XFile;
import Model.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class LoginGUI extends JFrame{

    private JTextField txtUser;
    private JPanel panelLogin;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel errorLogin;
    List<Account> accountList;
    String pathStaff = "src\\File\\staffs.dat";
    public LoginGUI(String title) {
        super(title);
        this.setVisible(true);
        this.setResizable(false);
        this.setContentPane(panelLogin);
//      Exit icon x
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();

        accountList = (List<Account>) XFile.readObject(pathStaff);

        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                errorLogin.setForeground(Color.white);

                if (txtUser.getText().equals("admin") && txtPassword.getText().equals("admin123")){
                    AdminGUI adminPage = new AdminGUI("Admin page");
                    adminPage.setVisible(true);
                    adminPage.setLocationRelativeTo(null);
                    dispose();
                }

                if (userLogin(String.valueOf(txtUser.getText()), String.valueOf(txtPassword.getText()))){
                    ProductGUI staffPage = new ProductGUI("Staff page");
                    staffPage.setVisible(true);
                    staffPage.setLocationRelativeTo(null);
                    dispose();
                }else {
                    errorLogin.setText("Username or Password is invalid.");
                    errorLogin.setForeground(Color.red);
                    txtUser.setText("");
                    txtPassword.setText("");
                }
            }
        });
//        Exit icon x
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitProgram();
            }
        });
    }
    public boolean userLogin(String username, String password){
        boolean temp = false;
        if (accountList != null) {
            for (Account account:accountList) {
                if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                    temp = true;
                    break;
                }
            }
        }
        return temp;
    }
    private void exitProgram() {
        int answer = JOptionPane.showConfirmDialog(this, "Do you want exit", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (answer == JOptionPane.YES_OPTION){
            System.exit(1);
        }
    }
}
