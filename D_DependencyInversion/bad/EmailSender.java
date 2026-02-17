package D_DependencyInversion.bad;
public class EmailSender {
    public void send(String message){
        System.out.println("Sending Email: " + message);
    }
}