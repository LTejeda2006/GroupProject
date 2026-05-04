package todo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler {
    private String fileName;

    public FileHandler(String fileName) {
        this.fileName = fileName;
    }

    public void saveTasks(ArrayList<Task> tasks) throws IOException {
        File file = new File(fileName);
        File parentFolder = file.getParentFile();

        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        for (Task task : tasks) {
            writer.write(escape(task.getTitle()) + "|" + escape(task.getDescription()) + "|" + task.isCompleted());
            writer.newLine();
        }

        writer.close();
    }

    public ArrayList<Task> loadTasks() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(fileName);

        if (!file.exists()) {
            return tasks;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|", -1);

            if (parts.length == 3) {
                String title = unescape(parts[0]);
                String description = unescape(parts[1]);
                boolean completed = Boolean.parseBoolean(parts[2]);
                tasks.add(new Task(title, description, completed));
            }
        }

        reader.close();
        return tasks;
    }

    private String escape(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("|", "\\p").replace("\n", "\\n");
    }

    private String unescape(String text) {
        StringBuilder result = new StringBuilder();
        boolean slashFound = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (slashFound) {
                if (c == 'p') {
                    result.append('|');
                } else if (c == 'n') {
                    result.append('\n');
                } else {
                    result.append(c);
                }
                slashFound = false;
            } else if (c == '\\') {
                slashFound = true;
            } else {
                result.append(c);
            }
        }

        if (slashFound) {
            result.append('\\');
        }

        return result.toString();
    }
}
