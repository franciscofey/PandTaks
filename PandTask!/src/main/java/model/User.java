package model;

import java.util.ArrayList;

/**
 * The User class represents the current user using the application.
 * @author Paula Com Morales wkz778
 * @author Shahad K Ali
 * @author Raymond Huelitl
 * @author Francisco Espinoza
 */
public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private ArrayList<Task> tasks;

    /**
     * User constructor
     * @param id, the user's id (int)
     * @param username, the user's username (String)
     * @param email, the user's email (String)
     * @param password, the user's password (String)
     */
    public User(int id, String username, String email, String password) {
        setId(id);
        setUsername(username);
        setEmail(email);
        setPassword(password);
        this.tasks = new ArrayList<>();
    }

    /**
     * Getters and Setters
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}