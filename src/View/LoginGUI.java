package View;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginGUI extends JFrame{

    private JTextField txtUser;
    private JPanel panelLogin;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    public LoginGUI(String title) {
        super(title);
        this.setVisible(true);
        this.setResizable(false);
        this.setContentPane(panelLogin);
//      Exit icon x
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();

        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (txtUser.getText().equals("tran") && txtPassword.getText().equals("123")){
                    JOptionPane.showMessageDialog(panelLogin, "Success", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                }else {
                    JOptionPane.showMessageDialog(panelLogin, "Fails", "ERROR", JOptionPane.ERROR_MESSAGE);
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

    private void exitProgram() {
        int answer = JOptionPane.showConfirmDialog(this, "Do you want exit", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (answer == JOptionPane.YES_OPTION){
            System.exit(1);
        }
    }
}
