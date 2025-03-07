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
        setLayout(null);
        
        // Set the background color of the main panel.
        this.setBackground(new Color(237, 241, 247));
        
        // Header
        headerPanel = new SubjectsHeaderPanel();
        headerPanel.setBounds(0, 0, 820, 60);
        add(headerPanel);
        
        // Table Panel (white background)
        tablePanel = new JPanel(null);
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBounds(0, 70, 820, 340);
        add(tablePanel);
        
        currentSubjectsLabel = new JLabel("Current Subjects");
        currentSubjectsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        currentSubjectsLabel.setForeground(new Color(1, 41, 112));
        currentSubjectsLabel.setBounds(20, 20, 200, 25);
        tablePanel.add(currentSubjectsLabel);
        
        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setForeground(new Color(255, 255, 255));
        searchField.setBounds(620, 20, 180, 30);
        searchField.putClientProperty("JTextField.placeholderText", "Search");
        tablePanel.add(searchField);
        
        // Table setup
        subjectsTable = new JTable();
        subjectsTable.setRowHeight(30);
        subjectsTable.setForeground(new Color(125,125,125));
        subjectsTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subjectsTable.getTableHeader().setReorderingAllowed(false);
        subjectsTable.getTableHeader().setBackground(new Color(246, 246, 246));
        subjectsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        
        // Remove all borders and grid lines
        subjectsTable.setShowGrid(false);
        subjectsTable.setIntercellSpacing(new Dimension(0, 0));
        
        dtm = new DefaultTableModel();
        dtm.setColumnIdentifiers(new String[] { "#", "Subject Name", "Coefficient", "Teacher Name", "Action" });
        subjectsTable.setModel(dtm);
        
        // Set the custom renderer for the Action column
        subjectsTable.getColumnModel().getColumn(4).setCellRenderer(new ActionCellRenderer());
        
        scrollPane = new JScrollPane(subjectsTable);
        scrollPane.setBorder(null); // Remove border from scroll pane
        scrollPane.setBounds(20, 70, 780, 250);
        tablePanel.add(scrollPane);
        
        // Fill the table with initial data (no filter)
        fillTable("");
        
        // Wire up search filtering
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String filter = searchField.getText().trim().toLowerCase();
                fillTable(filter);
            }
        });
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
