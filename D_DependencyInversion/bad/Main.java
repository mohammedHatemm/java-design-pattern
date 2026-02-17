package D_DependencyInversion.bad;

public class Main {
    public static void main(String[] args) {
        NotificationService service = new NotificationService();
        service.notify("hello!");
    }
}