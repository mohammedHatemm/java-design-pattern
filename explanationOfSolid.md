# مبادئ SOLID في البرمجة الكائنية

## المقدمة

مبادئ SOLID هي خمسة مبادئ أساسية في البرمجة الكائنية (OOP) تساعدك على كتابة كود نظيف، قابل للصيانة، وسهل التوسع.

**SOLID** هو اختصار لـ:
- **S** - Single Responsibility Principle (مبدأ المسؤولية الواحدة)
- **O** - Open/Closed Principle (مبدأ الفتح/الإغلاق)
- **L** - Liskov Substitution Principle (مبدأ استبدال ليسكوف)
- **I** - Interface Segregation Principle (مبدأ فصل الواجهات)
- **D** - Dependency Inversion Principle (مبدأ عكس التبعية)

---

## هيكل المشروع (Directory Structure)

```
src/
├── explanationOfSolid.md          # هذا الملف
├── S_SingleResponsibility/
│   ├── bad/                       # مثال سيء
│   └── good/                      # مثال صحيح
├── O_OpenClosed/
│   ├── bad/
│   └── good/
├── L_LiskovSubstitution/
│   ├── bad/
│   └── good/
├── I_InterfaceSegregation/
│   ├── bad/
│   └── good/
└── D_DependencyInversion/
    ├── bad/
    └── good/
```

---

# 1️⃣ S - Single Responsibility Principle
# مبدأ المسؤولية الواحدة

## التعريف

> "كل كلاس يجب أن يكون له سبب واحد فقط للتغيير"

بمعنى آخر: كل كلاس يجب أن يقوم بمهمة واحدة فقط.

## لماذا هذا المبدأ مهم؟

1. **سهولة الصيانة**: عندما يكون الكلاس مسؤول عن شيء واحد، يسهل فهمه وتعديله
2. **سهولة الاختبار**: يمكنك اختبار كل وظيفة بشكل منفصل
3. **تقليل التأثير الجانبي**: تغيير في مكان لن يؤثر على أماكن أخرى
4. **إعادة الاستخدام**: يمكنك استخدام الكلاس في أماكن مختلفة

---

## المثال السيء (Violates SRP)

**المسار:** `S_SingleResponsibility/bad/`

```java
// BAD: هذا الكلاس يفعل كل شيء!
class User {
    private String name;
    private String email;
    private String password;

    // 1. بيانات المستخدم
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // 2. التحقق من البيانات - مسؤولية منفصلة!
    public boolean validateEmail() {
        return email.contains("@");
    }

    public boolean validatePassword() {
        return password.length() >= 8;
    }

    // 3. حفظ في قاعدة البيانات - مسؤولية منفصلة!
    public void saveToDatabase() {
        System.out.println("Connecting to database...");
        System.out.println("Saving user: " + name);
    }

    // 4. إرسال إيميل - مسؤولية منفصلة!
    public void sendWelcomeEmail() {
        System.out.println("Connecting to email server...");
        System.out.println("Sending email to: " + email);
    }
}
```

### المشاكل:

| المشكلة | التوضيح |
|---------|---------|
| تغيير طريقة التحقق | يجب تعديل كلاس User |
| تغيير قاعدة البيانات | يجب تعديل كلاس User |
| تغيير مزود الإيميل | يجب تعديل كلاس User |
| **النتيجة** | كلاس واحد يتغير لأسباب كثيرة! |

---

## المثال الصحيح (Follows SRP)

**المسار:** `S_SingleResponsibility/good/`

### 1. كلاس User - للبيانات فقط

```java
// GOOD: كلاس للبيانات فقط
class User {
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters فقط
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
```

### 2. كلاس UserValidator - للتحقق فقط

```java
// GOOD: كلاس للتحقق فقط
class UserValidator {

    public boolean validateEmail(User user) {
        return user.getEmail().contains("@");
    }

    public boolean validatePassword(User user) {
        return user.getPassword().length() >= 8;
    }

    public boolean validateUser(User user) {
        return validateEmail(user) && validatePassword(user);
    }
}
```

### 3. كلاس UserRepository - لقاعدة البيانات فقط

```java
// GOOD: كلاس لقاعدة البيانات فقط
class UserRepository {

    public void save(User user) {
        System.out.println("Connecting to database...");
        System.out.println("Saving user: " + user.getName());
    }

    public User findByEmail(String email) {
        System.out.println("Searching for: " + email);
        return null;
    }

    public boolean delete(User user) {
        System.out.println("Deleting user: " + user.getName());
        return true;
    }
}
```

### 4. كلاس EmailService - للإيميل فقط

```java
// GOOD: كلاس للإيميل فقط
class EmailService {

    public void sendEmail(User user) {
        System.out.println("Connecting to email server...");
        System.out.println("Sending welcome email to: " + user.getEmail());
    }

    public void sendPasswordResetEmail(String email) {
        System.out.println("Sending password reset to: " + email);
    }
}
```

### 5. Main - الاستخدام

```java
public class Main {
    public static void main(String[] args) {
        // 1. إنشاء المستخدم
        User user = new User("Ahmed", "ahmed@example.com", "password123");

        // 2. التحقق
        UserValidator validator = new UserValidator();
        if (validator.validateUser(user)) {
            System.out.println("المستخدم صالح");
        }

        // 3. الحفظ
        UserRepository repository = new UserRepository();
        repository.save(user);

        // 4. إرسال الإيميل
        EmailService emailService = new EmailService();
        emailService.sendEmail(user);
    }
}
```

### الفوائد:

| الكلاس | سبب التغيير الوحيد |
|--------|-------------------|
| User | تغيير هيكل البيانات |
| UserValidator | تغيير قواعد التحقق |
| UserRepository | تغيير قاعدة البيانات |
| EmailService | تغيير مزود الإيميل |

---

# 2️⃣ O - Open/Closed Principle
# مبدأ الفتح/الإغلاق

## التعريف

> "الكود يجب أن يكون **مفتوح للتوسع** و **مغلق للتعديل**"

بمعنى آخر: يمكنك إضافة وظائف جديدة بدون تعديل الكود الموجود.

## لماذا هذا المبدأ مهم؟

