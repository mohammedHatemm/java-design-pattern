package O_OpenClosed.bad;
class DiscountCalculator {

    public double calcualteDiscount(String customerType , double amount){

        if(customerType.equals("regular")){
            return amount*0.1;
        }else if(customerType.equals("premium")){
            return amount*0.2;
        }
        else if(customerType.equals("VIP")){
            return amount*0.3;
        }
        return  0 ;

    }
}