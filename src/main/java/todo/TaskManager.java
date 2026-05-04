package todo;

import java.util.ArrayList;

public class TaskManager {
    private ArrayList<Task> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void updateTask(int index, String title, String description) {
        if (isValidIndex(index)) {
            Task task = tasks.get(index);
            task.setTitle(title);
            task.setDescription(description);
        }
    }

    public void deleteTask(int index) {
        if (isValidIndex(index)) {
            tasks.remove(index);
        }
    }

    public void markTaskComplete(int index) {
        if (isValidIndex(index)) {
            tasks.get(index).setCompleted(true);
        }
    }

    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    public ArrayList<Task> getCompletedTasks() {
        ArrayList<Task> completedTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.isCompleted()) {
                completedTasks.add(task);
            }
        }

        return completedTasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < tasks.size();
    }
}
