package View;

import Controller.OrderDetailsController;
import Lib.ImageRenderer;
import Lib.XFile;
import Model.OrderDetail;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsGUI extends JFrame {
    String pathOrderDetail = "src\\File\\orderdetails.dat";
    private JPanel panelOrderDetail;
    private JTable tbOrderDetail;
    OrderDetailsController orderDetailsController;
    List<OrderDetail> orderDetailList;

    public OrderDetailsGUI(String title) {
        super(title);
        this.setVisible(true);
        this.setResizable(false);
        this.setContentPane(panelOrderDetail);
//        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();

        //        Set field name for table Product
        tbOrderDetail.setModel(new DefaultTableModel(
                new Object[][]{ },
                new String[]{
                        "Order ID","Image" ,"Product", "Quantity", "Price", "Total"
                }
        ));

        tbOrderDetail.setRowHeight(50);
        tbOrderDetail.getColumnModel().getColumn(1).setCellRenderer(new ImageRenderer());

        //        ORDER DETAIL
//        Create class ProductController to use method into it
        orderDetailsController = new OrderDetailsController(
                (DefaultTableModel) tbOrderDetail.getModel(),
                (List<OrderDetail>) XFile.readObject(pathOrderDetail)

        );

        //        Get image column and override cell DefaultTableCellRenderer class component method getTableCellRendererComponent
        tbOrderDetail.setRowHeight(100);
        tbOrderDetail.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());


        String name = this.getTitle();
        int spaceCount = 0;
        int index = -1;

        // Loop through the string to find the index of the third white space
        for (int i = 0; i < name.length(); i++) {
            if (Character.isWhitespace(name.charAt(i))) {
                spaceCount++;
                if (spaceCount == 2) {
                    index = i;
                    break;
                }
            }
        }

        // Get the letter at the index of the third white space
        if (index != -1) {
            char orderID = name.charAt(index + 1);
            if (orderDetailList == null || orderDetailList.size() < 0) { orderDetailList = new ArrayList<>(); }  //Create List

            for (OrderDetail orderDetail : orderDetailsController.getOrderDetailList()) {
                if (orderDetail.getOrder().getId() == Integer.parseInt(String.valueOf(orderID))) {
                    orderDetailList.add(orderDetail);
                }
            }
            orderDetailsController.fillToTable(orderDetailList); //fill to table
        } else {
            System.out.println("Cannot find ID");
        }
    }
//    public void show(List<OrderDetail> odlst){
//        orderDetailsController.fillToTable(odlst);
//    }
}
