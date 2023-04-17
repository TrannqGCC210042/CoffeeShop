package View;

import Controller.OrderController;
import Controller.OrderDetailsController;
import Controller.ProductController;
import Controller.StaffController;
import Lib.ButtonEditor;
import Lib.ImageRenderer;
import Lib.XFile;
import Model.Order;
import Model.OrderDetail;
import Model.Product;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
    private JButton btnSearch;
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
    private JTable tbStatics;
    private JButton btnASCday;
    private JButton btnDESCday;
    private JButton newButton;
    private JButton editButton;
    private JButton deleteButton;
    String filePath = "src\\File\\products.dat";
    String pathOrder = "src\\File\\orders.dat";
    String pathOrderDetail = "src\\File\\orderDetails.dat";
    DefaultTableModel tableOrderModel;
    int row = -1;
    ProductController productController;
    OrderController orderController;
    OrderDetailsController orderDetailsController;
    String originalPath = null;

    public ProductGUI(String title) throws HeadlessException{
        super(title);
        this.setVisible(true);
        this.setContentPane(panelStaff);
        this.pack();
        int delay = 100;
        lbStatus.setVisible(false);
        rdSale.setVisible(false);
        rdSoldOut.setVisible(false);

        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                String formattedDateTime = now.format(formatter);
                txtDay.setText(formattedDateTime);
            }
        });

        timer.start();

//        Set field name for table Order
        tableOrderModel = new DefaultTableModel(
                new Object[][]{ },
                new String[]{
                        "ID", "Image", "Product Name", "Quantity", "Price"
                }
        );
        tbOrder.setModel(tableOrderModel);
//        Get image column and override cell DefaultTableCellRenderer class component method getTableCellRendererComponent
        tbOrder.setRowHeight(100);
        tbOrder.getColumnModel().getColumn(1).setCellRenderer(new ImageRenderer());

//        Limit value for Product Quantity
        SpinnerModel sm = new SpinnerNumberModel(0, 0, 1000, 1); //default value,lower bound,upper bound,increment by
        btnProductQuantity.setModel(sm);

//        Set field name for table Product
        tbProduct.setModel(new DefaultTableModel(
            new Object[][]{ },
            new String[]{
                    "ID", "Name", "Ingredient", "Price", "Quantity", "Status", "Image"
                }
        ));

//        Get image column and override cell DefaultTableCellRenderer class component method getTableCellRendererComponent
        tbProduct.setRowHeight(100);
        tbProduct.getColumnModel().getColumn(6).setCellRenderer(new ImageRenderer());

//        PRODUCT
//        Create class ProductController to use method into it
        productController = new ProductController(
                (DefaultTableModel) tbProduct.getModel(),
                (List<Product>) XFile.readObject(filePath)
        );

//        Set field name for table Product
        tbStatics.setModel(new DefaultTableModel(
                new Object[][]{ },
                new String[]{
                        "Order ID", "Date", "VAT", "Total", "Waiting Card Number", "Status", "Order Detail"
                }
        ));

//        ORDER
//        Create class ProductController to use method into it
        orderController = new OrderController(
                (DefaultTableModel) tbStatics.getModel(),
                (List<Order>) XFile.readObject(pathOrder)
        );

        orderController.fillToTable();
        tbStatics.getColumnModel().getColumn(6).setCellRenderer(new ButtonEditor());

        int desiredColumn = 6; // index of the column you want to catch clicks on
        tbStatics.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tbStatics.columnAtPoint(e.getPoint());*
                if (column == desiredColumn) {
                    JOptionPane.showMessageDialog(ProductGUI.this, "Do you want to remove it");
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
        tblManagement.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                productController.fillToTable();
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
                payment();
            }
        });
        tbProduct.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                JOptionPane.showMessageDialog(null, "You cannot edit directly in the Product table!", "Warming", JOptionPane.INFORMATION_MESSAGE);

            }
        });
    }

    private void clearOrder() {
        tableOrderModel.setRowCount(0); // delete all rows
        tableOrderModel.fireTableDataChanged(); // update the table mode
        tbOrder.repaint(); // refresh the JTable
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

        lbStatus.setVisible(true);
        rdSale.setVisible(true);
        rdSoldOut.setVisible(true);

        String id = (String) tbProduct.getValueAt(row, 0);
        txtProductID.setText(id);
        txtProductID.setEnabled(false);
        txtProductID.setToolTipText("You cannot update ID");

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
                        txtProductName.getText(),
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

                createUIComponents();

            }
        }
    }

