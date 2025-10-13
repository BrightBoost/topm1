# Mini Exercises Day 1

## Exercise 1: HTTP in Action (25 minutes)

### Deel a: Website request verkennen 

**Stappen:**
1. Open een bekende website (bijv. nu.nl, tweakers.net)
2. Open DevTools (F12) → **Network** tab
3. **Refresh** de pagina (of clear en reload)
4. Klik op de **eerste request** (meestal het HTML document)
5. Verken alle tabs:
   - **Headers**: Request/Response headers
   - **Preview**: Gerenderde content
   - **Response**: Raw response data
   - **Timing**: Hoe lang duurde elke fase?

**Wat te observeren:**
- Request method (GET of POST?)
- Status code (200, 304, 404?)
- Request headers (User-Agent, Accept, Cookie)
- Response headers (Content-Type, Set-Cookie, Cache-Control)
- Response body (HTML, JSON, etc.)

**Vragen te beantwoorden:**
1. Hoeveel HTTP requests genereert één pagina load?
2. Welke types resources worden geladen? (HTML, CSS, JS, images, fonts?)
3. Wat is de totale load time?
4. Welke request duurde het langst?

### Deel b: een eigen endpoint maken (15 min)

**Maak een simpele Spring Boot endpoint:**
- Maak een simpele Spring Boot REST controller met een GET endpoint `/kaas` die een random kaassoort terug geeft. Als er een nummer wordt meegegeven `kaas?nr=1`, toon dan de kaas met die index.
- Voeg een POST `/submitkaas` terug die een specifieke kaas uit de JSON body aan de lijst toevoegt.
    

**Inspecteer via DevTools:**
1. Start je Spring Boot app
2. Ga naar `http://localhost:8080/kaas`
3. Open DevTools → Network
4. Probeer: `http://localhost:8080/kaas?nr=3`
5. Bekijk de Request/Response headers
6. Wat is het verschil tussen de twee requests?

**Postman/cURL**
```bash
# GET request
curl http://localhost:8080/kaas

# POST request
curl -X POST http://localhost:8080/submitkaas \
  -d "kaasnaam=gouda"
```

**Vraag:**
- Wat gebeurt er als je `/submitkaas` aanroept met GET in plaats van POST?

---

## Exercise 2: GET vs POST Forms (25 minutes)

### Deel a: GET Form - Data in URL 

**Maak `form-get.html`:**
```html
<!DOCTYPE html>
<html>
<head>
    <title>GET form demo</title>
    <!-- <style>You can make it pretty</style> -->
</head>
<body>
    <h1>Login form (GET)</h1>
    <div>
        Let op: Dit formulier gebruikt GET. Kijk goed naar de URL na submit!
    </div>
    
    <form action="/submit" method="GET">
        <label>Username:</label>
        <input type="text" name="username" value="jan123" />
        
        <label>Email:</label>
        <input type="email" name="email" value="jan@example.com" />
        
        <label>Password:</label>
        <input type="password" name="password" value="geheim123" />
        
        <button type="submit">Submit</button>
    </form>
    
    <p><em>Tip: Open DevTools (F12) voor je submit!</em></p>
</body>
</html>
```

**Taken:**
1. Open `form-get.html` in je browser
2. Open DevTools en ga naar de Network tab
3. Submit het formulier
4. **Kijk naar de URL**: waar staat je data?
5. Wat zie je in de Network tab?

**Vragen:**
- Kun je het wachtwoord lezen in de URL?
- Wat gebeurt er als je de URL bookmark?
- Kun je de URL delen met iemand anders?

### Deel b: POST form, de data in body 

**Maak `form-post.html`:**
```html
<!DOCTYPE html>
<html>
<head>
    <title>POST form demo</title>
    <!-- <style>Prettifying goes here</style> -->
</head>
<body>
    <h1>Login Form (POST)</h1>
    <div>
        Dit formulier gebruikt POST en de data zit in de request body, niet in de URL
    </div>
    
    <form action="/submit" method="POST">
        <label>Username:</label>
        <input type="text" name="username" value="jan123" />
        
        <label>Email:</label>
        <input type="email" name="email" value="jan@example.com" />
        
        <label>Password:</label>
        <input type="password" name="password" value="geheim123" />
        
        <button type="submit">Submit</button>
    </form>
    
    <p><em>Check DevTools en ga naar Network</em></p>
</body>
</html>
```