1. **تقليل المخاطر**: لا تعدل كود يعمل بشكل صحيح
2. **سهولة الإضافة**: أضف ميزات جديدة بسهولة
3. **الاستقرار**: الكود القديم يبقى مستقر

---

## المثال السيء (Violates OCP)

**المسار:** `O_OpenClosed/bad/`

```java
// BAD: يجب تعديل هذا الكلاس لكل نوع عميل جديد
class DiscountCalculator {

    public double calculateDiscount(String customerType, double amount) {

        if (customerType.equals("regular")) {
            return amount * 0.1;  // 10%
        }
        else if (customerType.equals("premium")) {
            return amount * 0.2;  // 20%
        }
        else if (customerType.equals("VIP")) {
            return amount * 0.3;  // 30%
        }
        // إضافة عميل Gold؟ يجب تعديل هذا الكلاس!
        // إضافة عميل Student؟ يجب تعديل هذا الكلاس!
        return 0;
    }
}
```

### المشاكل:

```
إضافة نوع عميل جديد = تعديل DiscountCalculator
                     |
              خطر كسر الكود الموجود
                     |
              سلسلة if-else تكبر للأبد
```

---

## المثال الصحيح (Follows OCP)

**المسار:** `O_OpenClosed/good/`

### 1. إنشاء Interface

```java
// GOOD: واجهة للعملاء
interface Customer {
    double getDiscount(double amount);
}
```

### 2. تنفيذ لكل نوع عميل

```java
// عميل عادي
class RegularCustomer implements Customer {
    @Override
    public double getDiscount(double amount) {
        return amount * 0.1;  // 10%
    }
}

// عميل مميز
class PremiumCustomer implements Customer {
    @Override
    public double getDiscount(double amount) {
        return amount * 0.2;  // 20%
    }
}

// عميل VIP
class VIPCustomer implements Customer {
    @Override
    public double getDiscount(double amount) {
        return amount * 0.3;  // 30%
    }
}
```

### 3. حاسبة الخصم - لا تتغير أبدا!

```java
// GOOD: هذا الكلاس لن يتغير أبدا
class DiscountCalculator {

    public double calculateDiscount(Customer customer, double amount) {
        return customer.getDiscount(amount);
    }
}
```

### 4. إضافة نوع جديد - بدون تعديل!

```java
// إضافة عميل Gold - فقط كلاس جديد!
class GoldCustomer implements Customer {
    @Override
    public double getDiscount(double amount) {
        return amount * 0.25;  // 25%
    }
}

// إضافة عميل Student - فقط كلاس جديد!
class StudentCustomer implements Customer {
    @Override
    public double getDiscount(double amount) {
        return amount * 0.15;  // 15%
    }
}
```

### 5. Main - الاستخدام

```java
public class Main {
    public static void main(String[] args) {
        DiscountCalculator calculator = new DiscountCalculator();
        double amount = 100.0;

        Customer regular = new RegularCustomer();
        Customer premium = new PremiumCustomer();
        Customer vip = new VIPCustomer();

        System.out.println("Regular: $" + calculator.calculateDiscount(regular, amount));
        System.out.println("Premium: $" + calculator.calculateDiscount(premium, amount));
        System.out.println("VIP: $" + calculator.calculateDiscount(vip, amount));
    }
}
```

### المقارنة:

| الطريقة | إضافة ميزة جديدة |
|---------|-----------------|
| السيئة | تعديل الكلاس الموجود |
| الصحيحة | إنشاء كلاس جديد (توسع) |

---

# 3️⃣ L - Liskov Substitution Principle
# مبدأ استبدال ليسكوف

## التعريف

> "الكلاس الابن يجب أن يكون قابل للاستبدال مكان الكلاس الأب بدون كسر البرنامج"

بمعنى آخر: إذا كان B يرث من A، يجب أن تستطيع استخدام B في أي مكان يُستخدم فيه A.

## لماذا هذا المبدأ مهم؟

1. **الوراثة الصحيحة**: تأكد أن الوراثة منطقية
2. **تجنب المفاجآت**: الكود لن يتوقف فجأة
3. **Polymorphism صحيح**: تعدد الأشكال يعمل بشكل سليم

---

## المثال السيء (Violates LSP)

**المسار:** `L_LiskovSubstitution/bad/`

```java
// الكلاس الأب
class Bird {
    public void fly() {
        System.out.println("Bird is flying...");
    }
}

// العصفور يطير - لا مشكلة
class Sparrow extends Bird {
    @Override
    public void fly() {
        System.out.println("Sparrow is flying high!");
    }
}

// البطريق لا يطير - مشكلة!
class Penguin extends Bird {
    @Override
    public void fly() {
        // ماذا نفعل هنا؟!
        throw new UnsupportedOperationException("Penguins cannot fly!");
    }
}
```

### المشكلة في الاستخدام:

```java
public class Main {
    public static void main(String[] args) {
        Bird sparrow = new Sparrow();
        Bird penguin = new Penguin();

        sparrow.fly();  // يعمل

        penguin.fly();  // يرمي Exception!
        // البرنامج ينهار!
    }
}

// دالة تتوقع أي طائر
public void makeBirdFly(Bird bird) {
    bird.fly();  // قد ينهار إذا كان البطريق!
}
```

### لماذا هذا خطأ؟

```
Bird bird = new Penguin();
bird.fly();  // CRASH!

المشكلة: Penguin هو Bird لكن لا يستطيع الطيران
         لذلك لا يمكن استبداله مكان Bird
         = انتهاك LSP
```

---

## المثال الصحيح (Follows LSP)

**المسار:** `L_LiskovSubstitution/good/`

### الحل: فصل القدرات

```java
// واجهة أساسية لكل الطيور
interface Bird {
    void eat();
    void sleep();
}

// واجهة منفصلة للطيران
interface FlyingBird extends Bird {
    void fly();
}

// واجهة منفصلة للسباحة
interface SwimmingBird extends Bird {
    void swim();
}
```

### تنفيذ الطيور:

```java
// العصفور: يأكل، ينام، يطير
class Sparrow implements FlyingBird {
    @Override
    public void eat() {
        System.out.println("Sparrow is eating seeds");
    }

    @Override
    public void sleep() {
        System.out.println("Sparrow is sleeping in nest");
    }

    @Override
    public void fly() {
        System.out.println("Sparrow is flying high!");
    }
}

// البطريق: يأكل، ينام، يسبح (لا يطير!)
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

// البطة: يأكل، ينام، يطير، يسبح
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

### الاستخدام الآمن:

```java
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
```

### القاعدة الذهبية:

```
قبل الوراثة، اسأل نفسك:
"هل الكلاس الابن يستطيع فعل كل شيء يفعله الأب؟"

