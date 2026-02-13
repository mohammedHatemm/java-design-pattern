
package L_LiskovSubstitution.good;

class Sparrow implements FlyingBird{
    @Override
    public void eat() {
        System.out.println("Sparrow eating");
    }
    @Override
    public void sleep() {
        System.out.println("Sparrow sleeping");
    }

    @Override
    public void fly() {
        System.out.println("Sparrow flying");
    }

}