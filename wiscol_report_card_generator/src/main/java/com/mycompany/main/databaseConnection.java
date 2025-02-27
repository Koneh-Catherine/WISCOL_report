
package com.mycompany.main;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;

public class databaseConnection {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
            return null;
        }
    }

    public static void setupDatabase() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            if (conn == null) {
                System.out.println("❌ Database connection is null. Exiting setup.");
                return;
            }

            System.out.println("✅ Connected to PostgreSQL. Setting up database...");

            // 1️⃣ Create Users Table (Admins & Staff)
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(255) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "role VARCHAR(50) NOT NULL CHECK(role IN ('admin', 'staff'))" +
                    ");");

            // 2️⃣ Create Teachers Table
            stmt.execute("CREATE TABLE IF NOT EXISTS teachers (" +
                    "teacher_id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(255) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL" +
                    ");");

            // 3️⃣ Create Subjects Table
            stmt.execute("CREATE TABLE IF NOT EXISTS subjects (" +
                    "subject_id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255) UNIQUE NOT NULL, " +
                    "coef INT NOT NULL" +
                    ");");

            // 4️⃣ Create Teacher-Subject Relationship Table (Many-to-Many)
            stmt.execute("CREATE TABLE IF NOT EXISTS teacher_subject (" +
                    "teacher_id INT REFERENCES teachers(teacher_id) ON DELETE CASCADE, " +
                    "subject_id INT REFERENCES subjects(subject_id) ON DELETE CASCADE, " +
                    "PRIMARY KEY (teacher_id, subject_id)" +
                    ");");

            // 5️⃣ Create Students Table
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "student_id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "date_of_birth DATE NOT NULL, " +
                    "class_level VARCHAR(50) NOT NULL, " +
                    "academic_year VARCHAR(9) NOT NULL" +
                    ");");

            // 6️⃣ Create Marks Table (No Teacher)
            stmt.execute("CREATE TABLE IF NOT EXISTS marks (" +
                    "mark_id SERIAL PRIMARY KEY, " +
                    "student_id INT REFERENCES students(student_id) ON DELETE CASCADE, " +
                    "subject_id INT REFERENCES subjects(subject_id) ON DELETE CASCADE, " +
                    "seq1 FLOAT DEFAULT 0, seq2 FLOAT DEFAULT 0, seq3 FLOAT DEFAULT 0, " +
                    "seq4 FLOAT DEFAULT 0, seq5 FLOAT DEFAULT 0, seq6 FLOAT DEFAULT 0, " +
                    "avg_20 FLOAT DEFAULT 0, total FLOAT DEFAULT 0, grade VARCHAR(5), " +
                    "remark TEXT, term VARCHAR(20) NOT NULL, academic_year VARCHAR(9) NOT NULL" +
                    ");");

            // 7️⃣ Create Report Cards Table
            stmt.execute("CREATE TABLE IF NOT EXISTS report_cards (" +
                    "report_id SERIAL PRIMARY KEY, " +
                    "student_id INT REFERENCES students(student_id) ON DELETE CASCADE, " +
                    "class_level VARCHAR(50) NOT NULL, " +
                    "term VARCHAR(20) NOT NULL, " +
                    "academic_year VARCHAR(9) NOT NULL, " +
                    "generated_by INT REFERENCES users(user_id) ON DELETE SET NULL, " +
                    "file_path VARCHAR(255) NOT NULL" +
                    ");");

            System.out.println("✅ Database schema created successfully!");

            // Insert Sample Data
            stmt.executeUpdate("INSERT INTO users (name, email, password, role) VALUES " +
                    "('Admin User', 'admin@example.com', 'admin_pass', 'admin'), " +
                    "('Staff User', 'staff@example.com', 'staff_pass', 'staff') ON CONFLICT (email) DO NOTHING;");

            stmt.executeUpdate("INSERT INTO teachers (name, email, password) VALUES " +
                    "('John Doe', 'johndoe@example.com', 'hashed_password'), " +
                    "('Jane Smith', 'janesmith@example.com', 'hashed_password') ON CONFLICT (email) DO NOTHING;");

            stmt.executeUpdate("INSERT INTO subjects (name, coef) VALUES " +
                    "('Mathematics', 5), ('English', 4), ('Science', 3) ON CONFLICT (name) DO NOTHING;");

            stmt.executeUpdate("INSERT INTO teacher_subject (teacher_id, subject_id) VALUES " +
                    "(1, 1), (2, 2), (1, 3) ON CONFLICT DO NOTHING;");

            stmt.executeUpdate("INSERT INTO students (name, date_of_birth, class_level, academic_year) VALUES " +
                    "('Alice Johnson', '2008-05-14', 'Form 1', '2024-2025') ON CONFLICT DO NOTHING;");

            stmt.executeUpdate("INSERT INTO marks (student_id, subject_id, seq1, seq2, seq3, term, academic_year) VALUES " +
                    "(1, 1, 15, 18, 20, 'First', '2024-2025'), " +
                    "(1, 2, 12, 14, 13, 'First', '2024-2025') ON CONFLICT DO NOTHING;");

            System.out.println("✅ Sample data inserted successfully!");

            // Retrieve and display data
            ResultSet rs = stmt.executeQuery("SELECT students.name, subjects.name AS subject, marks.seq1, marks.seq2, marks.seq3, " +
                    "STRING_AGG(teachers.name, ', ') AS teachers " +
                    "FROM marks " +
                    "JOIN students ON marks.student_id = students.student_id " +
                    "JOIN subjects ON marks.subject_id = subjects.subject_id " +
                    "LEFT JOIN teacher_subject ON teacher_subject.subject_id = subjects.subject_id " +
                    "LEFT JOIN teachers ON teacher_subject.teacher_id = teachers.teacher_id " +
                    "GROUP BY students.name, subjects.name, marks.seq1, marks.seq2, marks.seq3;");

            System.out.println("\n📌 Student Marks:");
            while (rs.next()) {
                System.out.println("Student: " + rs.getString("name") + 
                        " | Subject: " + rs.getString("subject") +
                        " | Seq1: " + rs.getFloat("seq1") + 
                        " | Seq2: " + rs.getFloat("seq2") + 
                        " | Seq3: " + rs.getFloat("seq3") + 
                        " | Teachers: " + rs.getString("teachers"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        setupDatabase();
    }
}
