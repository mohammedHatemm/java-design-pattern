# L — Liskov Substitution Principle (LSP)

---

## Definition

> "Objects of a superclass should be replaceable with objects of its subclasses without breaking the program."
> — Barbara Liskov

In other words, **if B inherits from A, you must be able to use B anywhere A is used, and the program should still work correctly.**

---

## What Does "Substitution" Mean?

Think of a TV remote that uses AA batteries:

- If you put in **any brand of AA battery**, the remote works fine
- If you put in an **AAA battery (smaller)**, it won't fit — **it can't substitute**

In programming:

- If you have a method that accepts `Bird`
- **Any class that extends `Bird`** should work in that method
- If `Penguin extends Bird` but `Penguin` can't fly — **the principle is violated!**

---

## Why Is This Principle Important?

| Benefit              | Explanation                                            |
| -------------------- | ------------------------------------------------------ |
| Avoid Surprises      | Code won't crash because a subclass can't do something |
| Correct Polymorphism | Polymorphism works as expected                         |
| Logical Inheritance  | Ensures inheritance actually makes sense               |
| Testable Code        | You can substitute objects in tests safely             |
| Flexible Design      | Code extends safely                                    |

---

## File Structure

```
L_LiskovSubstitution/
├── bad/                           <- Bad example
│   ├── Bird.java                  <- Parent class with fly()
│   ├── Sparrow.java               <- Sparrow that flies (OK)
│   ├── Penguin.java               <- Penguin that flies?! throws Exception!
│   └── Main.java                  <- Program crashes!
└── good/                          <- Good example
    ├── Bird.java                  <- Base interface (eat, sleep)
    ├── FlyingBird.java            <- Interface for flying
    ├── SwimmingBird.java          <- Interface for swimming
    ├── Sparrow.java               <- Sparrow: flies
    ├── Penguin.java               <- Penguin: swims
    ├── Duck.java                  <- Duck: flies + swims
    └── Main.java                  <- Everything works safely
```

---

## Bad Example — Violates LSP

### Scenario:

You have a `Bird` class with a `fly()` method. All birds extend it.

**`Bird.java`**

```java
class Bird {
    public void fly() {
        System.out.println("Flying!");
    }
}
```

**`Sparrow.java`** — A sparrow flies, no problem.

```java
class Sparrow extends Bird {
    @Override
    public void fly() {
        System.out.println("Sparrow is flying high!");
    }
}
```

**`Penguin.java`** — A penguin can't fly!

```java
class Penguin extends Bird {
    @Override
    public void fly() {
        // Penguins can't fly... what do we do here?!
        throw new UnsupportedOperationException("Penguins cannot fly!");
    }
}
```

**`Main.java`** — The program crashes!

```java
public class Main {
    public static void main(String[] args) {
        Bird sparrow = new Sparrow();
        Bird penguin = new Penguin();

        sparrow.fly();   // Works: "Sparrow is flying high!"
        penguin.fly();   // CRASH! UnsupportedOperationException!
    }
}
```

### What Exactly Is the Problem?

```
Bird bird = new Penguin();
bird.fly();    <- CRASH!

The problem:
  Penguin "is a" Bird (by inheritance)
  But Penguin can't fly!
  So Penguin can't substitute for Bird
  = LSP Violation
```

**The dangerous scenario:**

Imagine you have a function like this:

```java
public void makeBirdFly(Bird bird) {
    bird.fly();  // <- If a Penguin is passed, this crashes!
}
```

Anyone calling it with a `Penguin` crashes the program. That's because `Penguin` **cannot substitute** for `Bird`.

### Why Did This Happen?

```
The problem: We assumed "all birds fly"
The reality: Not all birds fly!

Flying is NOT a fundamental property of all birds:
  - Sparrow flies
  - Penguin does NOT fly
  - Ostrich does NOT fly
  - Duck flies

The solution: Separate capabilities into different Interfaces!
```

---

## Good Example — Follows LSP

### The Idea:

Instead of putting `fly()` in the base `Bird` class, **separate capabilities** into different Interfaces.

### Step 1: Base Interface — All Birds

**`Bird.java`**

```java
interface Bird {
    public void eat();
    public void sleep();
}
```

All birds eat and sleep — that's common to all of them.

---

### Step 2: Interfaces for Special Capabilities

**`FlyingBird.java`** — A bird that flies

```java
interface FlyingBird extends Bird {
    public void fly();
}
```

**`SwimmingBird.java`** — A bird that swims

```java
interface SwimmingBird extends Bird {
    public void swim();
}
```

---

### Step 3: Each Bird Implements Only What It Can Do

**`Sparrow.java`** — Sparrow: eats + sleeps + **flies**

```java
class Sparrow implements FlyingBird {
    @Override
    public void eat() {
        System.out.println("Sparrow eating");
    }

    @Override
    public void sleep() {
        System.out.println("Sparrow sleeping");
    }

    @Override
    public void fly() {
        System.out.println("Sparrow flying");
    }
}
```

**`Penguin.java`** — Penguin: eats + sleeps + **swims** (does NOT fly!)

```java
class Penguin implements SwimmingBird {
    @Override
    public void eat() {
        System.out.println("Penguin is eating fish");
    }

    @Override
    public void sleep() {
        System.out.println("Penguin is sleeping standing");
    }

    @Override
    public void swim() {
        System.out.println("Penguin is swimming fast!");
    }
}
```