إذا لا --> لا تستخدم الوراثة!
       --> استخدم Interfaces منفصلة
```

---

# 4️⃣ I - Interface Segregation Principle
# مبدأ فصل الواجهات

## التعريف

> "لا تجبر الكلاس على تنفيذ methods لا يحتاجها"

بمعنى آخر: قسم الواجهات الكبيرة إلى واجهات صغيرة ومتخصصة.

## لماذا هذا المبدأ مهم؟

1. **المرونة**: كل كلاس ينفذ ما يحتاجه فقط
2. **تجنب الكود الفارغ**: لا methods فارغة
3. **سهولة الفهم**: واجهات صغيرة أسهل في الفهم

---

## المثال السيء (Violates ISP)

**المسار:** `I_InterfaceSegregation/bad/`

```java
// BAD: واجهة ضخمة تجبر الكل على تنفيذ كل شيء
interface Worker {
    void work();
    void eat();
    void sleep();
    void attendMeeting();
    void writeReport();
    void code();
}
```

### تنفيذ مشكل:

```java
// المبرمج: يحتاج كل الدوال
class Programmer implements Worker {
    @Override
    public void work() { System.out.println("Programming..."); }

    @Override
    public void eat() { System.out.println("Eating..."); }

    @Override
    public void sleep() { System.out.println("Sleeping..."); }

    @Override
    public void attendMeeting() { System.out.println("In meeting..."); }

    @Override
    public void writeReport() { System.out.println("Writing report..."); }

    @Override
    public void code() { System.out.println("Coding..."); }
}

// الروبوت: لا يأكل ولا ينام!
class Robot implements Worker {
    @Override
    public void work() { System.out.println("Robot working..."); }

    @Override
    public void eat() {
        // الروبوت لا يأكل! ماذا نكتب هنا؟
        throw new UnsupportedOperationException("Robots don't eat!");
    }

    @Override
    public void sleep() {
        // الروبوت لا ينام!
        throw new UnsupportedOperationException("Robots don't sleep!");
    }

    @Override
    public void attendMeeting() {
        // الروبوت لا يحضر اجتماعات!
    }

    @Override
    public void writeReport() {
        // فارغة!
    }

    @Override
    public void code() {
        System.out.println("Robot coding...");
    }
}
```

### المشاكل:

```
واجهة Worker ضخمة جدا
        |
Robot مجبر على تنفيذ eat() و sleep()
        |
methods فارغة أو ترمي exceptions
        |
كود قبيح وخطير!
```

---

## المثال الصحيح (Follows ISP)

**المسار:** `I_InterfaceSegregation/good/`

### قسم الواجهة الكبيرة:

```java
// واجهات صغيرة ومتخصصة

interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

interface Sleepable {
    void sleep();
}

interface Codeable {
    void code();
}

interface Meetable {
    void attendMeeting();
}

interface Reportable {
    void writeReport();
}
```

### تنفيذ نظيف:

```java
// المبرمج: ينفذ ما يحتاجه
class Programmer implements Workable, Eatable, Sleepable, Codeable, Meetable {
    @Override
    public void work() { System.out.println("Programming..."); }

    @Override
    public void eat() { System.out.println("Eating lunch..."); }

    @Override
    public void sleep() { System.out.println("Sleeping..."); }

    @Override
    public void code() { System.out.println("Writing code..."); }

    @Override
    public void attendMeeting() { System.out.println("In standup..."); }
}

// الروبوت: ينفذ ما يستطيع فقط
class Robot implements Workable, Codeable {
    @Override
    public void work() { System.out.println("Robot working 24/7..."); }

    @Override
    public void code() { System.out.println("Robot writing code..."); }

    // لا يحتاج eat() أو sleep() - لا مشكلة!
}

// المدير: ينفذ ما يحتاجه
class Manager implements Workable, Eatable, Sleepable, Meetable, Reportable {
    @Override
    public void work() { System.out.println("Managing team..."); }

    @Override
    public void eat() { System.out.println("Business lunch..."); }

    @Override
    public void sleep() { System.out.println("Sleeping..."); }

    @Override
    public void attendMeeting() { System.out.println("Leading meeting..."); }

    @Override
    public void writeReport() { System.out.println("Writing status report..."); }
}
```

### الاستخدام:

```java
public class Main {
    public static void main(String[] args) {
        Programmer dev = new Programmer();
        Robot bot = new Robot();
        Manager mgr = new Manager();

        // كل واحد يعمل
        makeWork(dev);
        makeWork(bot);
        makeWork(mgr);

        // فقط من يأكل
        feedWorker(dev);
        feedWorker(mgr);
        // feedWorker(bot);  // Compile error - الروبوت ليس Eatable
    }

    public static void makeWork(Workable worker) {
        worker.work();
    }

    public static void feedWorker(Eatable worker) {
        worker.eat();
    }
}
```

### القاعدة:

```
واجهة كبيرة = مشكلة
        |
قسمها إلى واجهات صغيرة
        |
كل كلاس ينفذ ما يحتاجه فقط
```

---

# 5️⃣ D - Dependency Inversion Principle
# مبدأ عكس التبعية

---

## 📚 المستوى الأول: الأساسيات

### ما هو مبدأ عكس التبعية؟

مبدأ عكس التبعية (DIP) هو آخر مبدأ في SOLID، وضعه **Robert C. Martin (Uncle Bob)** وينص على قاعدتين أساسيتين:

> **القاعدة الأولى:** "الموديولات عالية المستوى (High-level modules) لا يجب أن تعتمد على موديولات منخفضة المستوى (Low-level modules). كلاهما يجب أن يعتمد على التجريدات (Abstractions)"

> **القاعدة الثانية:** "التجريدات (Abstractions) لا يجب أن تعتمد على التفاصيل (Details). التفاصيل يجب أن تعتمد على التجريدات"

### لماذا سمي "عكس" التبعية؟

```
الطريقة التقليدية (بدون DIP):
┌─────────────────┐
│   High Level    │  (مثل: UserService)
│    Module       │
└────────┬────────┘
         │ يعتمد على (depends on)
         ▼
