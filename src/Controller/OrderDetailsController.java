package Controller;

import Lib.XUtils;
import Model.Order;
import Model.OrderDetail;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsController {
    DefaultTableModel tableOrderDetailModel;
    DefaultTableModel tableStaticsModel;

    List<OrderDetail> orderDetailList;

    public DefaultTableModel getTableStaticsModel() {
        return tableStaticsModel;
    }

    public void setTableStaticsModel(DefaultTableModel tableStaticsModel) {
        this.tableStaticsModel = tableStaticsModel;
    }

    public DefaultTableModel getTableOrderDetailModel() {
        return tableOrderDetailModel;
    }

    public void setTableOrderDetailModel(DefaultTableModel tableOrderDetailModel) {
        this.tableOrderDetailModel = tableOrderDetailModel;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public OrderDetailsController() {
    }
    public OrderDetailsController(List<OrderDetail> readObject) {
        this.orderDetailList = readObject;
        if (this.orderDetailList == null || this.orderDetailList.size() == 0) {
            this.orderDetailList = new ArrayList<>();
        }
    }
    public OrderDetailsController(DefaultTableModel tableOrderDetailModel, List<OrderDetail> orderDetailList) {
        this.tableOrderDetailModel = tableOrderDetailModel;
        this.orderDetailList = orderDetailList;

        if (this.orderDetailList == null || this.orderDetailList.size() == 0) {
            this.orderDetailList = new ArrayList<>();
        }
    }
    public OrderDetailsController(DefaultTableModel tableStaticsModel) {
        this.tableStaticsModel = tableStaticsModel;
    }

    public void fillToTable(List<OrderDetail> orderDetailList) {
        for (OrderDetail orderDetail: orderDetailList) {
            Object[] rowObj = new Object[] {
                    orderDetail.getOrder().getId(),orderDetail.getProduct().getImage(), orderDetail.getProduct().getName(), orderDetail.getQuantity(), orderDetail.getProduct().getPrice(), orderDetail.getProduct().getPrice() * orderDetail.getQuantity()
            };
            tableOrderDetailModel.addRow(rowObj);
        }
    }
    public void fillToStatics(List<Order> orderList) {
        tableOrderDetailModel.setRowCount(0);

        if (orderList.size() >= 0 || orderList.size() >= 0) {
            for (Order order: orderList) {
                Object[] rowObj = new Object[] {
                        order.getId(), XUtils.convertDatetoString(order.getDate()), order.getVat(), order.getTotal(), order.getWaitingCardNumber()
                };
                tableOrderDetailModel.addRow(rowObj);
            }
        }
    }
    public void add(OrderDetail orderDetail){
        orderDetailList.add(orderDetail);
    }
    public void show(){

    }
}
