package Controller;

import Model.Order;
import Model.Product;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class OrderController {
    DefaultTableModel tableOrderModel;
    List<Order> orderList;

    public DefaultTableModel getTableOrderModel() {
        return tableOrderModel;
    }

    public void setTableOrderModel(DefaultTableModel tableOrderModel) {
        this.tableOrderModel = tableOrderModel;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public OrderController(DefaultTableModel tableOrderModel, List<Order> orderList) {
        this.tableOrderModel = tableOrderModel;
        this.orderList = orderList;
        if (this.orderList == null || this.orderList.size() == 0) {
            this.orderList = new ArrayList<>();
        }
    }
    public void fillToTable() {
        tableOrderModel.setRowCount(0);
        for (Order order: orderList) {
            String status = null;
            if (order.isStatus()) {
                status = "Paid";
            }else {
                status = "Not Paid";
            }
            Object[] rowObj = new Object[] {
                    order.getId(), order.getDate(), order.getVat(), order.getTotal(), order.getWaitingCardNumber(), status
            };

            tableOrderModel.addRow(rowObj);
        }
    }
    public void add(Order order){
        orderList.add(order);
    }

}
