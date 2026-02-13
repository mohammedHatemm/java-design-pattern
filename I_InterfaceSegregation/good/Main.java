package I_InterfaceSegregation.good;

public class Main{

        System.out.println("main");
        Programmer dev = new Programmer();
        Robot bot = new Robot();
        Manager mgr = new Manager();


        makeWork(dev);
        makeWork(bot);
        makeWork(mgr);

        feedWorker(dev);
        feedWorker(mgr);


    }

    public static void makeWork(Workable worker){
        System.out.println("makeWork");
        worker.work();
    }
    public static void feedWorker(Eatable worker){
        System.out.println("feedWork");
        worker.eat();
    }
}