package com.mycompany.component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ActionCellRenderer extends DefaultTableCellRenderer {
    private final Icon editIcon;
    private final Icon deleteIcon;

    public ActionCellRenderer() {
        // Adjust the paths as necessary to point to your icon resources.
        editIcon = new ImageIcon(getClass().getResource("/images/edit.png"));
        deleteIcon = new ImageIcon(getClass().getResource("/images/trash.png"));
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setOpaque(true);
        panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);

        JLabel editLabel = new JLabel(editIcon);
        JLabel deleteLabel = new JLabel(deleteIcon);
        panel.add(editLabel);
        panel.add(deleteLabel);

        return panel;
    }
}
