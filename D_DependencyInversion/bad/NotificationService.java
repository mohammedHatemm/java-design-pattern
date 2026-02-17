package D_DependencyInversion.bad;

public class NotificationService{
    private EmailSender sender = new EmailSender();
    public void notify(String message){
        sender.send(message);
    }
}