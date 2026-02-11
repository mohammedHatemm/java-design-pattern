package S_SingleResponsibility.good;
public class User {
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Responsibility 1: User data (OK)
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {return password;}

    public String setEmail(String email) {
        this.email = email;
        return this.email;
    }
    public String setPassword(String password) {
        this.password = password;
        return this.password;

    }

    public void setName(String name) {
        this.name = name;

    }
}