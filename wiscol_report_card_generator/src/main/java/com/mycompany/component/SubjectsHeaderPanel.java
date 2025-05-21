package com.mycompany.component;

import javax.swing.*;
import java.awt.*;

public class SubjectsHeaderPanel extends JPanel {
    private JLabel titleLabel;
    private JLabel breadcrumbLabel;
    private JButton addSubjectBtn;

    public SubjectsHeaderPanel() {
        setLayout(null);
        setBackground(new Color(240, 240, 240));
        setPreferredSize(new Dimension(820, 60));

        titleLabel = new JLabel("Subjects");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(65, 84, 241));
        titleLabel.setBounds(20, 10, 200, 25);
        add(titleLabel);

        breadcrumbLabel = new JLabel("Manage > Subjects");
        breadcrumbLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        breadcrumbLabel.setForeground(new Color(120, 120, 120));
        breadcrumbLabel.setBounds(20, 35, 200, 20);
        add(breadcrumbLabel);

//        addSubjectBtn = new JButton("Add Subject");
//        addSubjectBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
//        addSubjectBtn.setForeground(Color.WHITE);
//        addSubjectBtn.setBackground(new Color(13, 110, 253));
//        addSubjectBtn.setFocusPainted(false);
//        addSubjectBtn.setBounds(680, 15, 120, 35);
//        add(addSubjectBtn);
    }

    public JButton getAddSubjectButton() {
        return addSubjectBtn;
    }
}
