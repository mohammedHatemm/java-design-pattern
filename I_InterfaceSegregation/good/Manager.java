package I_InterfaceSegregation.good;

class Manager implements Workable , Eatable  , Sleepable , Meetable , Reportable{
    @Override
    public void work() {
        System.out.println("manager work");

    }
    @Override
    public void sleep() {
        System.out.println("manager sleep");
    }
    @Override
    public void eat() {
        System.out.println("manager eat");
    }
    @Override
    public void meet() {
        System.out.println("manager meet");
    }
   

    @Override
    public void WriteReport() {
    }
}