package View;

import Controller.OrderDetailsController;
import Lib.ImageRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OrderDetailsGUI extends JFrame {

    private JPanel panelOrderDetail;
    private JTable tbOrderDetail;
    OrderDetailsController orderDetailsController;

    public OrderDetailsGUI(String title) {
        super(title);
        this.setVisible(true);
        this.setResizable(false);
        this.setContentPane(panelOrderDetail);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();

        //        Set field name for table Product
        tbOrderDetail.setModel(new DefaultTableModel(
                new Object[][]{ },
                new String[]{
                        "Order ID", "Product", "Quantity", "Price", "Total"
                }
        ));

        //        Get image column and override cell DefaultTableCellRenderer class component method getTableCellRendererComponent
        tbOrderDetail.setRowHeight(100);
        tbOrderDetail.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());
        orderDetailsController.fillToTable();  // Fill to Table

    }
}
