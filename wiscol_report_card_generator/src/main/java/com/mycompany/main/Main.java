/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.main;

import com.mycompany.event.EventMenuSelected;
import com.mycompany.form.Dashboard;
import com.mycompany.form.Results;
import com.mycompany.form.ReportCards;
import com.mycompany.form.Students;
import com.mycompany.form.Teachers;
import com.mycompany.form.Users;
import com.mycompany.form.Subjects;
import java.awt.Color;
import javax.swing.JComponent;

/**
 *
 * @author DELL
 */
public class Main extends javax.swing.JFrame {

    /**
     * Creates new form Main
     */
    
    private Dashboard dashboard;
    private Results results;
    private ReportCards reportCards;
    private Students students;
    private Teachers teachers;
    private Users users;
    private Subjects subjects;
    
    public Main() {
        initComponents();
        setBackground(new Color(0,0,0,0));
        
        dashboard = new Dashboard();
        results = new Results();
        reportCards = new ReportCards();
        students = new Students();
        teachers = new Teachers();
        users = new Users();
        subjects = new Subjects();
        
        
        menu.initMoving(Main.this); 
        menu.addEventMenuSelected(new EventMenuSelected(){
            @Override
            public void selected(int index) {
                if (index==0) {
                    System.out.println("this is the Dashboard Tab");    
                    setForm(dashboard);
                    
                } else if (index==1) {
                    System.out.println("this is the Results Tab");
                    setForm(results);
                    
                } else if (index==2) {
                    System.out.println("this is the Report Card Tab");
                    setForm(reportCards);
                    
                } else if (index==6) {
                    System.out.println("this is the Students Tab");
                    setForm(students);
                    
                } else if (index==7) {
                    System.out.println("this is the Teacher's Tab");
                    setForm(teachers);
                    
                } else if (index==8) {
                    System.out.println("this is the Users Tab");
                    setForm(users);
                    
                } else if (index==9) {
                    System.out.println("this is the Subjects Tab");
                    setForm(subjects);
                    
                } else if (index==10) {
                    System.out.println("You closed the application");
                    int confirm = javax.swing.JOptionPane.showConfirmDialog(
                        Main.this,
                        "Are you sure you want to logout?",
                        "Confirm Logout",
                        javax.swing.JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
            }
            
        });
        //set the system to open homepage initially
        setForm(new Dashboard());
    }

    private void setForm(JComponent com){
        mainPanel.removeAll();
        mainPanel.add(com);
        mainPanel.revalidate();
        mainPanel.repaint();
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBorder1 = new com.mycompany.swing.PanelBorder();
        menu = new com.mycompany.component.Menu();
        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelBorder1.setBackground(new java.awt.Color(255, 255, 255));

        mainPanel.setBackground(new java.awt.Color(214, 217, 223));
        mainPanel.setForeground(new java.awt.Color(0, 0, 0));
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 956, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainPanel;
    private com.mycompany.component.Menu menu;
    private com.mycompany.swing.PanelBorder panelBorder1;
    // End of variables declaration//GEN-END:variables
}
