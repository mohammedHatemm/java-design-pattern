package O_OpenClosed.good;

class DiscountCalculator {

    public double calculateDiscount(Customer customer, double amount) {
        return customer.getDiscount(amount);
    }
}
