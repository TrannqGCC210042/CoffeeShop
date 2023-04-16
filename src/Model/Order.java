package Model;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    private int id;
    private double total;
    private Date date;
    private int vat;
    private Account account;
    private boolean status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public Order() {
    }

    public Order(int id, double total, Date date, int vat, Account account, boolean status) {
        this.id = id;
        this.total = total;
        this.date = date;
        this.vat = vat;
        this.account = account;
        this.status = status;
    }
}