┌─────────────────┐
│   Low Level     │  (مثل: MySQLDatabase)
│    Module       │
└─────────────────┘

الطريقة الصحيحة (مع DIP):
┌─────────────────┐
│   High Level    │
│    Module       │
└────────┬────────┘
         │ يعتمد على
         ▼
┌─────────────────┐
│   Abstraction   │  (مثل: Database Interface)
│   (Interface)   │
└────────▲────────┘
         │ ينفذ (implements)
         │
┌─────────────────┐
│   Low Level     │
│    Module       │
└─────────────────┘

لاحظ: اتجاه التبعية انعكس! Low Level أصبح يعتمد على Abstraction
```

---

### ما معنى High-Level و Low-Level؟

| النوع | التعريف | أمثلة |
|-------|---------|-------|
| **High-Level** | الكلاسات التي تحتوي على Business Logic | `UserService`, `OrderProcessor`, `PaymentHandler` |
| **Low-Level** | الكلاسات التي تتعامل مع التفاصيل التقنية | `MySQLDatabase`, `EmailSender`, `FileWriter` |

```
High-Level = "ماذا نفعل" (What)
Low-Level  = "كيف نفعله" (How)

مثال:
- UserService يعرف أنه يحتاج "حفظ مستخدم" (What)
- MySQLDatabase يعرف "كيف يحفظ في MySQL" (How)
```

---

### ما هو الـ Abstraction (التجريد)؟

التجريد هو **عقد** (Contract) يحدد "ماذا" بدون "كيف":

```java
// هذا Abstraction - يحدد "ماذا" فقط
interface Database {
    void save(String data);    // ماذا: احفظ البيانات
    String read(int id);       // ماذا: اقرأ البيانات
    // لا يحدد كيف!
}

// هذا التفصيل (Detail) - يحدد "كيف"
class MySQLDatabase implements Database {
    @Override
    public void save(String data) {
        // كيف: استخدم MySQL connector
        // كيف: افتح اتصال
        // كيف: نفذ INSERT query
    }
}
```

**أنواع الـ Abstraction في Java:**
1. **Interface** - الأفضل والأكثر استخداماً
2. **Abstract Class** - عندما تحتاج كود مشترك
3. **Base Class** - أقل تفضيلاً

---

## 📚 المستوى الثاني: لماذا نحتاج DIP؟

### المشكلة بدون DIP

**المسار:** `D_DependencyInversion/bad/`

```java
// ❌ BAD: كلاس منخفض المستوى
class MySQLDatabase {
    public void save(String data) {
        System.out.println("Saving to MySQL: " + data);
    }

    public String read(int id) {
        return "Data from MySQL";
    }
}

// ❌ BAD: كلاس عالي المستوى يعتمد مباشرة على التفاصيل
class UserService {
    // 🔴 المشكلة هنا: اعتماد مباشر على كلاس محدد
    private MySQLDatabase database = new MySQLDatabase();

    public void createUser(String name) {
        database.save(name);
    }

    public String getUser(int id) {
        return database.read(id);
    }
}
```

### لماذا هذا خطأ؟

```
┌─────────────────────────────────────────────────────────────┐
│                    5 مشاكل رئيسية                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│ 1️⃣ Tight Coupling (ارتباط قوي)                             │
│    UserService مربوط بـ MySQLDatabase بشكل صلب              │
│                                                             │
│ 2️⃣ صعوبة التغيير                                           │
│    تغيير DB = تعديل كل الكلاسات التي تستخدمه               │
│                                                             │
│ 3️⃣ صعوبة الاختبار (Testing)                                │
│    لا يمكن اختبار UserService بدون MySQL حقيقي             │
│                                                             │
│ 4️⃣ انتهاك Open/Closed Principle                            │
│    إضافة DB جديد = تعديل الكود الموجود                     │
│                                                             │
│ 5️⃣ صعوبة إعادة الاستخدام                                   │
│    UserService لا يمكن استخدامه مع DB آخر                  │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### سيناريو واقعي للمشكلة

```java
// الآن: تطبيقك يستخدم MySQL
class UserService {
    private MySQLDatabase database = new MySQLDatabase();
}

class OrderService {
    private MySQLDatabase database = new MySQLDatabase();
}

class ProductService {
    private MySQLDatabase database = new MySQLDatabase();
}

// بعد 6 شهور: الشركة قررت الانتقال لـ PostgreSQL
// 😱 يجب تعديل كل الكلاسات!

class UserService {
    private PostgreSQLDatabase database = new PostgreSQLDatabase(); // تعديل!
}

class OrderService {
    private PostgreSQLDatabase database = new PostgreSQLDatabase(); // تعديل!
}

class ProductService {
    private PostgreSQLDatabase database = new PostgreSQLDatabase(); // تعديل!
}

// وإذا كان عندك 50 service؟ 😭
```

---

## 📚 المستوى الثالث: الحل الصحيح

**المسار:** `D_DependencyInversion/good/`

### الخطوة 1: إنشاء Abstraction

```java
// ✅ GOOD: واجهة مجردة - العقد
interface Database {
    void save(String data);
    String read(int id);
    void delete(int id);
    void update(int id, String data);
}
```

### الخطوة 2: إنشاء التنفيذات المختلفة