//    Add Product
    private void add() {
        if (isValidProduct("add")) {

            Product product = new Product(
                    txtProductID.getText(),
                    txtProductName.getText(),
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

            createUIComponents();
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
        txtProductID.setEnabled(true);
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

//    Custom Product in panel Order
    private void createUIComponents() {
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(4,4));

        productScrollPanel = new JScrollPane(productPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        productScrollPanel.setPreferredSize(new Dimension(2000, 1000));

        List<Product> productList = (List<Product>)XFile.readObject(filePath);
        if (productList != null) {
            for (Product product : productList) {
                if (product.isStatus()) {

//                  Create layout product Panel
                    JPanel layoutProductPanel = new JPanel(new GridBagLayout());
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
                    Color bg = new Color(6, 169, 177);
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
                                addToCart(lbProductName.getToolTipText(), product.getImage(), productName, (Integer) spinner.getValue(), productPrice * (Integer) spinner.getValue());
                                spinner.setValue(0);
                            }
                        }
                    });

                    gbc.gridx = 1;
                    gbc.gridy = 4;
                    gbc.gridwidth = 2;
                    layoutProductPanel.add(addCart, gbc);

                    productPanel.add(layoutProductPanel);;
                }
            }
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
                    float oldPrice = (float) tbOrder.getValueAt(i, 4);

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

        prepareInvoice();
    }

    private int[] checkUpdateQuantityAndPrice(String id, int quantity, int oldQuantity) {
        int[] grid = new int[2];

        for (Product product : productController.getProductList()) {
            if (product.getId().equals(id)) {

//                Check total quantity added is valid or not
                int newQuantity = quantity + oldQuantity;
                int tempQty = product.getQuantity() - newQuantity;

//                If quantity < stock => temQty < 0
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


    //    Display VAT, subtotal, total
    private void prepareInvoice() {
        Float subtotal = 0F;

        if (tbOrder.getRowCount() > 0) {
            for (int i = 0; i < tbOrder.getRowCount(); i++) {
                float price = (float) tbOrder.getValueAt(i, 4);
                subtotal += price;
            }
            txtVAT.setText(String.valueOf(subtotal * 0.1));
            txtSubtotal.setText(String.valueOf(subtotal));
            txtTotal.setText(String.valueOf(subtotal + subtotal * 0.1));
        }else {
            txtVAT.setText("0");
            txtSubtotal.setText("0");
            txtTotal.setText("0");
        }

    }

    private void payment() {
        int i = 0; //get data in table

//        Subtract the quantity in stock
        for (Product product : productController.getProductList()) {
            String name = (String) tbOrder.getValueAt(i, 1);
            int quantity = (int) tbOrder.getValueAt(i, 3);

            if (product.getName().equals(name)){
                int isValid = product.getQuantity() - quantity;
                if (isValid < 0) {
                    product.setQuantity(isValid);
                }else {
                    JOptionPane.showMessageDialog(null, "Some thing error quantity in Stock. Please restart the program!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    Window window = SwingUtilities.getWindowAncestor(panelOrder);
                    window.dispose();
                }
            }
            i++;
        }

//        SAVE ORDER
        Order order = new Order(
                Float.parseFloat(txtTotal.getText()),
                txtDay.getText(),
                Float.parseFloat(txtVAT.getText()),
                2
        );

        orderController.add(order); //        add new Order from Form
        orderController.fillToTable(); //        fill to Table
        XFile.writeObject(pathOrder, orderController.getOrderList()); //        Save to file

//        SAVE ORDER DETAILS
        i = 0;
        for (Product product:productController.getProductList()) {
            String name = (String) tbOrder.getValueAt(i, 1);
            if (product.getName().equals(name)) {
                int quantity = (int) tbOrder.getValueAt(i, 3);
                OrderDetail orderDetail = new OrderDetail(
                        product, order, quantity
                );
                orderDetailsController.add(orderDetail);  // Add new OrderDetail from Form
                XFile.writeObject(pathOrder, orderDetailsController.getOrderDetailList());  // Save to file
            }
            i++;
        }
//        Clear table
        clearOrder();
    }

}
