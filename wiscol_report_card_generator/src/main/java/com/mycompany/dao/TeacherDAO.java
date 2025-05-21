
package com.mycompany.dao;

/**
 *
 * @author ASONGNA FRANK
 */

import com.mycompany.main.databaseConnection;
import com.mycompany.model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {
     /**
     * Returns a list of all teacher names, with “--” as the first entry
     * to represent “no teacher assigned.”
     */
    public static List<String> getAllTeacherNames() {
        List<String> names = new ArrayList<>();
        names.add("--");
        String sql = "SELECT name FROM teachers ORDER BY name";
        try (Connection conn = databaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return names;
    }

    /**
     * Looks up a teacher’s ID by exact name match.
     * Returns null if no such teacher exists.
     */
    public static Integer getTeacherIdByName(String name) {
        String sql = "SELECT teacher_id FROM teachers WHERE name = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, name);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("teacher_id");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Looks up a teacher’s name by their ID.
     * Returns null if no such teacher exists.
     */
    public static String getTeacherNameById(int id) {
        String sql = "SELECT name FROM teachers WHERE teacher_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * (Optional) Fetch full Teacher objects if you later need more info.
     */
    public static List<Teacher> getAllTeachers() {
        List<Teacher> list = new ArrayList<>();
        String sql = "SELECT teacher_id, name, email, password FROM teachers";
        try (Connection conn = databaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Teacher(
                    rs.getInt("teacher_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
