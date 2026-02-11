package O_OpenClosed.bad;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== BAD Example: Violates Open/Closed Principle ===\n");

        DiscountCalculator calculator = new DiscountCalculator();
        double amount = 100.0;

        System.out.println("Order amount: $" + amount);
        System.out.println("Regular discount: $" + calculator.calcualteDiscount("regular", amount));
        System.out.println("Premium discount: $" + calculator.calcualteDiscount("premium", amount));
        System.out.println("VIP discount: $" + calculator.calcualteDiscount("VIP", amount));

        System.out.println("\n--- Problems ---");
        System.out.println("1. To add new customer type, must MODIFY DiscountCalculator");
        System.out.println("2. Risk of breaking existing functionality");
        System.out.println("3. if-else chain grows endlessly");
        System.out.println("4. Violates OCP: NOT closed for modification");
    }
}
