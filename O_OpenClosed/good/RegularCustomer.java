package O_OpenClosed.good;

class RegularCustomer implements Customer
{
    @Override
    public Double getDiscount(double amount) {
        return amount * 0.1;
    }


}