**Taken:**
1. Open `form-post.html` in browser
2. DevTools en ga naar Network tab
3. Submit het formulier
4. Klik op de POST request
5. Ga naar **Payload** (of **Request** tab)
6. Zie de form data

**Vragen:**
- Staat de data nu in de URL?
- Waar staat de data wel?
- Is dit veiliger voor wachtwoorden?

### Deel c: Vergelijking (5 min)

**Maak een tabel voor een vergelijking met de volgende punten:**
- Data locatie
- Zichtbaar in URL
- Bookmarkable
- Back button safe
- Cacheable
- Data size limiet
- Geschikt voor wachtwoorden

**Discussie:**
- Wanneer gebruik je GET?
- Wanneer gebruik je POST?
- Wat zijn de (security) implicaties?

---

## Exercise 3: Setting up your first Servlet (45 minutes)

### Deel a: Project setup 

**Maak een nieuw Maven project:**

```xml
<!-- pom.xml -->
<groupId>nl.topicus.training</groupId>
<artifactId>servletdemo</artifactId>
<version>1.0-SNAPSHOT</version>
<packaging>war</packaging>
```
And also in `pom.xml`:


```xml
<!-- pom.xml -->
<dependencies>
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>4.0.1</version>
        <scope>provided</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.eclipse.jetty</groupId>
            <artifactId>jetty-maven-plugin</artifactId>
            <version>11.0.20</version>
            <!-- version might have to be adapted based on Java version -->
            <!-- for Java 17 -->
            <!-- <version>9.4.48.v20220622</version> -->
        </plugin>
    </plugins>
</build>
```

**Project structuur:**
```
src/main/java/com/yourname/
├── HelloServlet.java
├── FormServlet.java
└── CounterServlet.java
```

### Deel b: GET Endpoint Maken (10 min)

**Opdracht: Maak een servlet die GET requests afhandelt**

```java
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, 
                        HttpServletResponse response) 
            throws IOException {
        
        // TODO: Implement this!
        // 1. Get 'name' parameter from request
        // 2. Set content type to "text/html"
        // 3. Generate HTML greeting
        // 4. If no name provided, greet "World"
        
        // Your code here...
    }
}
```

**Requirements:**
- Accept `name` parameter: `/hello?name=Jan`
- Default to "World" if no name
- Return HTML with `<h1>` greeting
- Include a link back to home

**Test:**
- Spin up with: `mvn jetty:run`
- `http://localhost:8080/hello` → "Hello world!"
- `http://localhost:8080/hello?name=Jan` → "Hello Jan!"

### Deel c: POST endpoint maken (10 min)

**Opdracht: Maak een servlet die POST requests afhandelt**

```java
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, 
                        HttpServletResponse response) 
            throws IOException {
        // Show registration form
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html><body>");
        out.println("<h1>Register</h1>");
        out.println("<form method='POST'>");
        // TODO: Add form fields for:
        // - username (text)
        // - email (email)
        // - age (number)
        out.println("</form>");
        out.println("</body></html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, 
                         HttpServletResponse response) 
            throws IOException {
        // TODO: Process registration
        // 1. Get parameters
        // 2. Validate (not null, not empty)
        // 3. Display confirmation or error
        
        // Your code here...
    }
}
```

**Requirements:**
- Form with username, email, age
- Validate all fields are filled
- Show confirmation with all data
- Show error if validation fails

### Deel d: Multithreading Awareness (10 min)

**Probleem demonstratie:**

```java
@WebServlet("/counter")
public class CounterServlet extends HttpServlet {
    
    // PROBLEEM: Instance variable!
    private int counter = 0;
    
    @Override
    protected void doGet(HttpServletRequest request, 
                        HttpServletResponse response) 
            throws IOException {
        
        // Simuleer langzame processing
        counter++;
        try {
            Thread.sleep(100); // 100ms delay
        } catch (InterruptedException e) {}
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Counter: " + counter + "</h1>");
        out.println("<p>Refresh a few times...</p>");
        out.println("</body></html>");
    }
}
```

**Experiment:**
1. Start de servlet
2. Open 3-4 browser tabs
3. Refresh alle tabs **snel achter elkaar**
4. Kijk naar de counter values
5. Zijn ze sequentieel? Of zie je rare getallen?

