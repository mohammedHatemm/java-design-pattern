package L_LiskovSubstitution.good;

public class Main {
    public static void main(String[] args) {
        // آمن: كل FlyingBird يستطيع الطيران
        FlyingBird sparrow = new Sparrow();
        FlyingBird duck = new Duck();

        makeBirdFly(sparrow);  // يعمل
        makeBirdFly(duck);     // يعمل

        // آمن: كل SwimmingBird يستطيع السباحة
        SwimmingBird penguin = new Penguin();
        makeBirdSwim(penguin); // يعمل
    }

    public static void makeBirdFly(FlyingBird bird) {
        bird.fly();  // مضمون أنه يطير
    }

    public static void makeBirdSwim(SwimmingBird bird) {
        bird.swim(); // مضمون أنه يسبح
    }
}