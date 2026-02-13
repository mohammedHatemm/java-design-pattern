# O — Open/Closed Principle (OCP)

---

## Definition

> "Software entities should be open for extension, but closed for modification."
> — Bertrand Meyer

In other words, **you can add new functionality without changing existing code that already works.**

---

## What Does "Open and Closed" Mean?

Think of a TV remote:

- **Open for extension**: You can add new buttons (like a Netflix button)
- **Closed for modification**: You don't need to take apart the remote and rewire existing buttons to add new ones

In programming:

- **Open**: You can add new features by creating new classes
- **Closed**: You don't need to modify existing classes that work correctly

---

## Why Is This Principle Important?

| Benefit       | Explanation                                                   |
| ------------- | ------------------------------------------------------------- |
| Reduced Risk  | You're not touching working code, so you won't break anything |
| Easy Addition | Need a new feature? Just add a new class                      |
| Stability     | Existing code stays stable                                    |
| Easy Testing  | New class gets its own test; old tests are unaffected         |
| Scalability   | The project grows easily                                      |

---

## File Structure

```
O_OpenClosed/
├── README.md                      <- This file
├── bad/                           <- Bad example
│   ├── DiscountCalculator.java    <- if-else for each customer type
│   └── Main.java                  <- Running the bad example
└── good/                          <- Good example
    ├── Customer.java              <- Interface
    ├── RegularCustomer.java       <- Regular customer (10%)
    ├── PremiumCustomer.java       <- Premium customer (20%)
    ├── VIPCustomer.java           <- VIP customer (30%)
    ├── DiscountCalculator.java    <- Calculator that never changes!
    └── Main.java                  <- Running the good example
```

---

## Bad Example — Violates OCP

**Path:** `bad/DiscountCalculator.java`

### Scenario:

You have a system that calculates discounts based on customer type.

```java
class DiscountCalculator {

    public double calcualteDiscount(String customerType, double amount) {

        if (customerType.equals("regular")) {
            return amount * 0.1;     // 10%
        } else if (customerType.equals("premium")) {
            return amount * 0.2;     // 20%
        } else if (customerType.equals("VIP")) {
            return amount * 0.3;     // 30%
        }
        return 0;
    }
}
```

### What's the Problem?

```
Want to add a "Gold" customer?
        |
        v
Must open DiscountCalculator and modify it
        |
        v
Add another else-if
        |
        v
Risk of breaking existing code!
        |
        v
Every new type = modify the same class
        |
        v
The if-else chain grows forever!
```

| Problem                    | Explanation                                   |
| -------------------------- | --------------------------------------------- |
| Adding a new customer type | Must modify `DiscountCalculator`              |
| `if-else` chain            | Grows with every new type                     |
| Risk of breakage           | Any modification could affect existing types  |
| Testing difficulty         | Must test all types together                  |
| **OCP Violation**          | **The class is NOT closed for modification!** |

---

## Good Example — Follows OCP

The solution: use **Interface** + **Polymorphism** — each customer type in its own class.

### Step 1: Create an Interface

**`Customer.java`**

```java
interface Customer {
    Double getDiscount(double amount);
}
```

This is the contract: any customer must have a `getDiscount` method.

---

### Step 2: Implement for Each Customer Type

**`RegularCustomer.java`**

```java
class RegularCustomer implements Customer {
    @Override
    public Double getDiscount(double amount) {
        return amount * 0.1;  // 10%
    }
}
```

**`PremiumCustomer.java`**

```java
class PremiumCustomer implements Customer {
    @Override
    public Double getDiscount(double amount) {
        return amount * 0.2;  // 20%
    }
}
```

**`VIPCustomer.java`**

```java
class VIPCustomer implements Customer {
    @Override
    public Double getDiscount(double amount) {
        return amount * 0.3;  // 30%
    }
}
```

---

### Step 3: The Discount Calculator — Never Changes!

**`DiscountCalculator.java`**

```java
class DiscountCalculator {

    public double calculateDiscount(Customer customer, double amount) {
        return customer.getDiscount(amount);
    }
}
```

Notice:

- `DiscountCalculator` takes `Customer` (the Interface), not `String`
- No `if-else` needed
- **It will never change**, no matter how many customer types we add!

---

### Step 4: Usage

**`Main.java`**

```java
public class Main {
    public static void main(String[] args) {
        DiscountCalculator calculator = new DiscountCalculator();
        double amount = 100.0;

        Customer regular = new RegularCustomer();
        Customer premium = new PremiumCustomer();
        Customer vip = new VIPCustomer();

        System.out.println("Regular discount: $" + calculator.calculateDiscount(regular, amount));
        System.out.println("Premium discount: $" + calculator.calculateDiscount(premium, amount));
        System.out.println("VIP discount: $" + calculator.calculateDiscount(vip, amount));
    }
}
```

---

### Step 5: Adding a New Type — Without Modifying Any Existing Code!

Want to add a Gold customer? Just create a new class:

```java
// Just a new file — no modification to any existing class!
class GoldCustomer implements Customer {
    @Override
    public Double getDiscount(double amount) {
        return amount * 0.25;  // 25%
    }
}
```

```
DiscountCalculator <- NOT modified
RegularCustomer    <- NOT modified
PremiumCustomer    <- NOT modified
VIPCustomer        <- NOT modified
GoldCustomer       <- New file only!
```

---

## How Polymorphism Works Here

```
                    +--------------+
                    |   Customer   |  <- Interface
                    | getDiscount()|
                    +------+-------+
           +-----------+---+----------+-----------+
           v           v              v           v
    +----------+ +----------+ +---------+ +----------+
    | Regular  | | Premium  | |   VIP   | |   Gold   |
    |   10%    | |   20%    | |   30%   | |   25%    |
    +----------+ +----------+ +---------+ +----------+
                                           <- New!
                                           No modifications!
```

`DiscountCalculator` works with `Customer` (the Interface).
It doesn't matter what the actual type is — Polymorphism picks the correct `getDiscount()` automatically!

---

## Comparison: Before and After

|                            | Before (Bad)                  | After (Good)            |
| -------------------------- | ----------------------------- | ----------------------- |
| **Adding a customer type** | Modify `DiscountCalculator`   | Create a new class only |
| **Risk of breaking code**  | High                          | Zero                    |
| **if-else**                | Long chain                    | None                    |
| **Testing**                | All types together            | Each type independently |
| **Number of files**        | One large file                | Small, focused files    |
| **When scaling**           | Class gets bigger and complex | Simple new file         |

---

## Golden Rule

```
Want to add a new feature?

    WRONG: Open an existing class and modify it
    RIGHT: Create a new class that implements the Interface

    "Add code... don't modify code!"
```

---

## Practical Tips

1. **If you see `if-else` or `switch` growing** — convert to Interface + implementations
2. **If every new feature requires modifying the same class** — you're violating OCP
3. **The Strategy Pattern** is a direct application of OCP
4. **Not every `if-else` is wrong** — but if the same pattern repeats, use Polymorphism
5. **In Java**: use `interface` or `abstract class` to achieve OCP

---

## Relationship with Other SOLID Principles

- **SRP (S)**: Each class responsible for one thing makes it easier to extend
- **LSP (L)**: New classes must respect the contract (the Interface) to work correctly
- **DIP (D)**: Depending on the Interface, not the Implementation, is what keeps `DiscountCalculator` from changing

---

## How to Run

```bash
# Bad example
cd O_OpenClosed/bad
javac *.java
java -cp .. O_OpenClosed.bad.Main

# Good example
cd O_OpenClosed/good
javac *.java
java -cp .. O_OpenClosed.good.Main
```
