package Model;

import Controller.OrderController;
import Lib.XFile;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    String pathOrder = "src\\File\\orders.dat";

    private int id;
    private float total;
    private Date date;
    private float vat;
    private int waitingCardNumber;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public Order() {
    }

    public Order(int id, float total, Date date, float vat, int waitingCardNumber) {
//        for (Order order: (List<Order>) XFile.readObject(pathOrder)) {
//
//        }
        this.id = id;
        this.total = total;
        this.date = date;
        this.vat = vat;
        this.waitingCardNumber = waitingCardNumber;
    }
}
