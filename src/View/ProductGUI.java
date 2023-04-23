package View;

import Controller.OrderController;
import Controller.OrderDetailsController;
import Controller.ProductController;
import Lib.*;
import Model.Order;
import Model.OrderDetail;
import Model.Product;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;


public class ProductGUI extends JFrame {
    private JPanel panelStaff;
    private JTextField txtSearch;
    private JTabbedPane tblManagement;
    private JPanel panelProductOrder;
    private JButton btnNewProduct;
    private JButton btnEditProduct;
    private JButton btnDeleteProduct;
    private JTextField txtProductName;
    private JTextField txtProductIngredient;
    private JTextField txtProductPrice;
    private JSpinner btnProductQuantity;
    private JPanel panelProduct;
    private JTable tbProduct;
    private JTable tbOrder;
    private JButton btnAddProduct;
    private JButton btnDeleteAllProduct;
    private JTextField txtDay;
    private JTextField txtProductID;
    private JButton btnSaveOrder;
    private JButton btnCancelOrder;
    private JLabel errorProductID;
    private JLabel errorProductName;
    private JLabel errorProductIngredient;
    private JLabel errorProductPrice;
    private JLabel errorProductQuantity;
    private JButton btnSearchProduct;
    private JScrollPane productScrollPanel;
    private JTextArea txtVAT;
    private JTextArea txtSubtotal;
    private JTextArea txtTotal;
    private JPanel panelOrder;
    private JButton btnUploadImage;
    private JLabel lbProductImage;
    private JTextField txtProductImage;
    private JRadioButton rdSale;
    private JRadioButton rdSoldOut;
    private JLabel lbStatus;
    private JTable tbStatistic;
    private JTextField txtSearchProOrder;
    private JButton btnSearchProOrder;
    private JPanel panelStatistical;
    private JTextArea txtTotalByMonth;
    private JComboBox cbMonth;
    private JScrollPane scrollPaneBestSelling;
    private JTextField txtSearchProduct;
    private JButton btnLogout;
    private JLabel lbProductID;
    private JLabel lbHeader;
    private JButton newButton;
    private JButton editButton;
    private JButton deleteButton;
    String filePath = "src\\File\\products.dat";
    String pathOrder = "src\\File\\orders.dat";
    String pathOrderDetail = "src\\File\\orderdetails.dat";
    DefaultTableModel tableOrderModel;
    int row = -1;
    int add = -1;
    ProductController productController;
    OrderController orderController;
    OrderDetailsController orderDetailsController;
    OrderController staticsController;
    String originalPath = null;
    List<OrderDetail> tempLst;
    JPanel productPanel;
    JPanel layoutProductPanel;
    int clickProOrder = -1;
    int clickPro = -1;
    List<Order> getTempOrderLst;
    List<Product> getBestSelling;
    List<Map.Entry<String, Integer>> top5BestSelling;
    JPanel panelBestSelling;
    int month_number = 0;

    public ProductGUI(String title) throws HeadlessException {
        super(title);
        this.setVisible(true);
        this.setContentPane(panelStaff);
        ImageIcon headerImg = new ImageIcon(new ImageIcon("src\\Images\\icon\\logo.png").getImage().getScaledInstance(80,60,Image.SCALE_DEFAULT));
        lbHeader.setIcon(headerImg);
        tbStatistic.setRowHeight(30);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();
        int delay = 100;

        lbStatus.setVisible(false);
        rdSale.setVisible(false);
        rdSoldOut.setVisible(false);
        tbStatistic.setRowHeight(25);

//        exit Program
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitProgram();
            }
        });
        //        exit Program

        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String formattedDateTime = now.format(formatter);
                txtDay.setText(formattedDateTime);
            }
        });

        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                exitProgram();
            }
        });

        timer.start();

//        Set field name for table Order
        tableOrderModel = new DefaultTableModel(
                new Object[][]{ },
                new String[]{
                        "ID", "Image", "Product Name", "Quantity", "Price", "Remove"
                }
        ){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbOrder.setModel(tableOrderModel);
        tbOrder.getColumnModel().getColumn(5).setCellRenderer(new ButtonRemove());

//        Get image column and override cell DefaultTableCellRenderer class component method getTableCellRendererComponent
        tbOrder.setRowHeight(50);
        tbOrder.setBackground(Color.white);
        tbOrder.getColumnModel().getColumn(1).setCellRenderer(new ImageRenderer());

//      Remove product in Order page
        tbOrder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                get click index Column in table
                if (tbOrder.getRowCount() > 0) {
                    int column = tbOrder.columnAtPoint(e.getPoint());
                    if (column == 5) {
                        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure to remove?", "Warming", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            tableOrderModel.removeRow(tbOrder.getSelectedRow());
                            add = -1;
                            prepareInvoice();
                        }
                    }
                }
            }
        });
