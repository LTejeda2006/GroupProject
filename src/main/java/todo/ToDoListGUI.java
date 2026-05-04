package todo;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;

public class ToDoListGUI extends JFrame {
    private TaskManager taskManager;
    private FileHandler fileHandler;

    private JTextField titleField;
    private JTextField descriptionField;
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;

    public ToDoListGUI() {
        taskManager = new TaskManager();
        fileHandler = new FileHandler("data/tasks.txt");

        loadTasksFromFile();
        setupWindow();
        displayAllTasks();
    }

    private void setupWindow() {
        setTitle("ToDo List");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleField = new JTextField();
        descriptionField = new JTextField();

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(new JLabel("Task Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.addListSelectionListener(event -> fillFieldsFromSelectedTask());

        JScrollPane scrollPane = new JScrollPane(taskList);

        JButton addButton = new JButton("Add Task");
        JButton updateButton = new JButton("Update Task");
        JButton completeButton = new JButton("Mark Complete");
        JButton deleteButton = new JButton("Delete Task");
        JButton showAllButton = new JButton("Show All Tasks");
        JButton showCompletedButton = new JButton("Show Completed Tasks");
        JButton saveButton = new JButton("Save Tasks");

        addButton.addActionListener(event -> addTask());
        updateButton.addActionListener(event -> updateTask());
        completeButton.addActionListener(event -> markComplete());
        deleteButton.addActionListener(event -> deleteTask());
        showAllButton.addActionListener(event -> displayAllTasks());
        showCompletedButton.addActionListener(event -> displayCompletedTasks());
        saveButton.addActionListener(event -> saveTasksToFile());

        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(showAllButton);
        buttonPanel.add(showCompletedButton);
        buttonPanel.add(saveButton);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addTask() {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a task title.");
            return;
        }

        Task task = new Task(title, description);
        taskManager.addTask(task);
        displayAllTasks();
        clearFields();
    }

    private void updateTask() {
        int index = taskList.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task to update.");
            return;
        }

        Task selectedTask = taskListModel.get(index);
        int realIndex = taskManager.getAllTasks().indexOf(selectedTask);

        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a task title.");
            return;
        }

        taskManager.updateTask(realIndex, title, description);
        displayAllTasks();
        clearFields();
    }

    private void deleteTask() {
        int index = taskList.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task to delete.");
            return;
        }

        Task selectedTask = taskListModel.get(index);
        int realIndex = taskManager.getAllTasks().indexOf(selectedTask);

        taskManager.deleteTask(realIndex);
        displayAllTasks();
        clearFields();
    }

    private void markComplete() {
        int index = taskList.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task to mark complete.");
            return;
        }

        Task selectedTask = taskListModel.get(index);
        int realIndex = taskManager.getAllTasks().indexOf(selectedTask);

        taskManager.markTaskComplete(realIndex);
        displayAllTasks();
        clearFields();
    }

    private void displayAllTasks() {
        taskListModel.clear();

        for (Task task : taskManager.getAllTasks()) {
            taskListModel.addElement(task);
        }
    }

    private void displayCompletedTasks() {
        taskListModel.clear();

        ArrayList<Task> completedTasks = taskManager.getCompletedTasks();

        for (Task task : completedTasks) {
            taskListModel.addElement(task);
        }
    }

    private void saveTasksToFile() {
        try {
            fileHandler.saveTasks(taskManager.getAllTasks());
            JOptionPane.showMessageDialog(this, "Tasks saved successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "There was a problem saving the tasks.");
        }
    }

    private void loadTasksFromFile() {
        try {
            taskManager.setTasks(fileHandler.loadTasks());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "There was a problem loading the tasks.");
        }
    }

    private void fillFieldsFromSelectedTask() {
        if (taskList.getSelectedIndex() == -1) {
            return;
        }

        Task selectedTask = taskList.getSelectedValue();

        if (selectedTask != null) {
            titleField.setText(selectedTask.getTitle());
            descriptionField.setText(selectedTask.getDescription());
        }
    }

    private void clearFields() {
        titleField.setText("");
        descriptionField.setText("");
        taskList.clearSelection();
    }
}
