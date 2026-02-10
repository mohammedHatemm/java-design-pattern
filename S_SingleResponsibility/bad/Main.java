package S_SingleResponsibility.bad;

/**
 * Demo of the BAD example
 *
 * Problem: User class is doing everything!
 * - If database changes (MySQL to MongoDB) → change User class
 * - If email provider changes → change User class
 * - If validation rules change → change User class
 *
 * This makes the code hard to maintain and test.
 */
public class Main {
    public static void main(String[] args) {
        // Create user
        User user = new User("Sherif", "sherif@example.com", "password123");

        // User class does EVERYTHING - this is bad!

        // Validation
        if (user.validateEmail() && user.validatePassword()) {
            System.out.println("User is valid");
        }

        // Database operation
        user.saveToDatabase();

        // Email operation
        user.sendWelcomeEmail();

        System.out.println("\n--- Problem ---");
        System.out.println("One class has 4 different reasons to change!");
        System.out.println("This violates Single Responsibility Principle.");
    }
}