//        Limit value for Product Quantity
        SpinnerModel sm = new SpinnerNumberModel(0, 0, 1000, 1); //default value,lower bound,upper bound,increment by
        btnProductQuantity.setModel(sm);

//        Set field name for table Product
        tbProduct.setModel(new DefaultTableModel(
            new Object[][]{ },
            new String[]{
                    "ID", "Name", "Ingredient", "Price", "Quantity", "Status", "Image"
                }
        ){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

//        Get image column and override cell DefaultTableCellRenderer class component method getTableCellRendererComponent
        tbProduct.setRowHeight(100);
        tbProduct.getColumnModel().getColumn(6).setCellRenderer(new ImageRenderer());

//        PRODUCT
//        Create class ProductController to use method into it
        productController = new ProductController(
                (DefaultTableModel) tbProduct.getModel(),
                (List<Product>) XFile.readObject(filePath)
        );
        productController.fillToTable();

//        ORDER DETAIL
//        Create class ProductController to use method into it
        orderDetailsController = new OrderDetailsController(
                (List<OrderDetail>) XFile.readObject(pathOrderDetail)
        );

//        ORDER
//        Set field name for table Order
        tbStatistic.setModel(new DefaultTableModel(
                new Object[][]{ },
                new String[]{
                        "Order ID", "Date", "VAT", "Total", "Waiting Card Number", "Order Detail"
                }
        ){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tbStatistic.getColumnModel().getColumn(5).setCellRenderer(new ButtonEditor());
//        Create class ProductController to use method into it
        staticsController = new OrderController(  //fill by month
                (DefaultTableModel) tbStatistic.getModel()
        );

        orderController = new OrderController(  // add Order
                (DefaultTableModel) tbStatistic.getModel(),
                (List<Order>) XFile.readObject(pathOrder)
        );

        Date formatDate = XUtils.convertStringtoDate(txtDay.getText());
        cbMonth.setSelectedItem(XUtils.convertDatetoMonthName(formatDate));
        fillStatics();

//        get ID for see more button
        tbStatistic.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                get click index Column in table
                if (tbStatistic.getRowCount() > 0) {
                    int column = tbStatistic.columnAtPoint(e.getPoint());
                    if (column == 5) {
                        int row = tbStatistic.getSelectedRow();
                        int idOrder = (int) tbStatistic.getValueAt(row, 0) ;  //id order
                        if (tempLst == null || tempLst.size() < 0) { tempLst = new ArrayList<>(); }

                        for (OrderDetail orderDetail:orderDetailsController.getOrderDetailList()) {
                            if (orderDetail.getOrder().getId() == idOrder) {
                                tempLst.add(orderDetail);
                                JFrame frame = new OrderDetailsGUI("OrderDetail ID " + idOrder);
                                frame.setLocationRelativeTo(null);

                                break;
                            }
                        }
                    }
                }
            }
        });
//      EVENT
//        Clear Product button
        btnNewProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearInput();
            }
        });

//      New Product Button
        btnAddProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                add();
            }
        });
//        Delete one
        btnDeleteProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                deleteOne();
            }
        });

//      Delete all
        btnDeleteAllProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                deleteAll();
            }
        });

//      Click a row in table Product
        tbProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickRowInTable();
            }
        });
