package controller;

import model.Task;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The AuthController class represents the controller for user authentication in the PandTaskApplication
 * @author Paula Com Morales wkz778
 * @author Shahad K Ali
 * @author Raymond Huelitl
 * @author Francisco Espinoza
 */
public class AuthController {

    /// current ID of User
    private int id;
    private String email;
    private String password;
    private String username;

    /// user current task
    private ArrayList<Task> currentTask = new ArrayList<>();

    /// List that holds all user objects currently loaded into the application
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<AuthController> authUsers = new ArrayList<>();

    /// Define the file path once for use in multiple methods
    private static final String USERS_FILE_PATH = "data/users.csv";

    /// Retrieve the list of all users loaded by the controller
    public ArrayList<AuthController> getAuthUsers() {
        return authUsers;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Loads and reads the user's file
     * @param filename, the user's file (String)
     * @throws IOException, throws exceptions related to file I/O
     */
    public void loadUsersFromCSV(String filename) throws IOException {
        // Use the defined constant path or the passed filename if necessary
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            // Read and discard the header line
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                // Using a regular expression to handle optional white spaces around the comma
                String[] userItems = line.split("\\s*,\\s*");

                // Basic check to ensure we have enough columns
                if (userItems.length < 4) {
                    System.err.println("Skipping malformed user line: " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(userItems[0]);
                    String username = userItems[1];
                    String email = userItems[2];
                    String password = userItems[3];

                    User user = new User(id, username, email, password);
                    users.add(user);
                } catch (NumberFormatException e) {
                    System.err.println("Skipping user with invalid ID: " + line);
                }
            }
        }
        System.out.println("Loaded " + users.size() + " users from CSV");
    }

    /**
     * Checks if the email is already registered. If not, a new unique ID is generated.
     * The new user is created, added to the in-memory list, and saved to the file.
     * @param username, the new user's username (String)
     * @param email, the user's email (String)
     * @param password, the user's password (String)
     * @return the user object created
     * @throws IOException, throws exceptions related to file I/O
     */
    public User singUp(String username, String email, String password) throws IOException {
        // Check if email already exists
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                System.out.println("\nEmail already registered.");
                return null;
            }
        }

        int newId;
        if (users.isEmpty()) {
            // First user gets the starting ID
            newId = 1001;
        } else {
            // Assumes the list is sorted by ID.
            // Get the last user object: users.getLast(users.size() - 1)
            int lastUserId = users.getLast().getId();
            newId = lastUserId + 1;
            id = newId;
        }
        this.email = email;
        this.password = password;
        this.username = username;

        User newUser = new User(newId, username, email, password);
        users.add(newUser); // Add to in-memory list

        // Append to the CSV file
        appendSignUpToCsv(newUser);

        return newUser;
    }

    /**
     * Appends a single new user to the file without reloading or overwriting.
     * @param user, the new user object created (User)
     * @throws IOException, throws exceptions related to file I/O
     */
    private void appendSignUpToCsv(User user) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE_PATH, true))) {
            bw.write( "\n" + convertUserToLine(user)  );
        }
    }

    /**
     * Appends the updated user to the csv file
     * @param updatedUser, the updated user (whenever the user changes the username or password)
     * @throws IOException, throws exceptions related to file I/O
     */
    public void appendUserToCsv(User updatedUser) throws IOException {
        boolean userFound = false;

        StringBuilder sb = new StringBuilder();
        sb.append("id,username,email,password\n");

        // UPDATE THE IN-MEMORY LIST
        // Find the user in the existing list and replace them with the updated object.
        for (int i = 0; i < users.size(); i++) {
            User existingUser = users.get(i);

            if (updatedUser.getId() == existingUser.getId()) {
                users.set(i, updatedUser); // Replace the old User object with the updated one
                userFound = true;
                break; // Stop searching once the user is replaced
            }
        }

        // If the user wasn't found
        if (!userFound) {
            users.add(updatedUser);
        }

        // OVERWRITE THE ENTIRE FILE WITH THE UPDATED LIST
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE_PATH))) {
            bw.write(sb.toString());
            // Write ALL users from the (now updated) list to the file
            int i = 0;
            for (User u : users) {
                if(i != users.size() - 1){
                    String userLine = convertUserToLine(u);
                    bw.write(userLine);
                    bw.newLine(); // cross-platform compatibility
                }
                i++;
            }
        }
    }

    /**
     *  Converts a User object to a CSV line string.
     * @param user, the user (User)
     * @return a string of the user's id, username, email, and password (String)
     */
    public String convertUserToLine(User user) {
        return user.getId() + "," + user.getUsername() + "," + user.getEmail() + "," + user.getPassword();
    }

    /**
     * Authenticates a user based on provided email and password.
     * @param email, the user's email (String)
     * @param password, the user's password (String)
     * @return the authenticated user (User)
     */
    public User login(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                username = user.getUsername();
                this.email = email;
                id = user.getId();
                this.password = password;
                System.out.println("\nLog in successful! Welcome " + user.getUsername());
                return user;
            }
        }
        System.out.println("Invalid email or password.");
        return null;
    }

    /**
     * Logs out a user by printing a message
     * @param user, the user (String)
     * @return a message showing user's logout (String)
     */
    public String logout(User user) {
        if (user != null) {
            String message = "\nGoodbye, " + user.getUsername();
            System.out.println(message); // Keep the print statement for immediate feedback
            return message;             // Return the string
        }
        return "Logout attempted with no active user.";
    }

    /**
     * String representation of the users
     * @return the users (ArrayList)
     */
    @Override
    public String toString(){
        return users.toString();
    }

    /**
     * Getters and setters
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Task> getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(ArrayList<Task> currentTask) {
        this.currentTask = currentTask;
    }
}
