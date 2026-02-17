package D_DependencyInversion.bad;
class EmailSender {
    public void send(String message){
        System.out.println("Sending Email: " + message);
    }
}