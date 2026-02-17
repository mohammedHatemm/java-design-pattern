package D_DependencyInversion.good;

class EmailSender implements MassageSender{
    public void send(String message){
        System.out.println("📧 Email: " + message);

    }
}