//      Edit
        btnEditProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                edit();
            }
        });
        btnUploadImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getProductImage();
            }
        });
        btnCancelOrder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cancelOrder();
            }
        });
        btnSaveOrder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (add < 0) {
                    JOptionPane.showMessageDialog(null, "You cannot pay, please select the product!", "Warming", JOptionPane.INFORMATION_MESSAGE);
                }else {
                    Object result = JOptionPane.showInputDialog(panelProductOrder, "Enter waiting number:", "Waiting number", JOptionPane.OK_CANCEL_OPTION);
                    if (result != null) {
                        payment(Integer.parseInt((String) result));
                    }
                }
            }
        });

        btnSearchProOrder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!txtSearchProOrder.getText().equals("") && clickProOrder == 1 && !txtSearchProOrder.getText().equals(" ")) {
                    searchProOrder();
                }else {
                    txtSearchProOrder.setText("Search by name");
                    JOptionPane.showMessageDialog(null, "Please enter a name to search.");
                }
            }
        });
        txtSearchProOrder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtSearchProOrder.setText("");
                clickProOrder = 1;
            }
        });

        cbMonth.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED){
                    fillStatics();
                }
            }
        });
        btnSearchProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!txtSearchProduct.getText().equals("") && clickPro == 1 && !txtSearchProduct.getText().equals(" ")) {
                    searchProduct();
                }else {
                    txtSearchProduct.setText("Search by name");
                    JOptionPane.showMessageDialog(null, "Please enter a name to search.");
                }
            }
        });
        txtSearchProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtSearchProduct.setText("");
                clickPro = 1;
            }
        });
        tblManagement.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                spinner.setValue(0);
                productPanel.removeAll();
                fillOrder(productController.getProductList());

                panelBestSelling.removeAll();
                top5BestSelling = getTop5BestSelling();
                fillBestSelling();
            }
        });
        tbStatistic.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                JOptionPane.showMessageDialog(null, "You are not allowed to enter data directly on the table.", "Warning", JOptionPane.WARNING_MESSAGE);

            }
        });
        txtSearchProduct.addMouseMotionListener(new MouseMotionAdapter() {
        });
        txtSearchProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                txtSearchProduct.setText("");
            }
        });
        tbOrder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
    }

    private void searchProOrder() {
        List<Product> productList = new ArrayList<>();

        for (Product product: productController.getProductList()) {
            if (product.getName().toUpperCase().contains(txtSearchProOrder.getText().toUpperCase())) {
                productList.add(product);
            }
        }

        if (productList.size() > 0) {
            productPanel.removeAll();
            fillOrder(productList);
        }else {
            JOptionPane.showMessageDialog(null, "Cannot find name \"" + txtSearchProOrder.getText() + "\"", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Not found");
        }
    }

    private void searchProduct() {
        List<Product> productList = new ArrayList<>();

        for (Product product: productController.getProductList()) {
            if (product.getName().toUpperCase().contains(txtSearchProduct.getText().toUpperCase())) {
                productList.add(product);
            }
        }

        if (productList.size() > 0) {
            productController.fillToTable(productList);
        }else {
            JOptionPane.showMessageDialog(null, "Cannot find name \"" + txtSearchProduct.getText() + "\"", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Not found");
            txtSearchProduct.setText("Search by name");

        }
    }
    private void fillStatics() {
        List<Order> orderList = (List<Order>)XFile.readObject(pathOrder);
        if (orderList != null) {
//           SORT BY
//           Display data when sort by. Sort by: default current month
            String monthName = cbMonth.getSelectedItem().toString();
            int monthNumber = XUtils.convertMonth(monthName);

            if (getTempOrderLst == null || getTempOrderLst.size() < 0) { // initialization for the first time
                getTempOrderLst = new ArrayList<>();
            }else {
                getTempOrderLst.removeAll(getTempOrderLst); // remove to save new obj in new month
            }

            for (Order order:orderList) { //find obj by month
                if (XUtils.convertDatetoMonthInteger(order.getDate()).equals(String.valueOf(monthNumber))) {
                    getTempOrderLst.add(order); //get temp List getTempOrderDetailsLst
                }
            }

            float total = orderController.fillToTable(getTempOrderLst); //        Display table Statics and calculator total
            txtTotalByMonth.setText("$" + total);

//           BEST SELLING
            top5BestSelling = getTop5BestSelling();
            panelBestSelling.removeAll();
            fillBestSelling();
        }
    }

    private List<Map.Entry<String, Integer>> getTop5BestSelling() {
        List<OrderDetail> orderDetailList = (List<OrderDetail>)XFile.readObject(pathOrderDetail);
        Map<String, Integer> bestSelling = new HashMap<>();
        String monthName = cbMonth.getSelectedItem().toString();
        int monthNumber = XUtils.convertMonth(monthName);

        if (orderDetailList != null && orderDetailList.size() > 0) {
            for (OrderDetail od:orderDetailList) {
                if (XUtils.convertDatetoMonthInteger(od.getOrder().getDate()).equals(String.valueOf(monthNumber))) {
                    if (bestSelling.containsKey(od.getProduct().getId())) {  // return true when id existed
                        bestSelling.replace(od.getProduct().getId(), bestSelling.get(od.getProduct().getId()) + od.getQuantity()); // update quantity
                    } else {
                        bestSelling.put(od.getProduct().getId(), od.getQuantity());  // add to Dictionary
                    }
                }
            }
        }

//       Get top 5
        List<Map.Entry<String, Integer>> bestSellingLst = new ArrayList<Map.Entry<String, Integer>>(bestSelling.entrySet());
        Collections.sort(bestSellingLst, (o1, o2) -> Integer.compare(o2.getValue(), o1.getValue())); // Sort the List object in descending order based on the values

        return bestSellingLst.subList(0, Math.min(bestSellingLst.size(), 5));
    }

    private void fillBestSelling() {
        List<Product> productList = (List<Product>)XFile.readObject(filePath);
        if (productList != null) {
            for (Product product : productList) {
                if (top5BestSelling == null) {
                    break;
                }
                for (Map.Entry<String, Integer> top5 : top5BestSelling) { // Print the top 5 bestselling
                    if (top5.getKey().equals(product.getId()) && product.isStatus()) {
//                      Create layout Best Selling Panel
                        JPanel panel = new JPanel(new GridBagLayout());
                        GridBagConstraints bagConstraints = new GridBagConstraints();
                        bagConstraints.fill = GridBagConstraints.HORIZONTAL;
                        bagConstraints.insets = new Insets(5, 5, 5, 5);

//                      Create image of product
                        JPanel image = new JPanel();
                        Color bg = new Color(237, 237, 237);
                        image.setBackground(bg); // Set background

                        Image img = new ImageIcon("src/Images/" + product.getImage()).getImage().getScaledInstance(130, 130, Image.SCALE_DEFAULT);
                        ImageIcon icon = new ImageIcon(img);
                        JLabel imageLb = new JLabel();
                        imageLb.setIcon(icon);
                        image.add(imageLb);

                        bagConstraints.gridx = 0;
                        bagConstraints.gridy = 0;
                        panel.add(image, bagConstraints);

//                      Create name of product
                        String proName = product.getName();
//                      Custom proName
                        String[] arrName = proName.split(" ");
                        for (int j = 0; j < arrName.length; j++) {
                            if (j % 2 != 0) {
                                arrName[j] += "<br/>";
                            } else {
                                arrName[j] += " ";
                            }
                        }
                        StringBuilder name = new StringBuilder();
                        for (int j = 0; j < arrName.length; j++) {
                            name.append(arrName[j]);
                        }

                        JLabel lbProName = new JLabel("<html>" + name + "</html>", SwingConstants.CENTER);
                        lbProName.setFont(new Font("Century Schoolbook", Font.BOLD, 13));

//                      Create quantity
                        JLabel lbItem = new JLabel();
                        lbItem.setFont(new Font("Century Schoolbook", Font.PLAIN, 14));
                        lbItem.setText(top5.getValue() + " items");

                        JPanel nameAndItem = new JPanel();
                        nameAndItem.add(lbProName);
                        nameAndItem.add(lbItem);

                        bagConstraints.gridx = 0;
                        bagConstraints.gridy = 1;
                        panel.add(nameAndItem, bagConstraints);

                        panelBestSelling.add(panel);
                        revalidate();
                        repaint();
                    }
                }
            }
        }
    }
    //    Custom Product in panel Order
    private void createUIComponents() {
        productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(4,4));

        productScrollPanel = new JScrollPane(productPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        productScrollPanel.setPreferredSize(new Dimension(2000, 1000));

        List<Product> productList = (List<Product>)XFile.readObject(filePath);
        if (productList != null) {
            fillOrder(productList);
        }
        panelBestSelling = new JPanel();
        panelBestSelling.setLayout(new GridLayout(1,5));

        scrollPaneBestSelling = new JScrollPane(panelBestSelling, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneBestSelling.setPreferredSize(new Dimension(2000, 1000));
    }

    private void fillOrder(List<Product> productList){ // Product in Order Page
        for (Product product : productList) {
            if (product.isStatus()) {
//                  Create layout product Panel
                layoutProductPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 5, 5);

//                  Create image of product
                JPanel imagePanel = new JPanel();
                imagePanel.setBackground(Color.white); // Set background
                imagePanel.setToolTipText(product.getIngredient());

                Border blackline = BorderFactory.createLineBorder(Color.black);
                imagePanel.setBorder(blackline); // Set border for panel

                Image img = new ImageIcon("src/Images/" + product.getImage()).getImage().getScaledInstance(100, 110, Image.SCALE_DEFAULT);
                ImageIcon imageIcon = new ImageIcon(img);
                JLabel imgLabel = new JLabel();
                imgLabel.setIcon(imageIcon);
                imagePanel.add(imgLabel);
                imgLabel.setToolTipText(product.getIngredient());

                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.gridwidth = 2;
                layoutProductPanel.add(imagePanel, gbc);

//                  Create name of product
                String text = product.getName();
                String[] words = text.split(" ");
                for (int j = 0; j < words.length; j++) {
                    if (j % 2 != 0) {
                        words[j] += "<br/>";
                    } else {
                        words[j] += " ";
                    }
                }
                StringBuilder productName = new StringBuilder();
                for (int j = 0; j < words.length; j++) {
                    productName.append(words[j]);
                }

                JLabel lbProductName = new JLabel("<html>" + productName + "</html>", SwingConstants.CENTER);
                lbProductName.setFont(new Font("Century Schoolbook", Font.BOLD, 12));
                lbProductName.setToolTipText(product.getId());

                gbc.gridx = 1;
                gbc.gridy = 1;
                gbc.gridwidth = 2;
                layoutProductPanel.add(lbProductName, gbc);

//                  Create choose qty
                JLabel lbQty = new JLabel("Quantity");
                lbQty.setFont(new Font("Georgia", Font.PLAIN, 13));

                SpinnerModel sm = new SpinnerNumberModel(0, 0, product.getQuantity(), 1);
                JSpinner spinner = new JSpinner(sm);
                spinner.setFont(new Font("Century Schoolbook", Font.PLAIN, 13));
                spinner.setPreferredSize(new Dimension(100, 23));

                gbc.gridx = 0;
                gbc.gridy = 2;
                layoutProductPanel.add(lbQty, gbc);

                gbc.gridx = 2;
                gbc.gridy = 2;
                layoutProductPanel.add(spinner, gbc);

//                  Create product price
                JLabel lbPrice = new JLabel("Price");
                lbPrice.setFont(new Font("Georgia", Font.PLAIN, 13));

                float productPrice = product.getPrice();
                JLabel lbProductPrice = new JLabel("$" + productPrice, SwingConstants.CENTER);
                lbProductPrice.setFont(new Font("Century Schoolbook", Font.BOLD, 12));

                gbc.gridx = 0;
                gbc.gridy = 3;
                layoutProductPanel.add(lbPrice, gbc);

                gbc.gridx = 2;
                gbc.gridy = 3;
                layoutProductPanel.add(lbProductPrice, gbc);

//                  Create add to cart button
                JButton addCart = new JButton("Add To Cart");
                Color bg = new Color(178, 40, 48);
                addCart.setBackground(bg);
                addCart.setForeground(Color.white);
                addCart.setFont(new Font("Century Schoolbook", Font.PLAIN, 14));
                addCart.setActionCommand(product.getId());

//                  Add to cart
                addCart.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (spinner.getValue().equals(0)) {
                            JOptionPane.showMessageDialog(null, "Please choose quantity!", "Warning", JOptionPane.WARNING_MESSAGE);
                        }else {
                            String productID = lbProductName.getToolTipText();
                            addToCart(productID, product.getImage(), productName, (Integer) spinner.getValue(), productPrice * (Integer) spinner.getValue());
                            spinner.setValue(0);
                            add = 1;
                        }
                    }
                });

                gbc.gridx = 1;
                gbc.gridy = 4;
                gbc.gridwidth = 2;
                layoutProductPanel.add(addCart, gbc);

                productPanel.add(layoutProductPanel);
                repaint();
                revalidate();
            }
        }
    }

    private void clearOrder() {
        tableOrderModel.setRowCount(0); // delete all rows
        tableOrderModel.fireTableDataChanged(); // update the table mode
        tbOrder.repaint(); // refresh the JTable
        txtVAT.setText("0");
        txtSubtotal.setText("0");
        txtTotal.setText("0");
    }

    private void cancelOrder() {
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure to cancel this Order", "Remove",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (answer == JOptionPane.YES_OPTION) {
            clearOrder();
        }
    }

    //    Image for Product
    private void getProductImage() {
        String userDir = System.getProperty("user.dir");
        JFileChooser fileChooser = new JFileChooser(userDir);
        FileNameExtensionFilter nameExtensionFilter = new FileNameExtensionFilter("IMAGES", "png", "jpg", "jpeg");
        fileChooser.addChoosableFileFilter(nameExtensionFilter);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//            BufferedImage img = XFile.readImage(fileChooser);
            File f = fileChooser.getSelectedFile();
            BufferedImage img = XFile.readImage(f);

            Image i = img.getScaledInstance(lbProductImage.getWidth(), lbProductImage.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(i);
            lbProductImage.setIcon(icon); // Display image in Panel
            txtProductImage.setText("1 file chosen");
            txtProductImage.setToolTipText(fileChooser.getSelectedFile().getName()); // get name of image
            originalPath = fileChooser.getSelectedFile().getPath();
        }
    }

