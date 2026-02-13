package L_LiskovSubstitution.bad;

class Penguin extends Bird {
    @Override
    public void fly() {
        throw new UnsupportedOperationException("Penguins cannot fly!");

    }
}
