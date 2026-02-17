package D_DependencyInversion.good;

class NotificationService {
    private MassageSender sender;

    // Constructor
    public NotificationService(MassageSender sender) {
        this.sender = sender;
    }

    // Method لإرسال الإشعار
    public void notify(String message) {
        sender.send(message);
    }
}
