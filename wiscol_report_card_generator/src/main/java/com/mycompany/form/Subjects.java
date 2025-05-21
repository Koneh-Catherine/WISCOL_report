package com.mycompany.form;

import com.mycompany.dao.SubjectDAO;
import com.mycompany.dao.TeacherDAO;
import com.mycompany.model.Subject;
import com.mycompany.component.SubjectsHeaderPanel;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Subjects extends JPanel {
    private SubjectsHeaderPanel headerPanel;
    private JPanel tablePanel;
    private JLabel currentSubjectsLabel;
    private JTextField searchField;
    private JTable subjectsTable;
    private JScrollPane scrollPane;
    private DefaultTableModel dtm;
    private JButton addButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton refreshButton;

    private List<Subject> allSubjects;
    private final List<String> teacherOptions;
    private final Set<Integer> editedRows = new LinkedHashSet<>();
    private boolean newRowAdded = false;


    public Subjects() {
        teacherOptions = TeacherDAO.getAllTeacherNames();
        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {
        setBackground(new Color(237, 241, 247));
        setLayout(new BorderLayout());

        // Header panel
        headerPanel = new SubjectsHeaderPanel();
        headerPanel.setPreferredSize(new Dimension(820, 60));
       
        // Table panel
        tablePanel = new JPanel(null);
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setPreferredSize(new Dimension(820, 500));

        // Title
        currentSubjectsLabel = new JLabel("Current Subjects");
        currentSubjectsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        currentSubjectsLabel.setForeground(new Color(51, 51, 51));
        currentSubjectsLabel.setBounds(20, 20, 200, 25);
        tablePanel.add(currentSubjectsLabel);

        // Buttons
        addButton = new JButton("Add");
        saveButton = new JButton("Save");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        styleButton(addButton);
        styleButton(saveButton);
        styleButton(deleteButton);
        styleButton(refreshButton);
        saveButton.setEnabled(false);
        saveButton.setBackground(Color.GRAY);
        deleteButton.setEnabled(true);

        int btnY = 20, btnH = 30;
        int startX = 240, gap = 10;
        int w = 80;
        addButton.setBounds(startX, btnY, w, btnH);
        saveButton.setBounds(startX + (w+gap)*1, btnY, w, btnH);
        deleteButton.setBounds(startX + (w+gap)*2, btnY, w, btnH);
        refreshButton.setBounds(startX + (w+gap)*3, btnY, w, btnH);
        tablePanel.add(addButton);
        tablePanel.add(saveButton);
        tablePanel.add(deleteButton);
        tablePanel.add(refreshButton);

        // Search
        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setForeground(new Color(102, 102, 102));
        searchField.setBounds(startX + (w+gap)*4, btnY, 170, btnH);
        searchField.putClientProperty("JTextField.placeholderText", "search...");
        tablePanel.add(searchField);

        // Table
        subjectsTable = new JTable();
        subjectsTable.setRowHeight(30);
        subjectsTable.setForeground(new Color(125, 125, 125));
        subjectsTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subjectsTable.getTableHeader().setReorderingAllowed(false);
        subjectsTable.getTableHeader().setBackground(new Color(245, 245, 245));
        subjectsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        subjectsTable.setShowGrid(false);
        subjectsTable.setIntercellSpacing(new Dimension(0, 0));
        subjectsTable.setFillsViewportHeight(true);
        subjectsTable.setBackground(Color.WHITE);
        subjectsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) c.setBackground(Color.WHITE);
                return c;
            }
        });

        // Model: no checkbox for save, only Select column for delete
        dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 || col >= 2;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return Boolean.class;
                    case 1:
                        return Integer.class; // Enables numeric sorting for the “#” column
                    case 3:
                        return Integer.class; // Coefficient column remains numeric
                    default:
                        return String.class;
                }
            }
        };
        dtm.setColumnIdentifiers(new String[]{"Select", "#", "Subject Name", "Coefficient", "Teacher Name"});
        subjectsTable.setModel(dtm);
        subjectsTable.getColumnModel().getColumn(0).setMaxWidth(60);
        subjectsTable.getColumnModel().getColumn(1).setMaxWidth(50);

        // Sorting
        subjectsTable.setRowSorter(new TableRowSorter<>(dtm));

        // Teacher editor
        JComboBox<String> combo = new JComboBox<>(teacherOptions.toArray(new String[0]));
        subjectsTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(combo));

        // Scroll
        scrollPane = new JScrollPane(subjectsTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBounds(20, 70, 780, 380);
        tablePanel.add(scrollPane);

        // Load data
        loadSubjects();
        fillTable("");

        // Listeners
        addButton.addActionListener(e -> onAddSubject());
        saveButton.addActionListener(e -> onSave());
        deleteButton.addActionListener(e -> onDelete());
        refreshButton.addActionListener(e -> onRefresh());

        // Enable save on any cell edit (excluding select column)
        dtm.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int col = e.getColumn();
                if (col >= 2) {
                    saveButton.setEnabled(true);
                    saveButton.setBackground(new Color(13,110,253));
                    editedRows.add(e.getFirstRow());
                }
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                fillTable(searchField.getText().trim().toLowerCase());
            }
        });

        // Layout
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tablePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(tablePanel);
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(contentPanel);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(13, 110, 253));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    private void loadSubjects() {
        allSubjects = SubjectDAO.getAllSubjects();
    }

    private void fillTable(String filter) {
        dtm.setRowCount(0);
        int count = 0;
        for (Subject s : allSubjects) {
            String tnames = s.getTeacherNames() == null ? "" : s.getTeacherNames();
            if (filter.isEmpty() || s.getName().toLowerCase().contains(filter) || tnames.toLowerCase().contains(filter)) {
                dtm.addRow(new Object[]{false, ++count, s.getName(), s.getCoefficient(), tnames.isEmpty() ? "--" : tnames});
            }
        }
        editedRows.clear();
    }

    private void onAddSubject() {
        dtm.addRow(new Object[]{false, dtm.getRowCount() + 1, "", "", "--"});
        newRowAdded = true;
        saveButton.setEnabled(true);
        saveButton.setBackground(new Color(13,110,253));
    }

    private void onSave() {
        for (int i : editedRows) {
            int m = i;
            String name = dtm.getValueAt(m, 2).toString().trim();
            int coef = Integer.parseInt(dtm.getValueAt(m, 3).toString().trim());
            String teacher = dtm.getValueAt(m, 4).toString();
            Integer tid = "--".equals(teacher)?null:TeacherDAO.getTeacherIdByName(teacher);
            if (m >= allSubjects.size() || newRowAdded && m == dtm.getRowCount()-1) {
                Subject created = SubjectDAO.insertSubject(name, coef, tid);
                allSubjects.add(created);
            } else {
                Subject ex = allSubjects.get(m);
                SubjectDAO.updateSubject(ex.getId(), name, coef, tid);
                ex.setName(name); ex.setCoefficient(coef); ex.setTeacherNames(teacher.equals("--")?null:teacher);
            }
        }
        newRowAdded = false;
        saveButton.setEnabled(false);
        saveButton.setBackground(Color.GRAY);
        fillTable(searchField.getText().trim().toLowerCase());
    }

    private void onDelete() {
        int rowCount = dtm.getRowCount();
        List<Subject> toRemove = new ArrayList<>();
        for (int i = rowCount - 1; i >= 0; i--) {
            Boolean sel = (Boolean) dtm.getValueAt(i, 0);
            if (sel != null && sel) {
                Subject s = allSubjects.get(i);
                SubjectDAO.deleteSubject(s.getId());
                toRemove.add(s);
            }
        }
        allSubjects.removeAll(toRemove);
        saveButton.setEnabled(false);
        saveButton.setBackground(Color.GRAY);
        fillTable(searchField.getText().trim().toLowerCase());
    }

    private void onRefresh() {
        loadSubjects();
        saveButton.setEnabled(false);
        saveButton.setBackground(Color.GRAY);
        fillTable(searchField.getText().trim().toLowerCase());
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
