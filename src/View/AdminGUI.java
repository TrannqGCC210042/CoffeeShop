package View;

import Controller.StaffController;
import Lib.XFile;
import Model.Account;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AdminGUI extends JFrame{
    private JPanel panelAdmin;
    private JTable tbStaff;
    private JTextField txtFullname;
    private JRadioButton rdMale;
    private JRadioButton rdFemale;
    private JTextField txtUsername;
    private JTextField txtPassword;
    private JButton btnNewStaff;
    private JButton btnDeleteAllStaff;
    private JButton btnAddStaff;
    private JButton btnDeleteStaff;
    private JTextField txtPhone;
    private JTextField txtAddressStaff;
    private JLabel errorStaffPassword;
    private JLabel errorStaffIUsername;
    private JLabel errorStaffName;
    private JLabel errorStaffPhone;
    private JLabel errorStaffAddress;
    private JButton btnEditStaff;
    private JTextField txtSearchStaff;
    private JButton btnSearchStaff;
    private JLabel lbHeader;
    private JButton btnLogout;
    String filePath = "src\\File\\staffs.dat";
    int click = -1;
    int row = -1;
    StaffController staffController;
    public AdminGUI(String title){
        super(title);
        this.setVisible(true);
        this.setContentPane(panelAdmin);
        this.pack();
        ImageIcon headerImg = new ImageIcon(new ImageIcon("src\\Images\\icon\\logo.png").getImage().getScaledInstance(80,60,Image.SCALE_DEFAULT));
        lbHeader.setIcon(headerImg);
        tbStaff.setRowHeight(30);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitProgram();
            }
        });

