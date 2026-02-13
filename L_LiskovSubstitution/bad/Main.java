package L_LiskovSubstitution.bad;

public class Main {

    public static void main(String[] args) {
        Bird sparrow = new Sparrow();
        Bird penguin = new Penguin();
        sparrow.fly();
        penguin.fly();
    }
}