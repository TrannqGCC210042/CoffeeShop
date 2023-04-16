package View;

import Controller.ProductController;
import Lib.ImageRenderer;
import Lib.XFile;
import Model.Product;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


public class ProductGUI extends JFrame {
    private JPanel panelStaff;

    private JLabel lbProduct1;
    private JLabel product3;
    private JLabel product11;

    private JLabel product4;
    private JLabel product15;
    private JLabel product17;

    private JLabel product12;
    private JLabel product13;
    private JTextField txtSearch;
    private JTabbedPane tblManagement;
    private JPanel panelProductOrder;
    private JLabel product5;
    private JLabel product6;
    private JLabel product7;
    private JLabel product8;
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
    private JLabel product2;
    private JLabel product9;
    private JLabel product10;
    private JLabel product14;
    private JLabel product16;
    private JLabel product18;
    private JButton btnAddProduct;
    private JButton btnDeleteAllProduct;
    private JTextField txtDay;
    private JLabel product19;
    private JLabel product20;
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
    private JButton newButton;
    private JButton editButton;
    private JButton deleteButton;
    String filePath = "src\\File\\products.dat";
    DefaultTableModel tableOrderModel;
    List<Product> productsList;
    int row = -1;
    ProductController productController;
    String originalPath;

    public ProductGUI(String title) throws HeadlessException{
        super(title);
        this.setVisible(true);
        this.setContentPane(panelStaff);
        this.pack();
        int delay = 100;

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

//        Create class ProductController to use method into it
        productController = new ProductController(
                (DefaultTableModel) tbProduct.getModel(),
                (List<Product>) XFile.readObject(filePath)
        );

        productController.fillToTable();



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
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("src\\Images\\" + image).getImage().getScaledInstance(200,200,Image.SCALE_DEFAULT));
        lbProductImage.setIcon(imageIcon);
    }

//    Function: delete product
    private void deleteOne() {
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please choose the product!");
            clearErrorProduct();
        }else {
            int answer = JOptionPane.showConfirmDialog(this, "Do you want to remove it", "Remove",
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
            JOptionPane.showMessageDialog(this, "Please choose the product!");
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
//              Copy image file
                String newProductPath = "src\\Images\\" + txtProductImage.getToolTipText();
                XFile.copyFile(originalPath, newProductPath);

                productController.fillToTable();
                XFile.writeObject(filePath, productController.getProductList());

                clearInput();
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
        }
    }


    //    Clear Product Function
    private void clearInput() {
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

        for (Product product : (List<Product>)XFile.readObject(filePath)) {
//            Create layout product Panel
            JPanel layoutProductPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);

//            Create image of product
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

//            Create name of product
            String text = "FREEZE COFFEE FREEZE";
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

//            Create choose qty
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

            //            Create product price
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

//            Create add to cart button
            JButton addToCart = new JButton("Add To Cart");
            Color bg = new Color(6, 169, 177);
            addToCart.setBackground(bg);
            addToCart.setForeground(Color.white);
            addToCart.setFont(new Font("Century Schoolbook", Font.PLAIN, 14));
            addToCart.setActionCommand(product.getId());

//        Add to cart
            addToCart.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    addToOrder(lbProductName.getToolTipText(), product.getImage(), productName, (Integer) spinner.getValue(), productPrice);
                }
            });

            gbc.gridx = 1;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            layoutProductPanel.add(addToCart, gbc);

            productPanel.add(layoutProductPanel);;
        }
    }

    private void addToOrder(String productID, String image, StringBuilder productName, int quantity, float price) {
        boolean temp = true;  // variable to check Product exist or not

//        When table cannot data, index of tbOrder = -1
        if (tbOrder.getRowCount() > 0) {
            for (int i = 0; i < tbOrder.getRowCount(); i++) {
                String id = (String) tbOrder.getValueAt(i, 0);

                if (id.equals(productID)) {
                    int oldQuantity = (Integer) tbOrder.getValueAt(i, 3);
                    int newQuantity = quantity + oldQuantity;
                    tbOrder.setValueAt(newQuantity, i, 3);

                    temp = false;
                    break;
                }
            }
        }

        if (temp) {
            tableOrderModel.addRow(new Object[]{productID, image, productName, quantity, price});
        }

    }
}
