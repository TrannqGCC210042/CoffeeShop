package Model;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private Order order;
    private Product product;
    private int quantity;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderDetail() {
    }

    public OrderDetail( Product product, Order order,int quantity) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
    }
}
