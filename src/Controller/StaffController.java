package Controller;

import Model.Account;
import Model.Product;
import View.AdminGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class StaffController {
    DefaultTableModel tableStaffModel;
    List<Account> accountList;

    public DefaultTableModel getTableStaffModel() {
        return tableStaffModel;
    }

    public void setTableStaffModel(DefaultTableModel tableStaffModel) {
        this.tableStaffModel = tableStaffModel;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public StaffController(DefaultTableModel tableStaffModel, List<Account> accountList) {
        this.tableStaffModel = tableStaffModel;
        this.accountList = accountList;

        if (this.accountList == null || this.accountList.size() == 0) {
            this.accountList = new ArrayList<>();
        }
    }
    public void fillToTable() {
        tableStaffModel.setRowCount(0);
        for (Account account: accountList) {
            String gender = "";

            if (account.isGender()) {
                gender = "Female";
            }
            else {
                gender = "Male";
            }

            Object[] rowObj = new Object[] {
                    account.getUsername(), account.getPassword(), account.getFullName(), gender, account.getPhone(), account.getAddress()
            };
            
            tableStaffModel.addRow(rowObj);
        }
    }

    public void fillToTable(List<Account> accountList) {
        tableStaffModel.setRowCount(0);
        for (Account account: accountList) {
            String gender = "";

            if (account.isGender()) {
                gender = "Female";
            }
            else {
                gender = "Male";
            }

            Object[] rowObj = new Object[] {
                    account.getUsername(), account.getPassword(), account.getFullName(), gender, account.getPhone(), account.getAddress()
            };

            tableStaffModel.addRow(rowObj);
        }
    }
    public void add(Account account) {
        accountList.add(account);
    }

    public void updateLst(Account account) {
        for (Account a: accountList) {
            if (a.getUsername().equals(account.getUsername())) {
                a.setFullName(account.getFullName());
                a.setGender(account.isGender());
                a.setPassword(account.getPassword());
                a.setPhone(account.getPhone());
                a.setAddress(account.getAddress());

                break;
            }
        }
    }

    public void deleteOne(String username) {
        for (Account a: accountList) {
            if (a.getUsername().equals(username)) {
                accountList.remove(a);

                break;
            }
        }
    }

    public void deleteAll() {
        accountList.removeAll(accountList);
    }

}