//    Show a row into Input
    private void clickRowInTable() {
        btnAddProduct.setEnabled(false);
        row = tbProduct.getSelectedRow();
        showIntoInput(row);
    }

//    Function: Show a row into Input
    private void showIntoInput(int row) {
        clearErrorProduct();

        if (row >= 0) {
            lbStatus.setVisible(true);
            rdSale.setVisible(true);
            rdSoldOut.setVisible(true);

            String id = (String) tbProduct.getValueAt(row, 0);
            txtProductID.setText(id);
            txtProductID.setVisible(false);
            lbProductID.setVisible(false);

            String name = (String) tbProduct.getValueAt(row, 1);
            txtProductName.setText(name);

            String ingredient = (String) tbProduct.getValueAt(row, 2);
            txtProductIngredient.setText(ingredient);

            Float price = (Float) tbProduct.getValueAt(row, 3);
            txtProductPrice.setText(String.valueOf(price));

            int quantity = (int) tbProduct.getValueAt(row, 4);
            btnProductQuantity.setValue(quantity);

            String status = (String) tbProduct.getValueAt(row, 5);
            if (Objects.equals(status, "Sale")) {
                rdSale.setSelected(true);
            }else {
                rdSoldOut.setSelected(true);
            }

            String image = (String) tbProduct.getValueAt(row, 6);
            txtProductImage.setText("1 file chosen");
            txtProductImage.setToolTipText(image);
            ImageIcon imageIcon = new ImageIcon(new ImageIcon("src\\Images\\" + image).getImage().getScaledInstance(200,200,Image.SCALE_DEFAULT));
            lbProductImage.setIcon(imageIcon);
        }

    }

