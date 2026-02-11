package model;

import java.util.ArrayList;

/**
 * The Task class represents the current tasks of the current user.
 * @author Paula Com Morales wkz778
 * @author Shahad K Ali
 * @author Raymond Huelitl
 * @author Francisco Espinoza
 */
public class Task {
    private int id;
    private int userId;
    private String title;
    private String startHour;
    private String endHour;
    private String repeat;
    private String description;
    private String eventDate;
    private String priority;
    private ArrayList<Label> labels;

    /**
     * Task constructor
     * @param id, the task's id (int)
     * @param userId, the user's id (int)
     * @param title, the task's name (String)
     * @param startHour, the task's start hour (String)
     * @param endHour, the task's end hour (String)
     * @param repeat, the task's repetition type (String)
     * @param description, the task's description (String)
     * @param eventDate, the task's date (String)
     * @param priority, the task's priority (String)
     */
    public Task(int id, int userId, String title, String startHour, String endHour,
                String repeat, String description, String eventDate, String priority) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.startHour = startHour;
        this.endHour = endHour;
        this.repeat = repeat;
        this.description = description;
        this.eventDate = eventDate;
        this.priority = priority;
        this.labels = new ArrayList<>();
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public ArrayList<Label> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<Label> labels) {
        this.labels = labels;
    }

    /**
     * String representation of the task
     * @return task attributes (String)
     */
    @Override
    public String toString() {
        return String.format("%s (%s - %s, %s, %s)", title, startHour, endHour, eventDate, priority);
    }
}