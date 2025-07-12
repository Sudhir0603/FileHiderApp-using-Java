package views;

import dao.DataDAO;
import model.Data;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserView {
    private final String email;
    private final Scanner scanner;

    public UserView(String email) {
        this.email = email;
        this.scanner = new Scanner(System.in);
    }

    public void home() {
        while (true) {
            printMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> showHiddenFiles();
                case 2 -> hideNewFile();
                case 3 -> unhideFile();
                case 0 -> {
                    System.out.println("\nGoodbye, " + email + "!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.\n");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n===============================");
        System.out.println("Welcome, " + email);
        System.out.println("===============================");
        System.out.println("1. Show hidden files");
        System.out.println("2. Hide a new file");
        System.out.println("3. Unhide a file");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void showHiddenFiles() {
        try {
            List<Data> files = DataDAO.getAllFiles(email);
            if (files.isEmpty()) {
                System.out.println("No hidden files found.");
                return;
            }

            System.out.println("\nHidden Files:");
            System.out.println("ID\tFile Name");
            for (Data file : files) {
                System.out.println(file.getId() + "\t" + file.getFileName());
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving hidden files.");
            e.printStackTrace();
        }
    }

    private void hideNewFile() {
        System.out.print("Enter the full file path to hide: ");
        String path = scanner.nextLine();
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("File does not exist at the given path.");
            return;
        }

        Data data = new Data(0, file.getName(), path, email);
        try {
            DataDAO.hideFile(data);
            System.out.println("File hidden successfully.");
        } catch (SQLException | IOException e) {
            System.out.println("Error hiding the file.");
            e.printStackTrace();
        }
    }

    private void unhideFile() {
        try {
            List<Data> files = DataDAO.getAllFiles(email);
            if (files.isEmpty()) {
                System.out.println("No hidden files found.");
                return;
            }

            System.out.println("\nHidden Files:");
            System.out.println("ID\tFile Name");
            for (Data file : files) {
                System.out.println(file.getId() + "\t" + file.getFileName());
            }

            System.out.print("Enter the ID of the file to unhide: ");
            int id = Integer.parseInt(scanner.nextLine());

            boolean valid = files.stream().anyMatch(file -> file.getId() == id);

            if (valid) {
                DataDAO.unhide(id);
                System.out.println("File unhidden successfully.");
            } else {
                System.out.println("Invalid file ID.");
            }

        } catch (SQLException | IOException e) {
            System.out.println("Error unhiding the file.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }
}
