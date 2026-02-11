package model;

/**
 * The Label class represents the labels of the tasks.
 * @author Paula Com Morales wkz778
 * @author Shahad K Ali
 * @author Raymond Huelitl
 * @author Francisco Espinoza
 */
public class Label {
    private int userId;
    private String name;
    private String color;

    /**
     * Label constructor
     * @param userId, the user's id (int)
     * @param name, the label's name (String)
     * @param color, the label's color (String)
     */
    public Label(int userId,String name, String color) {
        setUserId(userId);
        setName(name);
        setColor(color);
    }

    /**
     * Getters and Setters
     */

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    /**
     * String representation of the label
     * @return the label's name (String)
     */
    @Override
    public String toString(){
        return getName();
    }
}