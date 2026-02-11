package S_SingleResponsibility.good;

public class Main{
    public static void main(String[] args){

        // 1. Create user (data only)
        User user = new User("Sherif", "sherif@example.com", "password123");

        // 2. Validate (separate class)
        UserValidator validator = new UserValidator();
        if (validator.validateUser(user)) {
            System.out.println("✅ User is valid");
        } else {
            System.out.println("❌ User is NOT valid");
            return;
        }

        // 3. Save to database (separate class)
        UserRepository repository = new UserRepository();
        repository.save(user);

        // 4. Send email (separate class)
        EmailService emailService = new EmailService();
        emailService.sendEmail(user);

        System.out.println("\n--- Benefits ---");
        System.out.println("Each class has ONLY ONE reason to change!");
        System.out.println("User           → data structure changes");
        System.out.println("UserValidator   → validation rules change");
        System.out.println("UserRepository  → database changes");
        System.out.println("EmailService    → email provider changes");
        System.out.println("\nThis follows the Single Responsibility Principle ✅");

    }
}