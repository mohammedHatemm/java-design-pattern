package O_OpenClosed.good;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== GOOD Example: Follows Open/Closed Principle ===\n");

        DiscountCalculator calculator = new DiscountCalculator();
        double amount = 100.0;

        // Create different customer types
        Customer regular = new RegularCustomer();
        Customer premium = new PremiumCustomer();
        Customer vip = new VIPCustomer();

        System.out.println("Order amount: $" + amount);
        System.out.println("Regular discount: $" + calculator.calculateDiscount(regular, amount));
        System.out.println("Premium discount: $" + calculator.calculateDiscount(premium, amount));
        System.out.println("VIP discount: $" + calculator.calculateDiscount(vip, amount));

        System.out.println("\n--- Benefits ---");
        System.out.println("1. To add new customer type, just CREATE a new class");
        System.out.println("2. No modification to existing code");
        System.out.println("3. No risk of breaking existing functionality");
        System.out.println("4. Follows OCP: Open for extension, Closed for modification");

        System.out.println("\n--- Example: Adding GoldCustomer ---");
        System.out.println("Just create: class GoldCustomer implements Customer { ... }");
        System.out.println("No changes needed to DiscountCalculator!");
    }
}
