# S — Single Responsibility Principle (SRP)

---

## Definition

> "A class should have one, and only one, reason to change."
> — Robert C. Martin (Uncle Bob)

In other words, every class should do **one job only**. If you need to change a class, there should be only one reason for that change.

---

## What Does "One Reason to Change" Mean?

Imagine you're an employee:

- If you have **one responsibility** (e.g., programming only), your manager will change your work for one reason only: a change in programming requirements.
- If you have **many responsibilities** (programming + design + marketing), any of those areas changing will affect you.

A class works the same way:

- If it does one thing, it changes for one reason.
- If it does many things, it changes for many reasons — **that's the problem.**

---

## Why Is This Principle Important?

| Benefit              | Explanation                                                                   |
| -------------------- | ----------------------------------------------------------------------------- |
| Easy Maintenance     | When a class is responsible for one thing, it's easy to understand and modify |
| Easy Testing         | You can test each class independently                                         |
| Reduced Side Effects | A change in one place won't break something in another                        |
| Reusability          | Small, focused classes can be reused elsewhere                                |
| Team Collaboration   | Each developer can work on a separate class without conflicts                 |

---

## File Structure

```
S_SingleResponsibility/
├── bad/                          <- Bad example
│   ├── User.java                 <- One class doing everything!
│   └── Main.java                 <- Running the bad example
└── good/                         <- Good example
    ├── User.java                 <- User data only
    ├── UserValidator.java        <- Validation only
    ├── UserRepository.java       <- Database only
    ├── EmailService.java         <- Email only
    └── Main.java                 <- Running the good example
```

---

## Bad Example — Violates SRP

**Path:** `bad/User.java`

```java
public class User {
    private String name;
    private String email;
    private String password;

    // Responsibility 1: User data (OK)
    public User(String name, String email, String password) { ... }
    public String getName() { ... }
    public String getEmail() { ... }

    // Responsibility 2: Validation (WRONG — should be a separate class)
    public boolean validateEmail() {
        return email != null && email.contains("@");
    }
    public boolean validatePassword() {
        return password != null && password.length() >= 8;
    }

    // Responsibility 3: Database operations (WRONG — should be a separate class)
    public void saveToDatabase() {
        System.out.println("Connecting to database...");
        System.out.println("Saving user: " + name + " to database");
    }
    public void deleteFromDatabase() { ... }

    // Responsibility 4: Email operations (WRONG — should be a separate class)
    public void sendWelcomeEmail() {
        System.out.println("Connecting to email server...");
        System.out.println("Sending welcome email to: " + email);
    }
    public void sendPasswordResetEmail() { ... }
}
```

### What's the Problem?

The `User` class has **4 reasons to change**:

```
                    +-------------+
                    |    User     |
                    | (one class) |
                    +------+------+
          +--------+-------+--------+---------+
          v        v       v        v
        Data   Validation  DB     Email       = 4 reasons to change!
```

| Scenario                                                     | Result                                                             |
| ------------------------------------------------------------ | ------------------------------------------------------------------ |
| Validation rules change (e.g., password needs special chars) | Must modify `User` class                                           |
| Database changes (MySQL to MongoDB)                          | Must modify `User` class                                           |
| Email provider changes (Gmail to SendGrid)                   | Must modify `User` class                                           |
| **Result**                                                   | **One class changes for many reasons = fragile, hard to maintain** |

---

## Good Example — Follows SRP

The solution: **split the big class into small classes, each responsible for one thing.**

### 1. `User.java` — User Data Only

```java
public class User {
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Only getters and setters — no validation, no DB, no email
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}
```

**Only reason to change:** User data structure changes (e.g., adding a new field).

---

### 2. `UserValidator.java` — Validation Only

```java
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
```

**Only reason to change:** Validation rules change (e.g., password must include uppercase).

---

### 3. `UserRepository.java` — Database Only

```java
class UserRepository {

    public void save(User user) {
        System.out.println("Connecting to database...");
        System.out.println("Saving user: " + user.getName() + " to database");
    }

    public boolean delete(User user) {
        System.out.println("Deleting user: " + user.getName() + " from database");
        return true;
    }

    public User findByEmail(String email) {
        System.out.println("Connecting to database...");
        return null;
    }
}
```

**Only reason to change:** Database technology changes (e.g., MySQL to MongoDB).

---

### 4. `EmailService.java` — Email Only

```java
class EmailService {

    public void sendEmail(User user) {
        System.out.println("Connecting to email server...");
        System.out.println("Sending welcome email to: " + user.getEmail());
    }

    public void sendPasswordRestEmail(String email) {
        System.out.println("Connecting to email server...");
        System.out.println("Sending password rest email to: " + email);
    }
}
```

**Only reason to change:** Email provider changes (e.g., Gmail to SendGrid).

---

### 5. `Main.java` — Usage

```java
public class Main {
    public static void main(String[] args) {
        // 1. Create user (data only)
        User user = new User("Sherif", "sherif@example.com", "password123");

        // 2. Validate (separate class)
        UserValidator validator = new UserValidator();
        if (validator.validateUser(user)) {
            System.out.println("User is valid");
        } else {
            System.out.println("User is NOT valid");
            return;
        }

        // 3. Save to database (separate class)
        UserRepository repository = new UserRepository();
        repository.save(user);

        // 4. Send email (separate class)
        EmailService emailService = new EmailService();
        emailService.sendEmail(user);
    }
}
```

---

### Result After Splitting:

```
    +---------+   +---------------+   +----------------+   +--------------+
    |  User   |   | UserValidator |   | UserRepository |   | EmailService |
    |  data   |   |  validation   |   |    database    |   |    email     |
    +----+----+   +------+--------+   +-------+--------+   +------+-------+
         |               |                    |                    |
    one reason       one reason          one reason           one reason
    to change        to change           to change            to change
```

---

## Comparison: Before and After

|                       | Before (Bad)                  | After (Good)                     |
| --------------------- | ----------------------------- | -------------------------------- |
| **Number of classes** | 1                             | 4                                |
| **Reasons to change** | 4 reasons in one class        | 1 reason per class               |
| **Testing**           | Hard (everything coupled)     | Easy (each class independently)  |
| **Modification**      | Risky (might break something) | Safe (isolated)                  |
| **Readability**       | Hard (large class)            | Easy (small, focused classes)    |
| **Reusability**       | Not possible                  | Possible (EmailService anywhere) |

---

## Golden Rule

```
Before writing a method in a class, ask yourself:

    "Is this method the responsibility of THIS class?"

If the answer is no -> create a new class!
```

---

## Practical Tips

1. **If the class name is too generic** (like `Manager`, `Handler`, `Processor`) — it's likely doing too many things
2. **If the class is over 200 lines** — consider splitting it
3. **If you use the word "and" when describing the class** (e.g., "this class validates AND saves AND sends") — each "and" = a new class
4. **If changing one thing breaks multiple tests** — the class has too many responsibilities

---

## Relationship with Other SOLID Principles

- **Open/Closed (O)**: When each class is responsible for one thing, it's easier to extend without modification
- **Interface Segregation (I)**: Same idea — interfaces should be small and focused
- **Dependency Inversion (D)**: Small classes are easier to inject as dependencies

---

## How to Run

```bash
# Bad example
cd S_SingleResponsibility/bad
javac *.java
java -cp .. S_SingleResponsibility.bad.Main

# Good example
cd S_SingleResponsibility/good
javac *.java
java -cp .. S_SingleResponsibility.good.Main
```
