package O_OpenClosed.good;

class PremiumCustomer implements Customer {

    @Override
    public Double getDiscount(double amount) {
        return amount * 0.2;
    }


}