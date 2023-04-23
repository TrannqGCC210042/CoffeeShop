package Lib;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRemove extends AbstractCellEditor implements TableCellRenderer {
    JButton button;

    public ButtonRemove() {
        button = new JButton("x");
        button.setBackground(Color.white);
        button.setForeground(Color.black);
        button.setFont(new Font("Century Schoolbook", Font.BOLD, 16));
    }

    public Object getCellEditorValue() {
        return button;
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return button;
    }
}
