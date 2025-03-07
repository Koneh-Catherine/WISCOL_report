/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.form;

import com.mycompany.main.databaseConnection;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Subjects extends javax.swing.JPanel {
    
    // Declare the table, its model, and scroll pane as instance variables.
    private JPanel headerPanel;
    private JLabel titleLabel;
    private JLabel breadcrumbLabel;
    private JButton addSubjectBtn;

    private JPanel tablePanel;
    private JLabel currentSubjectsLabel;
    private JTextField searchField;
    private JTable subjectsTable;
    private JScrollPane scrollPane;
    private DefaultTableModel dtm;

    
     // Store all rows from DB so we can re-filter without re-querying
    private final java.util.List<Object[]> allData = new ArrayList<>();


    /**
     * Creates new form Form_1
     */
    public Subjects() {
        initComponents();
        initCustomComponents(); // Our custom initialization for the JTable interface
    }
    
    
    /**
     * Build our custom UI and wire up data and listeners.
     */
    private void initCustomComponents() {
        // ----- Header Panel -----
        headerPanel = new JPanel(null);
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setPreferredSize(new Dimension(820, 60));
        
        titleLabel = new JLabel("Subjects");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setBounds(20, 10, 200, 25);
        
        breadcrumbLabel = new JLabel("Manage > Subjects");
        breadcrumbLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        breadcrumbLabel.setForeground(new Color(120, 120, 120));
        breadcrumbLabel.setBounds(20, 35, 200, 20);
        
        addSubjectBtn = new JButton("Add Subject");
        addSubjectBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        addSubjectBtn.setForeground(Color.WHITE);
        addSubjectBtn.setBackground(new Color(22, 116, 194));
        addSubjectBtn.setFocusPainted(false);
        addSubjectBtn.setBounds(680, 15, 120, 35);
        
        headerPanel.add(titleLabel);
        headerPanel.add(breadcrumbLabel);
        headerPanel.add(addSubjectBtn);
        
        // ----- Table Panel (white background) -----
        tablePanel = new JPanel(null);
        tablePanel.setBackground(Color.WHITE);
        
        currentSubjectsLabel = new JLabel("Current Subjects");
        currentSubjectsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        currentSubjectsLabel.setForeground(new Color(51, 51, 51));
        currentSubjectsLabel.setBounds(20, 20, 200, 25);
        
        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setForeground(new Color(102, 102, 102));
        searchField.setBounds(620, 20, 180, 30);
        searchField.putClientProperty("JTextField.placeholderText", "Search...");
        
        // ----- Table Setup -----
        subjectsTable = new JTable();
        subjectsTable.setRowHeight(30);
        subjectsTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subjectsTable.getTableHeader().setReorderingAllowed(false);
        subjectsTable.getTableHeader().setBackground(new Color(245, 245, 245));
        subjectsTable.getTableHeader().setForeground(Color.BLACK);
        subjectsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        
        dtm = new DefaultTableModel();
        // Columns must appear exactly as defined in your design.
        dtm.setColumnIdentifiers(new String[] { "#", "Subject Name", "Coefficient", "Teacher Name", "Action" });
        subjectsTable.setModel(dtm);
        
        // Set a custom renderer for the "Action" column to show edit and delete icons.
        subjectsTable.getColumnModel().getColumn(4).setCellRenderer(new ActionCellRenderer());
        
        scrollPane = new JScrollPane(subjectsTable);
        scrollPane.setBounds(20, 70, 780, 250);
        
        tablePanel.add(currentSubjectsLabel);
        tablePanel.add(searchField);
        tablePanel.add(scrollPane);
        
        // ----- Assemble Main Panel -----
        this.removeAll();
        setLayout(null);
        headerPanel.setBounds(0, 0, 820, 60);
        add(headerPanel);
        tablePanel.setBounds(0, 70, 820, 340);
        add(tablePanel);
        setPreferredSize(new Dimension(820, 450));
        
        // Load data and set up search filtering.
        loadAllData();
        fillTable("");
        
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String filter = searchField.getText().trim().toLowerCase();
                fillTable(filter);
            }
        });
    }

    /**
     * Load all subjects from the database and store them in allData.
     * Each row is stored as an Object[] with: subjectId, subjectName, coefficient, teacherName.
     */
    private void loadAllData() {
        allData.clear();
        String sql = """
            SELECT s.subject_id,
                   s.name AS subject_name,
                   s.coef AS subject_coef,
                   COALESCE(STRING_AGG(t.name, ', '), '') AS teachers
              FROM subjects s
              LEFT JOIN teacher_subject ts ON s.subject_id = ts.subject_id
              LEFT JOIN teachers t ON ts.teacher_id = t.teacher_id
             GROUP BY s.subject_id, s.name, s.coef
             ORDER BY s.subject_id
        """;
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                int subjectId = rs.getInt("subject_id");
                String subjectName = rs.getString("subject_name");
                int coef = rs.getInt("subject_coef");
                String teachers = rs.getString("teachers");
                if (teachers.isEmpty()) {
                    teachers = "--";
                }
                allData.add(new Object[]{ subjectId, subjectName, coef, teachers });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Fill the table with rows from allData matching the filter.
     * The filter is applied to the subject name and teacher name.
     */
    private void fillTable(String filter) {
        dtm.setRowCount(0);
        int count = 0;
        for (Object[] row : allData) {
            String subjectName = (String) row[1];
            String teacherName = (String) row[3];
            if (filter.isEmpty() ||
                subjectName.toLowerCase().contains(filter) ||
                teacherName.toLowerCase().contains(filter)) {
                count++;
                // The "Action" column is filled with a placeholder string;
                // the custom renderer will replace it with edit and delete icons.
                dtm.addRow(new Object[]{ count, subjectName, row[2], teacherName, "Edit/Delete" });
            }
        }
    }

    /**
     * Custom renderer for the Action column.
     * Displays edit and delete icons side-by-side.
     */
    class ActionCellRenderer extends DefaultTableCellRenderer {
        private final Icon editIcon;
        private final Icon deleteIcon;
        
        public ActionCellRenderer() {
            // Load your icons here – adjust the paths as needed.
            editIcon = new ImageIcon(getClass().getResource("/images/edit.png"));
            deleteIcon = new ImageIcon(getClass().getResource("/images/trash.png"));
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            panel.setOpaque(true);
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(Color.WHITE);
            }
            
            JLabel editLabel = new JLabel(editIcon);
            JLabel deleteLabel = new JLabel(deleteIcon);
            
            panel.add(editLabel);
            panel.add(deleteLabel);
            
            return panel;
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
