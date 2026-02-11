package  S_SingleResponsibility.good;

class UserRepository {

public void save(User user){
    System.out.println("Connecting to database...");
    System.out.println("Saving user: " + user.getName() + " to database");
}
public boolean delete(User user){
    System.out.println("Deleting user: " + user.getName() + " from database");
    return true;
}
public User findByEmail(String email){
    System.out.println("Connecting to database...");
    return null;
}



}