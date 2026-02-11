package model;

/**
 * The QuickTask class represents the quick tasks of the user.
 * @author Paula Com Morales wkz778
 * @author Shahad K Ali
 * @author Raymond Huelitl
 * @author Francisco Espinoza
 */
public class QuickTask {
    private int id;
    private int userId;
    private String title;
    private boolean status; //true completed, false = incompleted

    /**
     * QuickTask constructor
     * @param id, the quickTask's id (int)
     * @param userId, the user's id (int)
     * @param title, the quick task title (String)
     * @param status, whether it is completed or not (boolean)
     */
    public QuickTask(int id, int userId, String title, boolean status) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.status = status;
    }

    /**
     * Getters and Setters
     */
    public int getId() {
        return id;
    }
    public int getUserId() {
        return userId;
    }
    public String getTitle() {
        return title;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
}
