package I_InterfaceSegregation.bad;

class Robot implements Worker {

    @Override
    public void work() {
        System.out.println("Robot is working...");
    }
    @Override
    public void eat(){
        throw  new UnsupportedOperationException("robot isnot  eating");
    }
    @Override
    public void sleep(){
        throw  new UnsupportedOperationException("robot isnot  sleeping");
    }
    @Override
    public void attendMeeting(){
        throw  new UnsupportedOperationException("robot isnot  attingMetting");
    }
    @Override
    public void code(){
        System.out.println("Robot code is working...");
    }
}