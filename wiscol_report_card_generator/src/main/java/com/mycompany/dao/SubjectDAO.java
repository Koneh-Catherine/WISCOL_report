package com.mycompany.dao;

import com.mycompany.main.databaseConnection;
import com.mycompany.model.Subject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    public static List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
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
                int id = rs.getInt("subject_id");
                String name = rs.getString("subject_name");
                int coef = rs.getInt("subject_coef");
                String teachers = rs.getString("teachers");
                if (teachers.isEmpty()) {
                    teachers = "--";
                }
                subjects.add(new Subject(id, name, coef, teachers));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return subjects;
    }
}
