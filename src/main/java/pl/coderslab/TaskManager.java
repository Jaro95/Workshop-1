package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    static final String FILE_NAME = "tasks.csv";
    static String[][] tasks = new String[0][];
    static final String[] ALL_OPTION = {"add", "remove", "list", "exit"};
    static Scanner inputUser = new Scanner(System.in);
    public static Path path = Paths.get(FILE_NAME);

    public static void main(String[] args) {
        try {
            readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(selectTask()){}

    }

    public static boolean selectTask() {
        boolean check = true;
        System.out.println(ConsoleColors.BLUE + "Please select an option:" + ConsoleColors.RESET);
        for (String option : ALL_OPTION) {
            System.out.println(option);
        }
        switch(inputUser.nextLine()) {
            case "add":
                addTask();
                break;
            case "remove":
                System.out.println("Please select number to remove.");
                removeTask();
                break;
            case "list":
                showTasks();
                break;
            case "exit":
                saveFile();
                System.out.println(ConsoleColors.RED + "Bye. bye");
                check = false;
                break;
            default:
                System.out.println("Please select a correct option.");
        }
        return check;
    }

    public static void readFile()  throws IOException {
        List<String> words = new ArrayList<>();
        if (!Files.exists(path)) {
            System.out.println("File does not exist");
            return;
        }
        for (String line : Files.readAllLines(path)) {
            words.add(line);
        }
        if (words.isEmpty()) {
            return;
        }
        fillArrayTasksFromFile(words);
    }

    public static void addTask() {
        String[] addTask = new String[3];
        System.out.println("Please add task descripton");
        addTask[0] = (inputUser.nextLine());
        System.out.println("Please add task due date");
        addTask[1] = (inputUser.nextLine());
        System.out.println("Is your task is important: true/false");
        addTask[2] = (inputUser.nextLine());
        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = addTask;
    }

    public static void removeTask() {
        try {
            int taskIndex = checkValidateNumber(inputUser.nextLine());
            tasks = ArrayUtils.remove(tasks, taskIndex);
            System.out.println("Value was successfully deleted.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Number is too height");
            removeTask();
        }
    }

    public static void showTasks(){
        for (int i = 0; i < tasks.length; i++) {
            System.out.print(i + " : ");
            System.out.println(String.join(" ", tasks[i]));
        }
    }

    private static void saveFile() {
        List<String> tasksList = new ArrayList<>();
        for (int i = 0 ; i < tasks.length ; i++) {
            tasksList.add(String.join(",", tasks[i]));
        }
        try {
            Files.write(path, tasksList);
        } catch (IOException e) {
            System.out.println("Nie można zapisać pliku");
        }
    }

    public static void fillArrayTasksFromFile(List<String> words) {
        tasks = new String[words.size()][words.get(0).split(",").length];
        for (int i = 0; i < words.size(); i++) {
            String[] task = words.get(i).split(",");
            for (int j = 0; j < task.length; j++) {
                tasks[i][j] = task[j];
            }
        }
    }

    public static int checkValidateNumber(String number) {
        while (!checkConvertOnInt(number) || !isEqualOrGreaterZero(number)) {
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            number = inputUser.nextLine();
        }
        return Integer.parseInt(number);
    }

    public static boolean checkConvertOnInt(String number) {
        return NumberUtils.isParsable(number);
    }

    public static boolean isEqualOrGreaterZero(String number) {
        return Integer.parseInt(number) >= 0;
    }
}

