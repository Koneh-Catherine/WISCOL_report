
package com.mycompany.model;

/**
 *
 * @author ASONGNA FRANK
 */
public class Teacher {
    private final int id;
    private final String name;
    private final String email;
    private final String passwordHash;  // assuming passwords are stored hashed

    public Teacher(int id, String name, String email, String passwordHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