```java
// تنفيذ MySQL
class MySQLDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println("MySQL: INSERT INTO users VALUES('" + data + "')");
    }

    @Override
    public String read(int id) {
        System.out.println("MySQL: SELECT * FROM users WHERE id = " + id);
        return "User from MySQL";
    }

    @Override
    public void delete(int id) {
        System.out.println("MySQL: DELETE FROM users WHERE id = " + id);
    }

    @Override
    public void update(int id, String data) {
        System.out.println("MySQL: UPDATE users SET name = '" + data + "' WHERE id = " + id);
    }
}

// تنفيذ PostgreSQL
class PostgreSQLDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println("PostgreSQL: INSERT INTO users VALUES('" + data + "')");
    }

    @Override
    public String read(int id) {
        System.out.println("PostgreSQL: SELECT * FROM users WHERE id = " + id);
        return "User from PostgreSQL";
    }

    @Override
    public void delete(int id) {
        System.out.println("PostgreSQL: DELETE FROM users WHERE id = " + id);
    }

    @Override
    public void update(int id, String data) {
        System.out.println("PostgreSQL: UPDATE users SET name = '" + data + "' WHERE id = " + id);
    }
}

// تنفيذ MongoDB (NoSQL)
class MongoDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println("MongoDB: db.users.insertOne({name: '" + data + "'})");
    }

    @Override
    public String read(int id) {
        System.out.println("MongoDB: db.users.findOne({_id: " + id + "})");
        return "User from MongoDB";
    }

    @Override
    public void delete(int id) {
        System.out.println("MongoDB: db.users.deleteOne({_id: " + id + "})");
    }

    @Override
    public void update(int id, String data) {
        System.out.println("MongoDB: db.users.updateOne({_id: " + id + "}, {$set: {name: '" + data + "'}})");
    }
}

// تنفيذ للاختبار - Mock
class MockDatabase implements Database {
    private List<String> savedData = new ArrayList<>();

    @Override
    public void save(String data) {
        savedData.add(data);
        System.out.println("[MOCK] Saved: " + data);
    }

    @Override
    public String read(int id) {
        System.out.println("[MOCK] Read id: " + id);
        return id < savedData.size() ? savedData.get(id) : "Not found";
    }

    @Override
    public void delete(int id) {
        System.out.println("[MOCK] Deleted id: " + id);
    }

    @Override
    public void update(int id, String data) {
        System.out.println("[MOCK] Updated id " + id + " with: " + data);
    }

    // للاختبار: التحقق من البيانات المحفوظة
    public List<String> getSavedData() {
        return savedData;
    }
}
```

### الخطوة 3: High-Level يعتمد على Abstraction

```java
// ✅ GOOD: يعتمد على Interface وليس كلاس محدد
class UserService {
    private final Database database;  // 🟢 Interface!

    // Dependency Injection عبر Constructor
    public UserService(Database database) {
        this.database = database;
    }

    public void createUser(String name) {
        // Validation logic
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        database.save(name);
    }

    public String getUser(int id) {
        return database.read(id);
    }

    public void deleteUser(int id) {
        database.delete(id);
    }

    public void updateUser(int id, String name) {
        database.update(id, name);
    }
}
```

### الخطوة 4: الاستخدام المرن

```java
public class Main {
    public static void main(String[] args) {
        // ============ Production: استخدام MySQL ============
        Database mysqlDb = new MySQLDatabase();
        UserService mysqlService = new UserService(mysqlDb);
        
        System.out.println("=== MySQL ===");
        mysqlService.createUser("Ahmed");
        mysqlService.getUser(1);

        // ============ Production: تغيير لـ PostgreSQL ============
        // 🟢 لم نغير UserService على الإطلاق!
        Database postgresDb = new PostgreSQLDatabase();
        UserService postgresService = new UserService(postgresDb);
        
        System.out.println("\n=== PostgreSQL ===");
        postgresService.createUser("Mohamed");
        postgresService.getUser(1);

        // ============ Production: تغيير لـ MongoDB ============
        // 🟢 لم نغير UserService على الإطلاق!
        Database mongoDb = new MongoDatabase();
        UserService mongoService = new UserService(mongoDb);
        
        System.out.println("\n=== MongoDB ===");
        mongoService.createUser("Ali");
        mongoService.getUser(1);

        // ============ Testing: استخدام Mock ============
        System.out.println("\n=== Testing with Mock ===");
        MockDatabase mockDb = new MockDatabase();
        UserService testService = new UserService(mockDb);
        
        testService.createUser("Test User 1");
        testService.createUser("Test User 2");
        
        // التحقق من البيانات للاختبار
        System.out.println("Saved data: " + mockDb.getSavedData());
    }
}
```

---

## 📚 المستوى الرابع: Dependency Injection بالتفصيل

### ما هو Dependency Injection (DI)؟

Dependency Injection هو **تقنية** لتطبيق DIP، حيث يتم "حقن" التبعيات من الخارج بدلاً من إنشائها داخل الكلاس.

```
بدون DI:
class UserService {
    private Database db = new MySQLDatabase(); // ❌ ينشئها بنفسه
}

مع DI:
class UserService {
    private Database db;
    public UserService(Database db) { // ✅ تُحقن من الخارج
        this.db = db;
    }
}
```

### أنواع Dependency Injection

#### 1️⃣ Constructor Injection (الأفضل ✅)

```java
class UserService {
    private final Database database;
    private final EmailService emailService;
    private final Logger logger;

    // كل التبعيات تُحقن عبر الـ Constructor
    public UserService(Database database, EmailService emailService, Logger logger) {
        this.database = database;
        this.emailService = emailService;
        this.logger = logger;
    }
}

// الاستخدام:
Database db = new MySQLDatabase();
EmailService email = new SMTPEmailService();
Logger logger = new FileLogger();

UserService service = new UserService(db, email, logger);
```

**مميزات Constructor Injection:**
- التبعيات واضحة ومرئية
- الكائن دائماً في حالة صالحة
- يمكن جعل الـ fields نوع `final`
- سهل الاختبار

#### 2️⃣ Setter Injection

```java
class UserService {
    private Database database;
    private EmailService emailService;

    // Setter لكل تبعية
    public void setDatabase(Database database) {
        this.database = database;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}

// الاستخدام:
UserService service = new UserService();
service.setDatabase(new MySQLDatabase());
service.setEmailService(new SMTPEmailService());
```

**متى تستخدم Setter Injection:**
- التبعيات اختيارية (Optional)
- تحتاج تغيير التبعية في Runtime

**⚠️ عيوب:**
- الكائن قد يكون في حالة غير صالحة
- لا يمكن جعل الـ fields نوع `final`

#### 3️⃣ Interface Injection (نادر الاستخدام)

```java
// واجهة للحقن
interface DatabaseInjectable {
    void injectDatabase(Database database);
}

class UserService implements DatabaseInjectable {
    private Database database;

    @Override
    public void injectDatabase(Database database) {
        this.database = database;
    }
}
```

---

## 📚 المستوى الخامس: Inversion of Control (IoC)

### ما هو IoC؟

