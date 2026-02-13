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

## التعريف

> "الكلاسات عالية المستوى لا يجب أن تعتمد على كلاسات منخفضة المستوى. كلاهما يجب أن يعتمد على Abstractions"

بمعنى آخر: اعتمد على Interfaces وليس على Classes مباشرة.

## لماذا هذا المبدأ مهم؟

1. **المرونة**: سهولة تبديل التنفيذات
2. **الاختبار**: سهولة عمل Mock للاختبار
3. **فك الارتباط**: تقليل التبعيات بين الكلاسات

---

## المثال السيء (Violates DIP)

**المسار:** `D_DependencyInversion/bad/`

```java
// كلاس منخفض المستوى
class MySQLDatabase {
    public void save(String data) {
        System.out.println("Saving to MySQL: " + data);
    }

    public String read(int id) {
        return "Data from MySQL";
    }
}

// BAD: كلاس عالي المستوى يعتمد مباشرة على MySQL
class UserService {
    // اعتماد مباشر على كلاس محدد
    private MySQLDatabase database = new MySQLDatabase();

    public void createUser(String name) {
        // business logic
        database.save(name);
    }

    public String getUser(int id) {
        return database.read(id);
    }
}
```

### المشاكل:

```
UserService يعتمد مباشرة على MySQLDatabase
                    |
تريد تغيير لـ PostgreSQL؟ يجب تعديل UserService!
                    |
تريد عمل Unit Test؟ صعب جدا!
                    |
ارتباط قوي (Tight Coupling) = كود صعب الصيانة
```

```java
// إذا أردت تغيير قاعدة البيانات:
class UserService {
    // يجب تغيير هذا السطر
    private PostgreSQLDatabase database = new PostgreSQLDatabase();
    // ويجب التأكد أن كل الدوال متوافقة
}
```

---

## المثال الصحيح (Follows DIP)

**المسار:** `D_DependencyInversion/good/`

### 1. إنشاء Abstraction (Interface)

```java
// واجهة مجردة لقاعدة البيانات
interface Database {
    void save(String data);
    String read(int id);
}
```

### 2. تنفيذات مختلفة

```java
// تنفيذ MySQL
class MySQLDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println("Saving to MySQL: " + data);
    }

    @Override
    public String read(int id) {
        return "Data from MySQL";
    }
}

// تنفيذ PostgreSQL
class PostgreSQLDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println("Saving to PostgreSQL: " + data);
    }

    @Override
    public String read(int id) {
        return "Data from PostgreSQL";
    }
}

// تنفيذ MongoDB
class MongoDBDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println("Saving to MongoDB: " + data);
    }

    @Override
    public String read(int id) {
        return "Data from MongoDB";
    }
}

// تنفيذ للاختبار
class MockDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println("[TEST] Mock save: " + data);
    }

    @Override
    public String read(int id) {
        return "[TEST] Mock data";
    }
}
```

### 3. الكلاس عالي المستوى يعتمد على Interface

```java
// GOOD: يعتمد على Interface وليس كلاس محدد
class UserService {
    private Database database;  // Interface!

    // Dependency Injection عبر Constructor
    public UserService(Database database) {
        this.database = database;
    }

    public void createUser(String name) {
        // business logic
        database.save(name);
    }

    public String getUser(int id) {
        return database.read(id);
    }
}
```

### 4. الاستخدام - المرونة الكاملة

```java
public class Main {
    public static void main(String[] args) {
        // استخدام MySQL
        Database mysql = new MySQLDatabase();
        UserService service1 = new UserService(mysql);
        service1.createUser("Ahmed");

        // تغيير لـ PostgreSQL - بدون تعديل UserService!
        Database postgres = new PostgreSQLDatabase();
        UserService service2 = new UserService(postgres);
        service2.createUser("Mohamed");

        // تغيير لـ MongoDB - بدون تعديل UserService!
        Database mongo = new MongoDBDatabase();
        UserService service3 = new UserService(mongo);
        service3.createUser("Ali");

        // للاختبار - سهل جدا!
        Database mockDb = new MockDatabase();
        UserService testService = new UserService(mockDb);
        testService.createUser("Test User");
    }
}
```

### الفرق:

```
قبل (بدون DIP):
UserService -----------------> MySQLDatabase
     |
     +-- تغيير DB = تعديل UserService

بعد (مع DIP):
UserService -----------------> Database (Interface)
                                    ^
                                    |
         +--------------------------+--------------------------+
         |                          |                          |
    MySQLDatabase          PostgreSQLDatabase          MongoDBDatabase
         |
         +-- تغيير DB = فقط أنشئ instance جديد!
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
