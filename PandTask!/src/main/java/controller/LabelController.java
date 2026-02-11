package controller;

import model.Label;
import model.User;

import java.io.*;
import java.util.ArrayList;

/**
 * The LabelController class represents the controller for the user's labels in the PandTaskApplication
 * @author Paula Com Morales wkz778
 * @author Shahad K Ali
 * @author Raymond Huelitl
 * @author Francisco Espinoza
 */
public class LabelController {
    private ArrayList<User> users;
    private ArrayList<Label> labels;
    private final String FILE_PATH = "data/labels.csv";

    public LabelController() {
        this.labels = new ArrayList<>();
        this.users = new ArrayList<>();
    }
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Label> getLabels() {
        return labels;
    }

    ///  Load labels from CSV and remember the path for saving later.
    public void loadLabelsFromUser(User currentUser) throws IOException {
        labels.clear();

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            createDefaultLabelsFile(currentUser);
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    try{
                        int userId = Integer.parseInt(data[0].trim());
                        if(userId == currentUser.getId()){
                            String name = data[1].trim();
                            String color = data[2].trim();
                            labels.add(new Label(userId,name, color));
                        }
                    }catch (Exception e){
                        System.out.println("Error parsing label line: " + line);
                    }
                }
            }
            System.out.println("Loaded " + labels.size() + " labels from user " +  currentUser.getUsername());

            if(labels.isEmpty()){
                addDefaultLabelsForUser(currentUser);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Adds the label to csv to the user that is adding the label
     * @param currentUser, the current user (User)
     * @param labelName, the label's name (String)
     * @param color, the label's color (String)
     * @throws IOException, throws exceptions related to file I/O
     */
    public void addLabel(User currentUser, String labelName, String color) throws IOException {
        Label newlabel = new Label(currentUser.getId(),labelName, color);
        labels.add(newlabel);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(String.format("%d,%s,%s\n",
                    newlabel.getUserId(),
                    newlabel.getName(),
                    newlabel.getColor()
            ));
            System.out.println("Label saved to CSV " + labelName);
        } catch (IOException e) {
            System.err.println("Error saving label " + e.getMessage());
        }
    }

    /**
     * Creates a default label to csv file
     * @param currentUser, the current user (User)
     * @throws IOException, throws exceptions related to file I/O
     */
    private void createDefaultLabelsFile(User currentUser) throws IOException {
        try{
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            file.createNewFile();

            try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("userId,name,color\n");
            }
            addDefaultLabelsForUser(currentUser);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add default labels to the current user
     * @param currentUser, the current user (User)
     * @throws IOException, throws exceptions related to file I/O
     */
    private void addDefaultLabelsForUser(User currentUser) throws IOException {
//        Label defaultLabel1 = new Label(currentUser.getId(), "School", "#436ac3");
//        Label defaultLabel2 = new Label(currentUser.getId(),"Work", "#dd3b35");
//        Label defaultLabel3 = new Label(currentUser.getId(), "Birthdays", "#ac52c2");
        String[][] defaults = {
                {"School", "#436ac3"}, {"Work", "#dd3b35"}, {"Birthdays", "#ac52c2"}
        };
//        for(User u : users){
//            if(u.equals(currentUser)){
//                labels.add(defaultLabel1);
//                labels.add(defaultLabel2);
//                labels.add(defaultLabel3);
//            }
//        }
    }
}