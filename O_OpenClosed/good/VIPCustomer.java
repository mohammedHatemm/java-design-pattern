package O_OpenClosed.good;

class VIPCustomer  implements Customer{
    @Override
    public Double getDiscount(double amount) {
        return amount * 0.3;
    }
}