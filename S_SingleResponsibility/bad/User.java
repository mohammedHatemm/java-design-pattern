package S_SingleResponsibility.bad;

/**
 * BAD EXAMPLE - Violates Single Responsibility Principle
 *
 * This class has TOO MANY responsibilities:
 * 1. Storing user data
 * 2. Validating user data
 * 3. Saving to database
 * 4. Sending emails
 *
 * If any of these change, we must modify this class.
 */
public class User {
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Responsibility 1: User data (OK)
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Responsibility 2: Validation (WRONG - should be separate class)
    public boolean validateEmail() {
        return email != null && email.contains("@");
    }

    public boolean validatePassword() {
        return password != null && password.length() >= 8;
    }

    // Responsibility 3: Database operations (WRONG - should be separate class)
    public void saveToDatabase() {
        // Database connection logic here
        System.out.println("Connecting to database...");
        System.out.println("Saving user: " + name + " to database");
        // INSERT INTO users VALUES (...)
    }

    public void deleteFromDatabase() {
        System.out.println("Deleting user: " + name + " from database");
    }

    // Responsibility 4: Email operations (WRONG - should be separate class)
    public void sendWelcomeEmail() {
        System.out.println("Connecting to email server...");
        System.out.println("Sending welcome email to: " + email);
    }

    public void sendPasswordResetEmail() {
        System.out.println("Sending password reset email to: " + email);
    }
}
