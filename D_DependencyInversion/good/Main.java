package D_DependencyInversion.good;

public class Main {
    public static void main(String[] args) {
        // بالإيميل
        NotificationService emailService = new NotificationService(new EmailSender());
        emailService.notify("Hello via Email!");

        // بالـ SMS - بدون تغيير NotificationService!
        NotificationService smsService = new NotificationService(new SMSSender());
        smsService.notify("Hello via SMS!");

        // بالواتساب - بدون تغيير NotificationService!
        NotificationService whatsappService = new NotificationService(new WhatsAppSender());
        whatsappService.notify("Hello via WhatsApp!");
    }
}