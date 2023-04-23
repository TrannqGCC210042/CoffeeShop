package Controller;

import Lib.XFile;
import Model.Product;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    DefaultTableModel tableProductModel;
    List<Product> productList;

    public ProductController(DefaultTableModel tableProductModel, List<Product> productList) {
        this.tableProductModel = tableProductModel;
        this.productList = productList;

        if (this.productList == null || this.productList.size() == 0) {
            this.productList = new ArrayList<>();
        }
    }

    public DefaultTableModel getTableProductModel() {
        return tableProductModel;
    }

    public void setTableProductModel(DefaultTableModel tableProductModel) {
        this.tableProductModel = tableProductModel;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public void fillToTable() {
        tableProductModel.setRowCount(0);
        for (Product product: productList) {
            String status = "";

            if (product.isStatus()) {
                status = "Sale";
            }
            else {
                status = "Out of Stock";
            }

            Object[] rowObj = new Object[] {
                    product.getId(), product.getName(), product.getIngredient(), product.getPrice(), product.getQuantity(), status, product.getImage()
            };

            tableProductModel.addRow(rowObj);
        }
    }
    public void fillToTable(List<Product> productList) {  // using search
        tableProductModel.setRowCount(0);
        for (Product product: productList) {
            String status = "";

            if (product.isStatus()) {
                status = "Sale";
            }
            else {
                status = "Out of Stock";
            }

            Object[] rowObj = new Object[] {
                    product.getId(), product.getName(), product.getIngredient(), product.getPrice(), product.getQuantity(), status, product.getImage()
            };

            tableProductModel.addRow(rowObj);
        }
    }
    public void addProduct(Product product) {
        productList.add(product);
    }

    public void updateProductLst(Product product) {
        for (Product p: productList) {
            if (p.getId().equals(product.getId())) {
                p.setName(product.getName());
                p.setIngredient(product.getIngredient());
                p.setPrice(product.getPrice());
                p.setQuantity(product.getQuantity());
                p.setStatus(product.isStatus());
                p.setImage(product.getImage());
                break;
            }
        }
    }

    public void deleteProduct(String id) {
        for (Product p: productList) {
            if (p.getId().equals(id)) {
                productList.remove(p);

                break;
            }
        }
    }

    public void deleteAllProduct() {
        productList.removeAll(productList);
    }
    public void updateQtyProductLst(Product product) {
        for (Product p: productList) {
            if (p.getId().equals(product.getId())) {
                p.setName(product.getName());
                p.setIngredient(product.getIngredient());
                p.setPrice(product.getPrice());
                p.setQuantity(product.getQuantity());
                p.setStatus(product.isStatus());
                p.setImage(product.getImage());
                break;
            }
        }
    }
}
