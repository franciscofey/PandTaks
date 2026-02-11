package controller;

import model.QuickTask;
import model.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The QuickTaskController class represents the controller for the user's quick tasks in the PandTaskApplication
 * @author Paula Com Morales wkz778
 * @author Shahad K Ali
 * @author Raymond Huelitl
 * @author Francisco Espinoza
 */
public class QuickTaskController {

    private String filePath = "data/quicktasks.csv";
    private int nextId = 1;

    /**
     * Load user's quick tasks from csv file
     * @param user, the current user
     * @return a list of quick tasks (List)
     */
    public List<QuickTask> loadQuickTasks(User user) {
        List<QuickTask> activeTask = new ArrayList<>();
        List<String> linesToKeep = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            try{
                file.getParentFile().mkdirs();
                file.createNewFile();
                try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
                    bw.write("id,userId,title,status\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return activeTask;
        }
        boolean fileNeedsUpdate = false;

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line = br.readLine();
            if (line != null) linesToKeep.add(line);

            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                if(data.length >= 4){
                    int id = Integer.parseInt(data[0].trim());
                    int userId = Integer.parseInt(data[1].trim());
                    String title = data[2].trim();
                    boolean status = Boolean.parseBoolean(data[3].trim());
                    if(id >= nextId) nextId = id + 1;

                    if(userId == user.getId()){
                        if(!status){
                            activeTask.add(new QuickTask(id, userId, title, false));
                            linesToKeep.add(line);
                        } else{
                            fileNeedsUpdate = true;
                        }
                    } else {
                        linesToKeep.add(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(fileNeedsUpdate){
            rewriteFile(linesToKeep);
        }
        return activeTask;
    }

    /**
     * Adds the user's quick task
     * @param user, the current user (User)
     * @param title, the quick task title (String)
     * @return the quick task (QuickTask)
     */
    public QuickTask addQuickTask(User user, String title) {
        QuickTask newTask = new QuickTask(nextId++, user.getId(), title, false);
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))){
            bw.write(String.format("%d,%d,%s,%b\n",
                    newTask.getId(),
                    newTask.getUserId(),
                    newTask.getTitle(),
                    newTask.isStatus()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newTask;
    }

    /**
     * Reads the quicktasks.csv file and set the status to true is the task was completed
     * @param taskId, the task id (int)
     */
    public void markTaskAsCompleted(int taskId){
        List<String> allLines = new ArrayList<>();
        File file = new  File(filePath);

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                if(data.length >= 4){
                    int currentId = -1;
                    try{
                        currentId = Integer.parseInt(data[0].trim());
                    } catch(NumberFormatException e){}
                    if(currentId == taskId){
                        allLines.add(data[0] + "," + data[1] + "," + data[2] + ",true");
                    } else{
                        allLines.add(line);
                    }
                } else{
                    allLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        rewriteFile(allLines);
    }

    /**
     * Rewrites the file
     * @param lines, the contents of the quick task (List)
     */
    private void rewriteFile(List<String> lines){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))){
            for (String line : lines){
                bw.write(line);
                bw.newLine();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
