package Controller;

import Lib.XUtils;
import Model.Order;
import Model.OrderDetail;
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

    public OrderController() {
    }

    public OrderController(DefaultTableModel tableOrderModel) {
        this.tableOrderModel = tableOrderModel;
    }

    public OrderController(DefaultTableModel tableOrderModel, List<Order> orderList) {
        this.tableOrderModel = tableOrderModel;
        this.orderList = orderList;

        if (this.orderList == null || this.orderList.size() == 0) {
            this.orderList = new ArrayList<>();
        }
    }

//    Function to calculator total and show in tbStatics
    public float fillToTable(List<Order> orderList) {
        tableOrderModel.setRowCount(0);
        float total = 0F;
        if (orderList.size() > 0) {
            for (Order od: orderList) {
                Object[] rowObj = new Object[] {
                        od.getId(), XUtils.convertDatetoString(od.getDate()), od.getVat(),
                        od.getTotal(), od.getWaitingCardNumber()
                };
                tableOrderModel.addRow(rowObj);
                total += od.getTotal();
            }
        }
        return total;
    }
    public void add(Order order){
        orderList.add(order);
    }

}
