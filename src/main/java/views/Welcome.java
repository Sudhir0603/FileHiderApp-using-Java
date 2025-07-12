package views;

import dao.UserDAO;
import model.User;
import service.GenerateOTP;
import service.SendOTPService;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

public class Welcome {
    private final Scanner scanner = new Scanner(System.in);

    public void welcomeScreen() {
        while (true) {
            printWelcomeMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> login();
                case 2 -> signUp();
                case 0 -> {
                    System.out.println("👋 Thank you for using the app. Goodbye!");
                    return;
                }
                default -> System.out.println("❌ Invalid option. Please try again.");
            }
        }
    }

    private void printWelcomeMenu() {
        System.out.println("\n==============================");
        System.out.println("🎉 Welcome to the File Hider App 🎉");
        System.out.println("==============================");
        System.out.println("🔐 1. Login");
        System.out.println("📝 2. Sign Up");
        System.out.println("🚪 0. Exit");
        System.out.print("👉 Enter your choice: ");
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void login() {
        System.out.print("📧 Enter email: ");
        String email = scanner.nextLine();

        try {
            if (UserDAO.isExists(email)) {
                String generatedOTP = GenerateOTP.getOTP();
                SendOTPService.sendOTP(email, generatedOTP);

                System.out.print("🔑 Enter the OTP sent to your email: ");
                String enteredOTP = scanner.nextLine();

                if (enteredOTP.equals(generatedOTP)) {
                    System.out.println("✅ Login successful!");
                    new UserView(email).home();
                } else {
                    System.out.println("❌ Incorrect OTP. Please try again.");
                }
            } else {
                System.out.println("⚠️ User not found. Please sign up first.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Database error occurred during login.");
            e.printStackTrace();
        }
    }

    private void signUp() {
        System.out.print("👤 Enter name: ");
        String name = scanner.nextLine();
        System.out.print("📧 Enter email: ");
        String email = scanner.nextLine();

        String generatedOTP = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email, generatedOTP);

        System.out.print("🔑 Enter the OTP sent to your email: ");
        String enteredOTP = scanner.nextLine();

        if (enteredOTP.equals(generatedOTP)) {
            User user = new User(name, email);
            int result = UserService.saveUser(user);

            switch (result) {
                case 0 -> System.out.println("🎉 User registered successfully!");
                case 1 -> System.out.println("⚠️ User already exists.");
                default -> System.out.println("❌ Unknown error occurred.");
            }
        } else {
            System.out.println("❌ Incorrect OTP. Please try again.");
        }
    }
}
