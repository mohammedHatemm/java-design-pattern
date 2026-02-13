package I_InterfaceSegregation.good;

class  Robot implements Workable ,  Codeable
{
    @Override
    public void work() {
        System.out.println("Robot work");
    }
    @Override
    public void code() {
        System.out.println("Robot code");
    }

}
