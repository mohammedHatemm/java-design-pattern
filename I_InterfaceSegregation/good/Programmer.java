package I_InterfaceSegregation.good;

class Programmer  implements Workable , Codeable, Eatable{

    @Override
    public void work() {
        System.out.println("Programmer work");

    }
    @Override
    public void code() {
        System.out.println("Programmer code");
    }

    @Override
    public void eat() {
        System.out.println("Programmer eat");
    }

}