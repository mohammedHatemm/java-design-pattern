package S_SingleResponsibility.good;

/**
 * Responsibility: Validation ONLY
 *
 * Only one reason to change: if validation rules change.
 * Example: password must include special characters.
 */
public class UserValidator {

    public boolean validateEmail(User user) {
        return user.getEmail() != null && user.getEmail().contains("@");
    }

    public boolean validatePassword(User user) {
        return user.getPassword() != null && user.getPassword().length() >= 8;
    }

    public boolean validateUser(User user) {
        return validateEmail(user) && validatePassword(user);
    }
}