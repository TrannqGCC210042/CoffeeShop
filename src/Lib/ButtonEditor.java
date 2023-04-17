package Lib;

import View.AdminGUI;
import View.ProductGUI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonEditor extends AbstractCellEditor implements TableCellRenderer {
    JButton button;

    public ButtonEditor() {
        button = new JButton("See more >>");

        Color bg = new Color(6, 169, 177);
        button.setBackground(bg);
        button.setForeground(Color.white);
        button.setFont(new Font("Century Schoolbook", Font.PLAIN, 14));
        button.setActionCommand("SP01");
    }

    public Object getCellEditorValue() {
        return button;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return button;
    }
}