//    Function: delete product
    private void deleteOne() {
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Please choose the product!");
            clearErrorProduct();
        }else {
            int answer = JOptionPane.showConfirmDialog(null, "Do you want to remove it", "Remove",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (answer == JOptionPane.YES_OPTION) {
                productController.deleteProduct(txtProductID.getText());
                productController.fillToTable();
                XFile.writeObject(filePath, productController.getProductList());

                clearInput();
            }
        }
    }

//    Function: delete all product
    private void deleteAll() {
        int answer = JOptionPane.showConfirmDialog(this, "Do you want to remove all", "Remove",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (answer == JOptionPane.YES_OPTION) {
            productController.deleteAllProduct();
            productController.fillToTable();
            XFile.writeObject(filePath, productController.getProductList());

            clearInput();
        }
    }

//    Edit Product
    private void edit() {
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Please choose the product!");
            clearInput();
        }else {
            lbStatus.setVisible(true);
            rdSale.setVisible(true);
            rdSoldOut.setVisible(true);

            boolean status = true;

            if (rdSoldOut.isSelected()) {
                status = false;
            }

            if (isValidProduct("edit")) {
                Product product = new Product(
                        txtProductID.getText(),
                        txtProductName.getText().toUpperCase(),
                        txtProductIngredient.getText(),
                        Float.parseFloat(txtProductPrice.getText()),
                        (Integer) btnProductQuantity.getValue(),
                        status,
                        txtProductImage.getToolTipText()
                );

//              Edit List
                productController.updateProductLst(product);

//                Check if it is old image or not
                for (Product p:productController.getProductList()) {
                    if (p.getImage().equals(txtProductImage.getToolTipText())) {
                        String newProductPath = "src\\Images\\" + txtProductImage.getToolTipText();
                        XFile.copyFile(originalPath, newProductPath); // Copy image file
                        break;
                    }
                }

                productController.fillToTable();
                XFile.writeObject(filePath, productController.getProductList());

                clearInput();
                JOptionPane.showMessageDialog(null, "A product was updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

            }
        }
    }

//    Add Product
    private void add() {
        if (isValidProduct("add")) {

            Product product = new Product(
                    txtProductID.getText(),
                    txtProductName.getText().toUpperCase(),
                    txtProductIngredient.getText(),
                    Float.parseFloat(txtProductPrice.getText()),
                    (Integer) btnProductQuantity.getValue(),
                    true,
                    txtProductImage.getToolTipText()
            );

//        add new Product from Form
            productController.addProduct(product);
//        Copy image file
            String newProductPath = "src\\Images\\" + txtProductImage.getToolTipText();
            XFile.copyFile(originalPath, newProductPath);
//        fill to Table
            productController.fillToTable();
//        Save to file
            XFile.writeObject(filePath, productController.getProductList());
//        Clear form
            clearInput();

            JOptionPane.showMessageDialog(null, "A product was added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    //    Clear Product Function
    private void clearInput() {
        tbProduct.setVisible(true);
        lbStatus.setVisible(false);
        rdSale.setVisible(false);
        rdSoldOut.setVisible(false);

        row = -1;
        btnAddProduct.setEnabled(true);
        txtProductID.setVisible(true);
        lbProductID.setVisible(true);
        txtProductID.setToolTipText("");

        txtProductID.setText("");
        txtProductName.setText("");
        txtProductIngredient.setText("");
        txtProductPrice.setText("");
        btnProductQuantity.setValue(0);
        rdSale.setSelected(true);
        txtProductImage.setText("<Choose file>");
        lbProductImage.setIcon(null);
        clearErrorProduct();
        productController.fillToTable();
    }

    private void clearErrorProduct() {
        errorProductID.setForeground(Color.white);
        errorProductName.setForeground(Color.white);
        errorProductIngredient.setForeground(Color.white);
        errorProductPrice.setForeground(Color.white);
        errorProductQuantity.setForeground(Color.white);
    }

    private boolean isValidProduct(String action) {
        clearErrorProduct();

        boolean temp = true;
        String regLetterAndNumber = "\\w+";
        String regLetterAndWhiteSpace = "^[a-zA-Z ]*$";
        String regLetter = "^[a-zA-Z,. ]*$";
        String regFloat = "^[0-9.]*$";
        String regNumber = "^[0-9]*$";

        if (action == "add") {
            if (!isDuplicate(txtProductID.getText())) {
                temp = false;
            }
        }

//        Product ID
        if (txtProductID.getText().equals("")) {
            errorProductID.setForeground(Color.red);
            errorProductID.setText("This field cannot be empty");

            temp = false;
        } else if (!txtProductID.getText().matches(regLetterAndNumber)) {
            errorProductID.setForeground(Color.red);
            errorProductID.setText("This field cannot contain the characters.");

            temp = false;
        }else if (txtProductID.getText().length() < 3) {
            errorProductID.setForeground(Color.red);
            errorProductID.setText("This field must contain more than 2 letters.");

            temp = false;
        }

//        Product name
        if (txtProductName.getText().equals("")) {
            errorProductName.setForeground(Color.red);
            errorProductName.setText("This field cannot be empty");

            temp = false;
        } else if (!txtProductName.getText().matches(regLetterAndWhiteSpace)) {
            errorProductName.setForeground(Color.red);
            errorProductName.setText("This field cannot contain the characters or number.");

            temp = false;
        }else if (txtProductName.getText().length() < 3) {
            errorProductName.setForeground(Color.red);
            errorProductName.setText("This field must contain more than 2 letters.");

            temp = false;
        }

//        Product Ingredient
        if (txtProductIngredient.getText().equals("")) {
            errorProductIngredient.setForeground(Color.red);
            errorProductIngredient.setText("This field cannot be empty");

            temp = false;
        }else if (!txtProductIngredient.getText().matches(regLetter)) {
            errorProductIngredient.setForeground(Color.red);
            errorProductIngredient.setText("This field cannot contain the characters or number.");

            temp = false;
        }else if (txtProductIngredient.getText().length() < 3) {
            errorProductIngredient.setForeground(Color.red);
            errorProductIngredient.setText("This field must contain more than 2 letters.");

            temp = false;
        }

//        Product Price
        if (txtProductPrice.getText().equals("")) {
            errorProductPrice.setForeground(Color.red);
            errorProductPrice.setText("This field cannot be empty");

            temp = false;
        }else if (!txtProductPrice.getText().matches(regFloat)) {
            errorProductPrice.setForeground(Color.red);
            errorProductPrice.setText("This field must be the number greater than 0.");

            temp = false;
        }

//        Quantity
        try{
            int quantity = (int) btnProductQuantity.getValue();
            if (quantity < 1) {
                errorProductQuantity.setForeground(Color.red);
                errorProductQuantity.setText("Quantity must be a number greater than 0");

                temp = false;
            }
        }catch (Exception e){
            errorProductQuantity.setForeground(Color.red);
            errorProductQuantity.setText("Quantity must be a number greater than 0");
        }
        return temp;
    }
    public boolean isDuplicate(String id) {
        String temp = null;

        for (Product product: productController.getProductList()) {
            if (product.getId().equals(id)) {
                errorProductID.setForeground(Color.red);
                errorProductID.setText("Product ID already exists.");

                temp = id;
                break;
            }
        }

        if (temp == null) {
            return true;
        }else {
            return false;
        }
    }

    private void addToCart(String productID, String image, StringBuilder productName, int quantity, float price) {
        boolean temp = true;  // variable to check Product exist or not

//        When table cannot data, index of tbOrder = -1
        if (tbOrder.getRowCount() > 0) {
            for (int i = 0; i < tbOrder.getRowCount(); i++) {
                String id = (String) tbOrder.getValueAt(i, 0);

                if (id.equals(productID)) {
//                    get old value in Order table
                    int oldQuantity = (Integer) tbOrder.getValueAt(i, 3);

//                    check new value is valid. If return value of method checkUpdateQuantityAndPrice = 0, error
                    int[] results = checkUpdateQuantityAndPrice(id, quantity, oldQuantity);

                    if (results[0] < 0) {
                        JOptionPane.showMessageDialog(null, "Some thing error. The current stock quantity is " + results[1], "Warning", JOptionPane.WARNING_MESSAGE);
                        temp = false;
                        break;
                    }else {
                        tbOrder.setValueAt(results[0], i, 3);
                        tbOrder.setValueAt(results[0] * price, i, 4);
                        temp = false;
                    }
                    break;
                }
            }
        }

        if (temp) {
            tableOrderModel.addRow(new Object[]{productID, image, productName, quantity, price});
        }

        prepareInvoice();   //update total, subtotal, vat
    }

    private int[] checkUpdateQuantityAndPrice(String id, int quantity, int oldQuantity) {
        int[] grid = new int[2];

        for (Product product : productController.getProductList()) {
            if (product.getId().equals(id)) {
//              Check total quantity added is valid or not
                int newQuantity = quantity + oldQuantity;
                int tempQty = product.getQuantity() - newQuantity;

//              If quantity < stock => temQty < 0
                if (tempQty > 0 ) {
                    grid[0] = newQuantity;
                    grid[1] = product.getQuantity();
                }else {
                    grid[0] = -1;
                    grid[1] = product.getQuantity();
                }
            }
        }

        return grid;
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");

    //    Display VAT, subtotal, total
    private void prepareInvoice() {
        Float subtotal = 0F;

        if (tbOrder.getRowCount() > 0) {
            for (int i = 0; i < tbOrder.getRowCount(); i++) {
                float price = (float) tbOrder.getValueAt(i, 4);
                subtotal += price;
            }
            df.setRoundingMode(RoundingMode.UP);
            txtVAT.setText(String.valueOf(df.format(subtotal * 0.1)));
            txtSubtotal.setText(df.format(subtotal));
            txtTotal.setText(String.valueOf(df.format(subtotal + subtotal * 0.1)));
        }else {
            txtVAT.setText("0");
            txtSubtotal.setText("0");
            txtTotal.setText("0");
        }
    }

    private void payment(int result) {
        int a = orderController.getOrderList().size();
        System.out.println("size" + a);
//        SAVE ORDER
        Order order = new Order(
                orderController.getOrderList().size() + 1,
                Float.parseFloat(txtTotal.getText()),
                XUtils.convertStringtoDate(txtDay.getText()),
                Float.parseFloat(txtVAT.getText()),
                result
        );

        orderController.add(order); //        add new Order from Form
        XFile.writeObject(pathOrder, orderController.getOrderList()); //        Save to file

//        SAVE ORDER DETAILS
        for (int index = 0; index < tbOrder.getRowCount(); index++) {
            String id = String.valueOf(tbOrder.getValueAt(index, 0));
            System.out.println(id);
            for (Product product:productController.getProductList()) {
                if (product.getId().equals(id)) {
                    int quantity = (int) tbOrder.getValueAt(index, 3);
                    OrderDetail orderDetail = new OrderDetail(
                            product, order, quantity
                    );
//                    save order detail
                    orderDetailsController.add(orderDetail);  //    Add new OrderDetail from Form
                    XFile.writeObject(pathOrderDetail, orderDetailsController.getOrderDetailList());  //  Save to file

//                    Update product quantity
                    product.setQuantity(product.getQuantity() - quantity); //    Subtract the quantity in stock
                    if (product.getQuantity() <= 0) { product.setStatus(false); } //   set status if quantity = 0

                    productController.updateQtyProductLst(product);
                    productController.fillToTable();
                    XFile.writeObject(filePath, productController.getProductList());
                }
            }
        }

//        Clear table
        clearOrder();
//        fill to table Product a
        productController.fillToTable();
        fillStatics(); //        fill to Table
    }

    private void exitProgram() {
        int answer = JOptionPane.showConfirmDialog(this, "Do you want to Logout", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (answer == JOptionPane.YES_OPTION){
            JFrame login = new LoginGUI("Login");
            login.setVisible(true);
            login.setLocationRelativeTo(null);
            dispose();
        }
    }
}