//        Set field name for table Product
        tbStaff.setModel(new DefaultTableModel(
                new Object[][]{ },
                new String[]{
                        "Username", "Password", "Full Name", "Gender", "Phone", "Address"
                }
        ){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

//      Use DefaultTableModel for processing table, and processing file
        staffController = new StaffController(
                (DefaultTableModel) tbStaff.getModel(),
                (List<Account>) XFile.readObject(filePath)
        );

        staffController.fillToTable();

        btnNewStaff.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearInput();
            }
        });

        btnAddStaff.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                add();
            }
        });
        btnEditStaff.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                edit();
            }
        });
        btnDeleteStaff.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                deleteOne();
            }
        });
        btnDeleteAllStaff.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                deleteAll();
            }
        });
        tbStaff.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickRowInTable();
            }
        });

        btnSearchStaff.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!txtSearchStaff.getText().equals("") && click == 1 && !txtSearchStaff.getText().equals(" ")) {
                    search();
                }else {
                    txtSearchStaff.setText("Search by name");
                    JOptionPane.showMessageDialog(null, "Please enter a name to search.");
                }
            }
        });
        txtSearchStaff.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtSearchStaff.setText("");
                click = 1;
            }
        });
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                exitProgram();
            }
        });
    }

    private void exitProgram() {
        int answer = JOptionPane.showConfirmDialog(this, "Do you want to Logout", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (answer == JOptionPane.YES_OPTION){
            JFrame login = new LoginGUI("Login");
            login.setVisible(true);
            login.setLocationRelativeTo(null);
            dispose();
        }
    }

    private void search() {
        List<Account> accountList = new ArrayList<>();

        for (Account account: staffController.getAccountList()) {
            if (account.getFullName().toUpperCase().contains(txtSearchStaff.getText().toUpperCase())) {
                accountList.add(account);
            }
        }

        if (accountList.size() > 0) {
            staffController.fillToTable(accountList);
        }else {
            JOptionPane.showMessageDialog(null, "Cannot find name \"" + txtSearchStaff.getText() + "\"", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Not found");
        }
    }

    //    Edit Product
    private void edit() {
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please choose the staff!");
            clearError();
        }else {
            if (isValidInput("edit")) {
                boolean gender = false;

                if (rdFemale.isSelected()) {
                    gender = true;
                }

                Account account = new Account(
                        txtFullname.getText(),
                        gender,
                        txtUsername.getText(),
                        txtPassword.getText(),
                        txtPhone.getText(),
                        txtAddressStaff.getText()
                );

//              Edit List
                staffController.updateLst(account);
                staffController.fillToTable();
                XFile.writeObject(filePath, staffController.getAccountList());

                clearInput();
            }
        }
    }
    //    Show a row into Input
    private void clickRowInTable() {
        btnAddStaff.setEnabled(false);
        btnEditStaff.setEnabled(true);

        row = tbStaff.getSelectedRow();
        showIntoInput(row);
    }

    //    Function: Show a row into Input
    private void showIntoInput(int row) {
        clearError();

        String username = (String) tbStaff.getValueAt(row, 0);
        txtUsername.setText(username);
        txtUsername.setEnabled(false);
        txtUsername.setToolTipText("You cannot update ID");

        String password = (String) tbStaff.getValueAt(row, 1);
        txtPassword.setText(password);

        String name = (String) tbStaff.getValueAt(row, 2);
        txtFullname.setText(name);

        String gender = (String) tbStaff.getValueAt(row, 3);

        if (gender.equals("Male")) {
            rdMale.setSelected(true);
        } else {
            rdFemale.setSelected(true);
        }

        String phone = (String) tbStaff.getValueAt(row, 4);
        txtPhone.setText(phone);

        String address = (String) tbStaff.getValueAt(row, 5);
        txtAddressStaff.setText(address);
    }
    //    Function: delete staff
    private void deleteOne() {
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please choose the staff!");
        }else {
            int answer = JOptionPane.showConfirmDialog(this, "Do you want to remove it", "Remove",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (answer == JOptionPane.YES_OPTION) {
                staffController.deleteOne(txtUsername.getText());
                staffController.fillToTable();
                XFile.writeObject(filePath, staffController.getAccountList());

                clearInput();
            }
        }
    }

    //    Function: delete all staff
    private void deleteAll() {
        int answer = JOptionPane.showConfirmDialog(this, "Do you want to remove all", "Remove",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (answer == JOptionPane.YES_OPTION) {
            staffController.deleteAll();
            staffController.fillToTable();
            XFile.writeObject(filePath, staffController.getAccountList());

            clearInput();
        }
    }

    private void add() {
        if (isValidInput("add")) {
            boolean gender = false;

            if (rdFemale.isSelected()) {
                gender = true;
            }

            Account account = new Account(
                    txtFullname.getText(),
                    gender,
                    txtUsername.getText(),
                    txtPassword.getText(),
                    txtPhone.getText(),
                    txtAddressStaff.getText()
            );

//        add new Product from Form
            staffController.add(account);
//        fill to Table
            staffController.fillToTable();
//        Save to file
            XFile.writeObject(filePath, staffController.getAccountList());
//        Clear form
            clearInput();
        }
    }

    private boolean isValidInput(String action) {
        boolean temp = true;
        String regLetterAndNumber = "\\w+";
        String regLetter = "^[a-zA-Z,. ]*$";

        if (action == "add") {
            if (!isDuplicateID()) {
                temp = false;
            }
            //        Phone
            if (!isValidPhone(action)) {
                temp = false;
            }
        }

        if (txtUsername.getText().equals("")) {
            errorStaffIUsername.setForeground(Color.red);
            errorStaffIUsername.setText("This field cannot be empty.");

            temp = false;
        }else if (!txtUsername.getText().matches(regLetterAndNumber)) {
            errorStaffIUsername.setForeground(Color.red);
            errorStaffIUsername.setText("This field cannot contain the characters.");

            temp = false;
        }else if (txtUsername.getText().length() < 4) {
            errorStaffIUsername.setForeground(Color.red);
            errorStaffIUsername.setText("This field must contain more than 3 letters.");

            temp = false;
        }

//      Password
        if (txtPassword.getText().equals("")) {
            errorStaffPassword.setForeground(Color.red);
            errorStaffPassword.setText("This field cannot be empty.");

            temp = false;
        }else if (txtPassword.getText().length() < 6) {
            errorStaffPassword.setForeground(Color.red);
            errorStaffPassword.setText("This field must contain more than 5 characters.");

            temp = false;
        }

//        Full name
        if (txtFullname.getText().equals("")) {
            errorStaffName.setForeground(Color.red);
            errorStaffName.setText("This field cannot be empty.");

            temp = false;
        }else if (txtFullname.getText().matches(regLetter)) {
            errorStaffName.setForeground(Color.red);
            errorStaffName.setText("This field cannot contain the characters.");

            temp = false;
        }else if (txtFullname.getText().length() < 4) {
            errorStaffName.setForeground(Color.red);
            errorStaffName.setText("This field must contain more than 3 characters.");

            temp = false;
        }

        if (txtPhone.getText().equals("")) {
            errorStaffPhone.setForeground(Color.red);
            errorStaffPhone.setText("This field cannot be empty.");

            temp = false;
        }

        if (txtPhone.getText().length() != 10) {
            errorStaffPhone.setForeground(Color.red);
            errorStaffPhone.setText("Phone number must be 10 digits.");

            temp = false;
        }
//        Phone
        if (action == "edit") {
            if (!isValidPhone("edit")) {
                temp = false;
            }
        }

//      address
        if (txtAddressStaff.getText().equals("")) {
            errorStaffAddress.setForeground(Color.red);
            errorStaffAddress.setText("This field cannot be empty.");

            temp = false;
        }else if (!txtAddressStaff.getText().matches(regLetter)) {
            errorStaffAddress.setForeground(Color.red);
            errorStaffAddress.setText("This field cannot contain the characters.");

            temp = false;
        }else if (txtAddressStaff.getText().length() < 3) {
            errorStaffAddress.setForeground(Color.red);
            errorStaffAddress.setText("This field must be more than 2 characters.");

            temp = false;
        }

        return temp;
    }
    public boolean isDuplicateID() {
        String temp = null;

        for (Account account: staffController.getAccountList()) {
            if (account.getUsername().equals(txtUsername.getText())) {
                errorStaffIUsername.setForeground(Color.red);
                errorStaffIUsername.setText("Username already exists.");

                temp = account.getUsername();
                break;
            }
        }

        if (temp == null) {
            return true;
        }else {
            return false;
        }
    }
    public boolean isValidPhone(String action){
        String temp = null;

        if (action.equals("add")) {
            for (Account account: staffController.getAccountList()) {
                if (account.getPhone().equals(txtPhone.getText())) {
                    errorStaffPhone.setForeground(Color.red);
                    errorStaffPhone.setText("Phone number already exists.");

                    temp = account.getPhone();
                    break;
                }
            }
        }else {
//            Update Staff
            row = tbStaff.getSelectedRow();
            for (Account account: staffController.getAccountList()) {
                if (!account.getPhone().equals(tbStaff.getValueAt(row,4)) && account.getPhone().equals(txtPhone.getText())) {
                    errorStaffPhone.setForeground(Color.red);
                    errorStaffPhone.setText("Phone number already exists.");

                    temp = account.getPhone();
                    break;
                }
            }
        }

        if (temp == null) {
            return true;
        }else {
            return false;
        }
//        Pattern pattern = Pattern.compile("^(\\(0\\d{1,3}\\)\\d{7})|(0\\d{9,10})$");
//        Matcher matcher = pattern.matcher(phone);
//        return !matcher.find();
    }
    //   Function: Clear Staff
    private void clearInput() {
        row = -1;
        btnAddStaff.setEnabled(true);
        txtUsername.setEnabled(true);
        txtUsername.setToolTipText("");

        txtUsername.setText("");
        txtFullname.setText("");
        rdMale.setSelected(true);
        txtPassword.setText("");
        txtPhone.setText("");
        txtAddressStaff.setText("");
        clearError();

        txtSearchStaff.setText("");
        staffController.fillToTable();
    }

    private void clearError() {
        errorStaffIUsername.setForeground(Color.white);
        errorStaffPassword.setForeground(Color.white);
        errorStaffName.setForeground(Color.white);
        errorStaffPhone.setForeground(Color.white);
        errorStaffAddress.setForeground(Color.white);
    }

}

