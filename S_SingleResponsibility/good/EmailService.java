package S_SingleResponsibility.good;

class EmailService{

    public void sendEmail(User user){
        System.out.println("Connecting to email server...");
        System.out.println("Sending welcome email to: " + user.getEmail());
    }

    public void sendPasswordRestEmail(String email){
        System.out.println("Connecting to email server...");
        System.out.println("Sending password rest email to: " + email);
    }
}