IoC هو **مبدأ** أعم من DI. يعني أن التحكم في إنشاء الكائنات ينتقل من الكود إلى "حاوية" (Container) خارجية.

```
بدون IoC (التقليدي):
┌─────────────────────────────────────────┐
│              Your Code                  │
│  ┌─────────────────────────────────┐    │
│  │ UserService service =           │    │
│  │   new UserService(              │    │
│  │     new MySQLDatabase(),        │ ◄── أنت تتحكم في كل شيء
│  │     new SMTPEmailService()      │    │
│  │   );                            │    │
│  └─────────────────────────────────┘    │
└─────────────────────────────────────────┘

مع IoC:
┌─────────────────────────────────────────┐
│            IoC Container                │
│  ┌─────────────────────────────────┐    │
│  │ - يعرف كيف ينشئ Database        │    │
│  │ - يعرف كيف ينشئ EmailService   │ ◄── الـ Container يتحكم
│  │ - يعرف كيف ينشئ UserService    │    │
│  │ - يحقن التبعيات تلقائياً       │    │
│  └─────────────────────────────────┘    │
└─────────────────────────────────────────┘
          │
          ▼
┌─────────────────────────────────────────┐
│              Your Code                  │
│  ┌─────────────────────────────────┐    │
│  │ UserService service =           │    │
│  │   container.get(UserService);   │ ◄── فقط تطلب ما تحتاجه
│  └─────────────────────────────────┘    │
└─────────────────────────────────────────┘
```

### IoC Container بسيط (يدوي)

```java
// IoC Container بسيط
class DIContainer {
    private Map<Class<?>, Object> instances = new HashMap<>();
    private Map<Class<?>, Supplier<?>> factories = new HashMap<>();

    // تسجيل كلاس مع instance
    public <T> void registerInstance(Class<T> type, T instance) {
        instances.put(type, instance);
    }

    // تسجيل كلاس مع factory
    public <T> void registerFactory(Class<T> type, Supplier<T> factory) {
        factories.put(type, factory);
    }

    // الحصول على instance
    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        if (instances.containsKey(type)) {
            return (T) instances.get(type);
        }
        if (factories.containsKey(type)) {
            return (T) factories.get(type).get();
        }
        throw new RuntimeException("Type not registered: " + type.getName());
    }
}

// الاستخدام:
public class Main {
    public static void main(String[] args) {
        DIContainer container = new DIContainer();

        // تسجيل التبعيات
        container.registerInstance(Database.class, new MySQLDatabase());
        container.registerInstance(EmailService.class, new SMTPEmailService());
        container.registerInstance(Logger.class, new ConsoleLogger());

        // تسجيل UserService مع factory
        container.registerFactory(UserService.class, () -> {
            return new UserService(
                container.resolve(Database.class),
                container.resolve(EmailService.class),
                container.resolve(Logger.class)
            );
        });

        // الاستخدام - لا تحتاج معرفة التفاصيل!
        UserService userService = container.resolve(UserService.class);
        userService.createUser("Ahmed");
    }
}
```

---

## 📚 المستوى السادس: DIP في الـ Frameworks

### Spring Framework (Java الأشهر)

```java
// 1. تعريف Interface
public interface Database {
    void save(String data);
    String read(int id);
}

// 2. تنفيذ مع @Component
@Component
@Primary  // هذا هو التنفيذ الافتراضي
public class MySQLDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println("MySQL: Saving " + data);
    }

    @Override
    public String read(int id) {
        return "Data from MySQL";
    }
}

@Component
@Qualifier("postgres")
public class PostgreSQLDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println("PostgreSQL: Saving " + data);
    }

    @Override
    public String read(int id) {
        return "Data from PostgreSQL";
    }
}

// 3. حقن التبعية تلقائياً
@Service
public class UserService {
    private final Database database;

    @Autowired  // Spring يحقن تلقائياً!
    public UserService(Database database) {
        this.database = database;
    }

    public void createUser(String name) {
        database.save(name);
    }
}

// 4. لاستخدام تنفيذ محدد:
@Service
public class AdminService {
    private final Database database;

    @Autowired
    public AdminService(@Qualifier("postgres") Database database) {
        this.database = database;
    }
}
```

### كيف يعمل Spring؟

```
┌────────────────────────────────────────────────────────────┐
│                    Spring IoC Container                    │
│                                                            │
│  1. يفحص كل الكلاسات مع @Component, @Service, etc         │
│  2. يبني خريطة التبعيات                                    │
│  3. عند طلب Bean، ينشئه ويحقن كل تبعياته                  │
│                                                            │
│  ┌──────────────────────────────────────────────────┐     │
│  │ Beans:                                            │     │
│  │   MySQLDatabase     → Database.class             │     │
│  │   PostgreSQLDatabase → "postgres"                │     │
│  │   UserService       → (needs Database)           │     │
│  │   AdminService      → (needs "postgres")         │     │
│  └──────────────────────────────────────────────────┘     │
└────────────────────────────────────────────────────────────┘
```

---

## 📚 المستوى السابع: أمثلة متقدمة

### مثال 1: نظام إشعارات متعدد القنوات

```java
// Abstraction للإشعارات
interface NotificationService {
    void send(String userId, String message);
    boolean supports(NotificationType type);
}

enum NotificationType {
    EMAIL, SMS, PUSH, SLACK
}

// تنفيذات مختلفة
class EmailNotificationService implements NotificationService {
    @Override
    public void send(String userId, String message) {
        System.out.println("📧 Sending email to " + userId + ": " + message);
    }

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.EMAIL;
    }
}

class SMSNotificationService implements NotificationService {
    @Override
    public void send(String userId, String message) {
        System.out.println("📱 Sending SMS to " + userId + ": " + message);
    }

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.SMS;
    }
}

class PushNotificationService implements NotificationService {
    @Override
    public void send(String userId, String message) {
        System.out.println("🔔 Sending push to " + userId + ": " + message);
    }

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.PUSH;
    }
}

class SlackNotificationService implements NotificationService {
    @Override
    public void send(String userId, String message) {
        System.out.println("💬 Sending Slack to " + userId + ": " + message);
    }

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.SLACK;
    }
}

// High-level module يستخدم كل الإشعارات
class NotificationManager {
    private final List<NotificationService> services;

    public NotificationManager(List<NotificationService> services) {
        this.services = services;
    }

    public void notifyUser(String userId, String message, NotificationType... types) {
        for (NotificationType type : types) {
            services.stream()
                .filter(s -> s.supports(type))
                .forEach(s -> s.send(userId, message));
        }
    }

    public void notifyAll(String userId, String message) {
        services.forEach(s -> s.send(userId, message));
    }
}

// الاستخدام:
public class Main {
    public static void main(String[] args) {
        List<NotificationService> services = Arrays.asList(
            new EmailNotificationService(),
            new SMSNotificationService(),
            new PushNotificationService(),
            new SlackNotificationService()
        );

        NotificationManager manager = new NotificationManager(services);

        // إرسال بالبريد والـ SMS فقط
        manager.notifyUser("user123", "Your order is ready!", 
            NotificationType.EMAIL, NotificationType.SMS);

        // إرسال بكل القنوات
        manager.notifyAll("admin", "System alert!");
    }
}
```