**`Duck.java`** — Duck: eats + sleeps + **flies** + **swims** (can do both!)

```java
class Duck implements FlyingBird, SwimmingBird {
    @Override
    public void eat() {
        System.out.println("Duck is eating");
    }

    @Override
    public void sleep() {
        System.out.println("Duck is sleeping");
    }

    @Override
    public void fly() {
        System.out.println("Duck is flying");
    }

    @Override
    public void swim() {
        System.out.println("Duck is swimming");
    }
}
```

---

### Step 4: Safe Usage

**`Main.java`**

```java
public class Main {
    public static void main(String[] args) {
        // Safe: every FlyingBird is guaranteed to fly
        FlyingBird sparrow = new Sparrow();
        FlyingBird duck = new Duck();

        makeBirdFly(sparrow);  // Works
        makeBirdFly(duck);     // Works

        // Safe: every SwimmingBird is guaranteed to swim
        SwimmingBird penguin = new Penguin();
        makeBirdSwim(penguin); // Works
    }

    public static void makeBirdFly(FlyingBird bird) {
        bird.fly();   // Guaranteed to fly — because it's a FlyingBird
    }

    public static void makeBirdSwim(SwimmingBird bird) {
        bird.swim();  // Guaranteed to swim — because it's a SwimmingBird
    }
}
```

Notice: no `throw Exception` and no crash!

---

## The Design Difference

### Bad Design:

```
        +----------+
        |   Bird   |
        |  fly()   | <- All birds must fly?!
        +----+-----+
     +-------+-------+
     v               v
+---------+    +----------+
| Sparrow |    | Penguin  |
| fly() OK|    | fly() !! | <- CRASH!
+---------+    +----------+
```

### Good Design:

```
              +----------+
              |   Bird   | <- eat(), sleep() only
              | (base)   |
              +----+-----+
        +----------+----------+
        v                     v
  +-----------+        +--------------+
  |FlyingBird |        |SwimmingBird  |
  |  fly()    |        |   swim()     |
  +-----+-----+        +------+-------+
   +----+----+                |
   v         v                v
+-------+ +------+     +----------+
|Sparrow| | Duck |     | Penguin  |
|fly() OK| |fly()OK|     | swim() OK|
+-------+ |swim()OK|     +----------+
          +------+
```

---

## Comparison: Before and After

|                         | Before (Bad)                          | After (Good)                        |
| ----------------------- | ------------------------------------- | ----------------------------------- |
| **Design**              | Single inheritance for all birds      | Separate interfaces by capability   |
| **Penguin**             | Forced to fly -> Exception            | Implements `SwimmingBird` only      |
| **Safety**              | Can crash at any time                 | Guaranteed and safe                 |
| **Adding a new bird**   | Must think: does it fly or not?       | Implement the appropriate interface |
| **`makeBirdFly(bird)`** | Can crash                             | Guaranteed (FlyingBird only)        |
| **Exceptions**          | `throw UnsupportedOperationException` | No exceptions!                      |

---

## The LSP Test: A Simple Question

Before using inheritance, ask yourself:

```
"Can the child class do EVERYTHING the parent class does?"

    YES -> Inheritance is correct
    NO  -> Don't use inheritance! Use separate Interfaces
```

### Examples:

| Relationship                | Inheritance OK? | Why?                                         |
| --------------------------- | --------------- | -------------------------------------------- |
| `Cat extends Animal`        | YES             | Every cat is an animal — eats, sleeps, moves |
| `Penguin extends Bird(fly)` | NO              | Penguin can't fly                            |
| `Square extends Rectangle`  | NO              | Square doesn't accept width != height        |
| `ElectricCar extends Car`   | MAYBE           | If Car has `fillGas()` -> won't work         |

---

## Golden Rule

```
Inheritance = "is a" relationship

    Sparrow IS A FlyingBird    -> YES (sparrow is a flying bird)
    Penguin IS A SwimmingBird  -> YES (penguin is a swimming bird)
    Penguin IS A FlyingBird    -> NO  (penguin is NOT a flying bird!)

    If "is a" isn't 100% true -> don't use inheritance!
```

---

## Practical Tips

1. **If a subclass has a method that throws `UnsupportedOperationException`** — you're violating LSP
2. **If a subclass has an empty method (empty body)** — a sign that inheritance is wrong
3. **If you need an `instanceof` check before calling a method** — inheritance isn't correct
4. **Use Interfaces instead of class inheritance when in doubt**
5. **The rule: "Before you inherit... ask: can the child do everything the parent does?"**

---

## Relationship with Other SOLID Principles

- **SRP (S)**: Each Interface is responsible for one capability (`FlyingBird` = flying only)
- **OCP (O)**: You can add a new bird (new class) without modifying existing code
- **ISP (I)**: Interfaces are split correctly — no large interface forcing everything
- **DIP (D)**: Depend on `FlyingBird` (Interface), not `Sparrow` (Implementation)

---

## How to Run

```bash
# Bad example (will crash with Exception!)
cd L_LiskovSubstitution/bad
javac *.java
java -cp .. L_LiskovSubstitution.bad.Main

# Good example (everything works safely)
cd L_LiskovSubstitution/good
javac *.java
java -cp .. L_LiskovSubstitution.good.Main
```
