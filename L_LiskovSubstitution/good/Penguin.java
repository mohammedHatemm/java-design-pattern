package L_LiskovSubstitution.good;

class Penguin implements SwimmingBird {
    @Override
    public void eat() {
        System.out.println("Penguin is eating fish");
    }

    @Override
    public void sleep() {
        System.out.println("Penguin is sleeping standing");
    }

    @Override
    public void swim() {
        System.out.println("Penguin is swimming fast!");
    }
}