### مثال 2: نظام دفع متعدد البوابات

```java
// Abstraction للدفع
interface PaymentGateway {
    PaymentResult processPayment(PaymentRequest request);
    boolean supportsCard(String cardType);
    String getGatewayName();
}

class PaymentRequest {
    String cardNumber;
    double amount;
    String currency;
    String cardType; // VISA, MASTERCARD, AMEX
}

class PaymentResult {
    boolean success;
    String transactionId;
    String errorMessage;
}

// تنفيذات مختلفة
class StripeGateway implements PaymentGateway {
    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        System.out.println("Processing via Stripe: $" + request.amount);
        PaymentResult result = new PaymentResult();
        result.success = true;
        result.transactionId = "STRIPE_" + System.currentTimeMillis();
        return result;
    }

    @Override
    public boolean supportsCard(String cardType) {
        return Arrays.asList("VISA", "MASTERCARD").contains(cardType);
    }

    @Override
    public String getGatewayName() { return "Stripe"; }
}

class PayPalGateway implements PaymentGateway {
    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        System.out.println("Processing via PayPal: $" + request.amount);
        PaymentResult result = new PaymentResult();
        result.success = true;
        result.transactionId = "PAYPAL_" + System.currentTimeMillis();
        return result;
    }

    @Override
    public boolean supportsCard(String cardType) {
        return true; // PayPal يدعم الكل
    }

    @Override
    public String getGatewayName() { return "PayPal"; }
}

// Payment Processor - High Level
class PaymentProcessor {
    private final List<PaymentGateway> gateways;
    private final Logger logger;

    public PaymentProcessor(List<PaymentGateway> gateways, Logger logger) {
        this.gateways = gateways;
        this.logger = logger;
    }

    public PaymentResult pay(PaymentRequest request) {
        // اختر البوابة المناسبة
        PaymentGateway gateway = gateways.stream()
            .filter(g -> g.supportsCard(request.cardType))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No gateway supports " + request.cardType));

        logger.info("Using gateway: " + gateway.getGatewayName());

        // نفذ الدفع
        PaymentResult result = gateway.processPayment(request);

        // سجل النتيجة
        if (result.success) {
            logger.info("Payment successful: " + result.transactionId);
        } else {
            logger.error("Payment failed: " + result.errorMessage);
        }

        return result;
    }
}
```

---

## 📚 المستوى الثامن: أفضل الممارسات والأخطاء الشائعة

### ✅ أفضل الممارسات

```java
// 1. ✅ استخدم Constructor Injection
class UserService {
    private final Database database; // final!

    public UserService(Database database) {
        this.database = database;
    }
}

// 2. ✅ البرمجة للـ Interface وليس Implementation
private final Database database;  // ✅ Interface
// private final MySQLDatabase database; // ❌ Implementation

// 3. ✅ Interface في package منفصل
// com.myapp.domain.ports.Database        (Interface)
// com.myapp.infrastructure.MySQLDatabase (Implementation)

// 4. ✅ Abstraction يملكها High-level module
// Database interface يكون مع UserService
// وليس مع MySQLDatabase

// 5. ✅ اسم Interface يعبر عن "ماذا" وليس "كيف"
interface UserRepository { }     // ✅ ماذا نريد
interface MySQLUserRepository { } // ❌ كيف ننفذ
```

### ❌ الأخطاء الشائعة

```java
// 1. ❌ إنشاء Interface لكل شيء
interface StringUtils { }  // ❌ لا حاجة له - لن يتغير

// 2. ❌ Interface يعكس Implementation
interface IMySQLDatabase { }  // ❌ الاسم يحدد التنفيذ

// 3. ❌ استخدام new داخل الكلاس
class UserService {
    private Database db;

    public void doSomething() {
        this.db = new MySQLDatabase(); // ❌ يجب الحقن
    }
}

// 4. ❌ Service Locator Pattern (Anti-pattern)
class UserService {
    public void doSomething() {
        Database db = ServiceLocator.get(Database.class); // ❌
    }
}

// 5. ❌ Circular Dependency
class A {
    public A(B b) { }  // A يحتاج B
}
class B {
    public B(A a) { }  // B يحتاج A - دائرة!
}
```

---

## 📚 المستوى التاسع: فوائد الاختبار

### بدون DIP - صعب الاختبار

```java
// ❌ صعب الاختبار
class UserService {
    private MySQLDatabase db = new MySQLDatabase();

    public void createUser(String name) {
        db.save(name);
    }
}

// الاختبار يحتاج MySQL حقيقي! 😱
@Test
void testCreateUser() {
    UserService service = new UserService();
    service.createUser("Test"); // يحفظ فعلياً في DB!
}
```

### مع DIP - سهل الاختبار

```java
// ✅ سهل الاختبار
class UserService {
    private final Database database;

    public UserService(Database database) {
        this.database = database;
    }

    public void createUser(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name required");
        }
        database.save(name);
    }
}

// الاختبار مع Mock
@Test
void testCreateUser_Success() {
    // Arrange
    MockDatabase mockDb = new MockDatabase();
    UserService service = new UserService(mockDb);

    // Act
    service.createUser("Ahmed");

    // Assert
    assertTrue(mockDb.getSavedData().contains("Ahmed"));
}

@Test
void testCreateUser_EmptyName_ThrowsException() {
    MockDatabase mockDb = new MockDatabase();
    UserService service = new UserService(mockDb);

    assertThrows(IllegalArgumentException.class, () -> {
        service.createUser("");
    });
}

// مع Mockito
@Test
void testCreateUser_WithMockito() {
    // Arrange
    Database mockDb = Mockito.mock(Database.class);
    UserService service = new UserService(mockDb);

    // Act
    service.createUser("Ahmed");

    // Assert
    Mockito.verify(mockDb).save("Ahmed");
}
```