**Waarom gebeurt dit?**
```
Thread 1: counter++ (counter = 1)
Thread 2: counter++ (counter = 2)
Thread 1: sleep(100)
Thread 2: sleep(100)
Thread 1: show counter bijv 2 (verwacht 1)
```

**Discussie vragen:**
1. Waarom is dit een probleem?
2. Wat als dit een "balance" was in banking software?
3. Hoe zouden we dit kunnen oplossen?

**Oplossingen (preview):**
- Instance variables vermijden
- Local variables gebruiken
- Session data (Day 2!)
- Database transactions
- `synchronized` keyword (advanced)

**Vuistregel voor vandaag:**
```java
// NIET DOEN (shared state)
private int userCount = 0;
private List<String> users = new ArrayList<>();

// WEL DOEN (geen state in servlet)
protected void doGet(...) {
    String name = request.getParameter("name");  // Local var
    int count = getUserCountFromDatabase();      // Database
    // Process and respond
}
```

---

## Exercise 4: Browser resubmit warning (20 minutes)

### Deel a: Het probleem ervaren

**Setup:** Je hebt een werkende servlet nodig (gebruik de demo app)

```java
@WebServlet("/buy")
public class PurchaseServlet extends HttpServlet {
    private static int purchaseCount = 0;
    
    @Override
    protected void doPost(HttpServletRequest request, 
                         HttpServletResponse response) 
            throws IOException {
        
        String item = request.getParameter("item");
        purchaseCount++;
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Purchase Confirmed!</h1>");
        out.println("<p>You bought: " + item + "</p>");
        out.println("<p>Total purchases today: " + purchaseCount + "</p>");
        out.println("<p><strong>Probeer nu F5 te drukken!</strong></p>");
        out.println("</body></html>");
    }
}
```

**HTML Form:**
```html
<form action="/buy" method="POST">
    <input type="hidden" name="item" value="Laptop (€1000)">
    <button type="submit">Koop Laptop</button>
</form>
```

**Stappen:**
1. Submit het formulier
2. Zie de confirmation page
3. Druk op **F5** (refresh)
4. Zie de browser warning: "Confirm form resubmission"
5. Klik "OK" en zie wat gebeurt
6. Herhaal dit 3x

**Observaties:**
- De `purchaseCount` stijgt elke keer!
- Je hebt nu 4x een laptop gekocht!
- Dit is een **groot probleem** voor e-commerce

### Deel b: Real-world voorbeelden 

**Wanneer zie je deze warning?**
- Betaling voltooien (dubbele betaling!)
- Formulier submission (dubbele comments/posts)
- Account aanmaken (dubbele accounts)
- Email versturen (dubbele emails)

**Groepsdiscussie:**
1. Heb je deze warning wel eens gezien?
2. Waar ging het mis?
3. Welke problemen kan dit veroorzaken?


**Dit gaan we oplossen op Day 4 met PRG pattern**

---

## Exercise 5: Bonus challenge 

### Opdracht: Fix de bugs

**Hier is een servlet met 5 bugs. Vind ze allemaal!**

```java
@WebServlet("/buggy")
public class BuggyServlet extends HttpServlet {
    
    private String lastUser;  
    
    protected void doGet(HttpServletRequest request, 
                        HttpServletResponse response) 
            throws IOException {
        
        String name = request.getParameter("name");
        lastUser = name;
        
        PrintWriter out = response.getWriter(); 
        
        out.println("<html><body>");
        out.println("<h1>Hello " + name.toUpperCase() + "</h1>"); 
        out.println("<p>Previous user: " + lastUser + "</p>");
        out.println("</body></html>");
    }
    
    protected void doPost(HttpServletRequest request, 
                         HttpServletResponse response) 
            throws IOException {
        
        response.sendRedirect("/success?name=" + request.getParameter("name")); 
        
        response.setContentType("text/html");  
        PrintWriter out = response.getWriter();
        out.println("This won't work...");
    }
}
```

**Hints:**
1. Wat als `name` null is?
2. Content type vergeten?
3. Multithreading probleem?
4. URL encoding vergeten?
5. Volgorde probleem?

**Bonus:** Fix alle bugs en test de servlet!

