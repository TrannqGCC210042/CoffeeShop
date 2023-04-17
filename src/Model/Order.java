package Model;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    private static int step = 0;
    private int id;
    private float total;
    private String date;
    private float vat;
    private int waitingCardNumber;
    private boolean status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getVat() {
        return vat;
    }

    public void setVat(float vat) {
        this.vat = vat;
    }

    public int getWaitingCardNumber() {
        return waitingCardNumber;
    }

    public void setWaitingCardNumber(int waitingCardNumber) {
        this.waitingCardNumber = waitingCardNumber;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public Order() {
    }

    public Order(float total, String date, float vat, int waitingCardNumber) {
        this.id = ++step;
        this.total = total;
        this.date = date;
        this.vat = vat;
        this.waitingCardNumber = waitingCardNumber;
        this.status = false;
    }
}