---

## 📚 المستوى العاشر: متى لا تستخدم DIP؟

### حالات لا تحتاج فيها DIP:

```java
// 1. ✅ Utility Classes - لن تتغير أبداً
String.valueOf(123);           // لا تحتاج Interface
Math.max(a, b);                // لا تحتاج Interface
Collections.sort(list);        // لا تحتاج Interface

// 2. ✅ Value Objects - بيانات بسيطة
class Money {
    private BigDecimal amount;
    private String currency;
}

// 3. ✅ DTOs - نقل البيانات فقط
class UserDTO {
    String name;
    String email;
}

// 4. ✅ Entities بسيطة
class User {
    private String id;
    private String name;

    public void updateName(String name) {
        this.name = name;
    }
}

// 5. ✅ كلاسات لن تتغير
// إذا كنت متأكد 100% أن التنفيذ لن يتغير
// وليس هناك حاجة للاختبار المنفصل
```

### القاعدة الذهبية:

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│   استخدم DIP عندما:                                         │
│   ✓ التنفيذ قد يتغير (Database, API, Service)              │
│   ✓ تحتاج Unit Testing                                     │
│   ✓ تريد فصل Business Logic عن Technical Details           │
│                                                             │
│   لا تستخدم DIP عندما:                                      │
│   ✗ Utilities و Math operations                            │
│   ✗ Value Objects و DTOs                                   │
│   ✗ كلاسات بسيطة لن تتغير أبداً                             │
│                                                             │
│   التوازن هو المفتاح!                                       │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## الرسم البياني النهائي

```
                    ┌──────────────────────────────────────┐
                    │        Dependency Inversion          │
                    │           Principle (DIP)            │
                    └──────────────────────────────────────┘
                                     │
           ┌─────────────────────────┼─────────────────────────┐
           │                         │                         │
           ▼                         ▼                         ▼
    ┌──────────────┐         ┌──────────────┐         ┌──────────────┐
    │  القاعدة 1   │         │  القاعدة 2   │         │   الفائدة    │
    │              │         │              │         │              │
    │ High-Level   │         │ Abstractions │         │ Loose        │
    │ لا يعتمد على │         │ لا تعتمد على │         │ Coupling     │
    │ Low-Level    │         │ Details      │         │              │
    └──────────────┘         └──────────────┘         └──────────────┘
           │                         │                         │
           └─────────────────────────┼─────────────────────────┘
                                     │
                                     ▼
                    ┌──────────────────────────────────────┐
                    │        Dependency Injection          │
                    │           (التقنية)                  │
                    └──────────────────────────────────────┘
                                     │
           ┌─────────────────────────┼─────────────────────────┐
           │                         │                         │
           ▼                         ▼                         ▼
    ┌──────────────┐         ┌──────────────┐         ┌──────────────┐
    │ Constructor  │         │   Setter     │         │  Interface   │
    │  Injection   │         │  Injection   │         │  Injection   │
    │   (الأفضل)   │         │ (اختياري)   │         │   (نادر)     │
    └──────────────┘         └──────────────┘         └──────────────┘
                                     │
                                     ▼
                    ┌──────────────────────────────────────┐
                    │       IoC Container (اختياري)        │
                    │   Spring, Guice, Dagger, etc.       │
                    └──────────────────────────────────────┘
```

---

# ملخص مبادئ SOLID

| المبدأ | الاسم | القاعدة | الكلمة المفتاحية |
|--------|-------|---------|-----------------|
| **S** | Single Responsibility | كلاس واحد = مسؤولية واحدة | **فصل المسؤوليات** |
| **O** | Open/Closed | مفتوح للتوسع، مغلق للتعديل | **استخدم Interfaces** |
| **L** | Liskov Substitution | الابن يحل مكان الأب | **وراثة صحيحة** |
| **I** | Interface Segregation | واجهات صغيرة ومتخصصة | **قسم الواجهات** |
| **D** | Dependency Inversion | اعتمد على Abstractions | **Dependency Injection** |

---

# نصائح عملية

## متى تستخدم SOLID؟

```
استخدم SOLID عندما:
- المشروع كبير أو سيكبر
- يعمل عليه أكثر من مطور
- تحتاج صيانة طويلة المدى
- تحتاج اختبارات (Unit Tests)

لا تبالغ عندما:
- مشروع صغير جدا
- نموذج أولي (Prototype)
- كود لن يتغير أبدا
```

## الأخطاء الشائعة

```
تطبيق SOLID بشكل أعمى
   --> يؤدي لـ Over-engineering

إنشاء Interface لكل كلاس
   --> ليس كل شيء يحتاج Interface

تجاهل SOLID تماما
   --> كود صعب الصيانة

الحل: التوازن والحكم السليم
```

---

# العلاقة بين المبادئ

```
S (Single Responsibility)
|
+-- يساعد في تطبيق --> O (Open/Closed)
|                      |
|                      +-- يعتمد على --> L (Liskov)
|                                        |
+-- يرتبط بـ --------------------------> I (Interface Segregation)
                                         |
                                         +-- يؤدي إلى --> D (Dependency Inversion)
```

**كلها مترابطة:**
- **S** يجعلك تفصل المسؤوليات
- **O** يجعلك تستخدم Interfaces
- **L** يضمن صحة الوراثة
- **I** يجعلك تقسم الواجهات
- **D** يجعلك تعتمد على Abstractions

---

# الخلاصة

```
SOLID = كود نظيف + صيانة سهلة + توسع مرن

تعلمها --> طبقها بحكمة --> راجعها باستمرار

"الكود الجيد ليس الذي يعمل فقط،
 بل الذي يسهل فهمه وتعديله وتوسيعه"
```

---

**تم إعداد هذا الشرح لمساعدتك على فهم مبادئ SOLID من الصفر إلى الاحتراف**
