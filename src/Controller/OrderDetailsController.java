package Controller;

import Model.Order;
import Model.OrderDetail;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsController {
    DefaultTableModel tableOrderDetailModel;
    List<OrderDetail> orderDetailList;

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

    public OrderDetailsController(DefaultTableModel tableOrderDetailModel, List<OrderDetail> orderDetailList) {
        this.tableOrderDetailModel = tableOrderDetailModel;
        this.orderDetailList = orderDetailList;

        if (this.orderDetailList == null || this.orderDetailList.size() == 0) {
            this.orderDetailList = new ArrayList<>();
        }
    }
    public void fillToTable() {
        tableOrderDetailModel.setRowCount(0);
        for (OrderDetail orderDetail: orderDetailList) {
            Object[] rowObj = new Object[] {
                    orderDetail.getOrder().getId(), "<html>" + orderDetail.getProduct().getImage() + "<\\br>" + orderDetail.getProduct().getName() + "</html>", orderDetail.getQuantity(), orderDetail.getProduct().getPrice(), orderDetail.getOrder().getTotal()
            };

            tableOrderDetailModel.addRow(rowObj);
        }
    }
    public void add(OrderDetail orderDetail){
        orderDetailList.add(orderDetail);
    }
}
