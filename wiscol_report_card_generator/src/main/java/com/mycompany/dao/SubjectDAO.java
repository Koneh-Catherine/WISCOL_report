package com.mycompany.dao;

import com.mycompany.main.databaseConnection;
import com.mycompany.model.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    /**
     * Fetches all subjects with aggregated teacher names.
     */
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
                    teachers = null;
                }
                subjects.add(new Subject(id, name, coef, teachers));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return subjects;
    }

    /**
     * Inserts a new subject and optional teacher relation.
     */
    public static Subject insertSubject(String name, int coef, Integer teacherId) {
        String insertSql = "INSERT INTO subjects(name, coef) VALUES (?, ?) RETURNING subject_id";
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            PreparedStatement pst = conn.prepareStatement(insertSql);
            pst.setString(1, name);
            pst.setInt(2, coef);
            ResultSet rs = pst.executeQuery();
            rs.next();
            int newId = rs.getInt("subject_id");

            // optional teacher assignment
            if (teacherId != null) {
                String relSql = "INSERT INTO teacher_subject(teacher_id, subject_id) VALUES (?, ?)";
                try (PreparedStatement pst2 = conn.prepareStatement(relSql)) {
                    pst2.setInt(1, teacherId);
                    pst2.setInt(2, newId);
                    pst2.executeUpdate();
                }
            }
            // fetch aggregated teacher name
            String teacherNames = (teacherId == null)
                    ? null
                    : TeacherDAO.getTeacherNameById(teacherId);
            return new Subject(newId, name, coef, teacherNames);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        }
    }

    /**
     * Updates an existing subject’s details and teacher relation.
     */
    public static boolean updateSubject(int subjectId, String name, int coef, Integer teacherId) {
        String updateSql = "UPDATE subjects SET name = ?, coef = ? WHERE subject_id = ?";
        String deleteRel = "DELETE FROM teacher_subject WHERE subject_id = ?";
        String insertRel = "INSERT INTO teacher_subject(teacher_id, subject_id) VALUES (?, ?)";
        try (Connection conn = databaseConnection.connect()) {
            conn.setAutoCommit(false);

            // 1) update core subject
            try (PreparedStatement pst = conn.prepareStatement(updateSql)) {
                pst.setString(1, name);
                pst.setInt(2, coef);
                pst.setInt(3, subjectId);
                pst.executeUpdate();
            }

            // 2) clear existing relations
            try (PreparedStatement pst = conn.prepareStatement(deleteRel)) {
                pst.setInt(1, subjectId);
                pst.executeUpdate();
            }

            // 3) insert new relation if provided
            if (teacherId != null) {
                try (PreparedStatement pst = conn.prepareStatement(insertRel)) {
                    pst.setInt(1, teacherId);
                    pst.setInt(2, subjectId);
                    pst.executeUpdate();
                }
            }
            conn.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    
    /**
    * Deletes a subject (and cascades to teacher_subject via FK ON DELETE CASCADE).
    * @return true if the delete succeeded
    */
    public static boolean deleteSubject(int subjectId) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, subjectId);
            return pst.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
