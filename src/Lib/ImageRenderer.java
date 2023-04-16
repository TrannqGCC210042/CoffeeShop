package Lib;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ImageRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String name = value.toString();
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("src\\Images\\" + name).getImage().getScaledInstance(40,40,Image.SCALE_DEFAULT));
        return new JLabel(imageIcon);
    }
}
