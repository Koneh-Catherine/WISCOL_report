package com.mycompany.model;

public class Subject {
    private int id;
    private String name;
    private int coefficient;
    private String teacherNames; // comma-separated list if more than one

    public Subject(int id, String name, int coefficient, String teacherNames) {
        this.id = id;
        this.name = name;
        this.coefficient = coefficient;
        this.teacherNames = teacherNames;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public String getTeacherNames() {
        return teacherNames;
    }
}
