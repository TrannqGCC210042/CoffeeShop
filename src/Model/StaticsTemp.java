package Model;

public class StaticsTemp {
    private String proID;
    private int quantity;

    public String getProID() {
        return proID;
    }

    public void setProID(String proID) {
        this.proID = proID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public StaticsTemp() {
    }

    public StaticsTemp(String proID, int quantity) {
        this.proID = proID;
        this.quantity = quantity;
    }
}
