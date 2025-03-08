/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.form;

import com.mycompany.dao.SubjectDAO;
import com.mycompany.model.Subject;
import com.mycompany.component.SubjectsHeaderPanel;
import com.mycompany.component.ActionCellRenderer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

public class Subjects extends javax.swing.JPanel {
    
    private SubjectsHeaderPanel headerPanel;
    private JPanel tablePanel;
    private JLabel currentSubjectsLabel;
    private JTextField searchField;
    private JTable subjectsTable;
    private JScrollPane scrollPane;
    private DefaultTableModel dtm;
    
    // Store all data locally for filtering
    private final java.util.List<Subject> allSubjects;

    /**
     * Creates new form Form_1
     */
    public Subjects() {
        // Initialize list from DAO once (you can also load dynamically if needed)
        allSubjects = SubjectDAO.getAllSubjects();
        initComponents();
        initCustomComponents(); // Our custom initialization for the JTable interface
    }
    
    
    private void initCustomComponents() {
    // Set the background color of the main panel.
    this.setBackground(new Color(237, 241, 247));
    // Use a BorderLayout for the main panel.
    setLayout(new BorderLayout());
    
    // ---------------- Create Header and Table Panels ----------------
    
    // Header panel remains the same; ensure it sets its own bounds or preferred size.
    headerPanel = new SubjectsHeaderPanel();
    headerPanel.setPreferredSize(new Dimension(820, 60));
    // (Inside SubjectsHeaderPanel, you already have absolute positioning for its components.)
    
    // Table panel: we keep your current absolute positioning for inner components.
    tablePanel = new JPanel(null);
    tablePanel.setBackground(Color.WHITE);
    tablePanel.setPreferredSize(new Dimension(820, 340));
    
    // Add "Current Subjects" label.
    currentSubjectsLabel = new JLabel("Current Subjects");
    currentSubjectsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
    currentSubjectsLabel.setForeground(new Color(51, 51, 51));
    currentSubjectsLabel.setBounds(20, 20, 200, 25);
    tablePanel.add(currentSubjectsLabel);
    
    // Search field with placeholder "search"
    searchField = new JTextField();
    searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
    searchField.setForeground(new Color(102, 102, 102));
    searchField.setBounds(620, 20, 180, 30);
    searchField.putClientProperty("JTextField.placeholderText", "search...");
    tablePanel.add(searchField);
    
    // Table setup
    subjectsTable = new JTable();
    subjectsTable.setRowHeight(30);
    subjectsTable.setForeground(new Color(125,125,125));
    subjectsTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
    subjectsTable.getTableHeader().setReorderingAllowed(false);
    subjectsTable.getTableHeader().setBackground(new Color(245, 245, 245));
    subjectsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
    // Remove borders from the table.
    subjectsTable.setShowGrid(false);
    subjectsTable.setIntercellSpacing(new Dimension(0, 0));
    
    // Ensure the entire table (including unused area) is white.
    subjectsTable.setFillsViewportHeight(true);
    subjectsTable.setBackground(Color.WHITE);

    // Override the default renderer so that every cell's background remains white.
    subjectsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(Color.WHITE);
            }
            return c;
        }
    });
    
    dtm = new DefaultTableModel();
    dtm.setColumnIdentifiers(new String[] { "#", "Subject Name", "Coefficient", "Teacher Name", "Action" });
    subjectsTable.setModel(dtm);
    
    // Set the custom renderer for the Action column.
    subjectsTable.getColumnModel().getColumn(4).setCellRenderer(new ActionCellRenderer());
    
    // Create scroll pane and remove its border.
    scrollPane = new JScrollPane(subjectsTable);
    scrollPane.setBorder(null);
    scrollPane.getViewport().setBackground(Color.WHITE); // Ensure viewport background is white
    scrollPane.setBounds(20, 70, 780, 250);
    tablePanel.add(scrollPane);
    
    // Fill the table with initial data.
    fillTable("");
    
    // Wire up search filtering.
    searchField.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String filter = searchField.getText().trim().toLowerCase();
            fillTable(filter);
        }
    });
    
    // ---------------- Wrap the Header and Table Panels for Centering ----------------
    
    // Create a vertical box container to hold the header and table panels.
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.setOpaque(false);
    
    // Set components' alignment so they are centered horizontally.
    headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    tablePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    // Add the header and table panels (with optional vertical spacing).
    contentPanel.add(headerPanel);
    contentPanel.add(Box.createVerticalStrut(10));  // optional spacing between panels
    contentPanel.add(tablePanel);
    
    // Create a container panel with FlowLayout (center alignment) to ensure
    // equal free space on left and right.
    JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    centerPanel.setOpaque(false);
    centerPanel.add(contentPanel);
    
    // Finally, add the centerPanel to the main panel.
    add(centerPanel, BorderLayout.CENTER);
    }
    
    private void fillTable(String filter) {
        dtm.setRowCount(0);
        int count = 0;
        for (Subject subject : allSubjects) {
            if (filter.isEmpty() ||
               subject.getName().toLowerCase().contains(filter) ||
               subject.getTeacherNames().toLowerCase().contains(filter)) {
                count++;
                dtm.addRow(new Object[]{
                    count,
                    subject.getName(),
                    subject.getCoefficient(),
                    subject.getTeacherNames(),
                    "Edit/Delete" // Placeholder; the renderer will handle this
                });
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
