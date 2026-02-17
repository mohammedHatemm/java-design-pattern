package D_DependencyInversion.good;

class WhatsAppSender implements MassageSender{
    public void send(String message){
        System.out.println("WhatsApp: " + message);
    }
}