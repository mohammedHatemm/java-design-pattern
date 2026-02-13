package L_LiskovSubstitution.bad;

class Sparrow extends Bird{
    @Override
    public void fly() {
        System.out.println("Sparrow is flying high!");
    }
}