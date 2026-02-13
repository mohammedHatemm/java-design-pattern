package I_InterfaceSegregation.bad;

class Programmer implements Worker{

    @Override
    public void work() {
        System.out.println("Programmer is working...");
    }
    @Override
    public void eat() {
        System.out.println("Programmer is eating");
    }
    @Override
    public void sleep() {
        System.out.println("Programmer is sleeping");

    }
    @Override
    public void attendMetting() {
        System.out.println("Programmer is attending metting");
    }
    @Override
    public void writeReport() {
        System.out.println("Programmer is writing report");
    }
    @Override
    public void code() {
        System.out.println("codnig .....");
    }
}