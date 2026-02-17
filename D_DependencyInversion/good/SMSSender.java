package D_DependencyInversion.good;

class SMSSender implements MassageSender {
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}