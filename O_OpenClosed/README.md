# Open/Closed Principle (OCP)

> "Software entities should be **open for extension**, but **closed for modification**."

## What does it mean?
- **Open for extension**: You can add new functionality
- **Closed for modification**: Without changing existing code

---

## BAD Example (Violates OCP)

```java
class DiscountCalculator {

    public double calculateDiscount(String customerType, double amount) {
        // Must modify this method for every new customer type!
        if (customerType.equals("Regular")) {
            return amount * 0.1;  // 10%
        } else if (customerType.equals("Premium")) {
            return amount * 0.2;  // 20%
        } else if (customerType.equals("VIP")) {
            return amount * 0.3;  // 30%
        }
        // Adding "Gold" customer? Must MODIFY this class!
        // Adding "Student" customer? Must MODIFY this class!
        return 0;
    }
}
```

### Problems:
- Every new customer type requires modifying `DiscountCalculator`
- Risk of breaking existing functionality
- if-else chain grows endlessly
- NOT closed for modification

---

## GOOD Example (Follows OCP)

### Step 1: Create an interface
```java
interface Customer {
    double getDiscount(double amount);
}
```

### Step 2: Implement for each customer type
```java
class RegularCustomer implements Customer {
    public double getDiscount(double amount) {
        return amount * 0.1;  // 10%
    }
}

class PremiumCustomer implements Customer {
    public double getDiscount(double amount) {
        return amount * 0.2;  // 20%
    }
}

class VIPCustomer implements Customer {
    public double getDiscount(double amount) {
        return amount * 0.3;  // 30%
    }
}
```

### Step 3: Calculator uses the interface
```java
class DiscountCalculator {

    public double calculateDiscount(Customer customer, double amount) {
        return customer.getDiscount(amount);
    }
}
```

### Adding new customer type (NO modification needed!)
```java
// Just create a new class - existing code stays unchanged!
class GoldCustomer implements Customer {
    public double getDiscount(double amount) {
        return amount * 0.25;  // 25%
    }
}
```

### Benefits:
- **Open**: Add new customer types by creating new classes
- **Closed**: Never modify `DiscountCalculator` or existing customers
- No risk of breaking existing functionality
- Clean, maintainable code

---

## Key Takeaway

| Approach | Adding New Feature |
|----------|-------------------|
| BAD | Modify existing class |
| GOOD | Create new class (extend) |

Use **interfaces/abstract classes** to achieve OCP!
