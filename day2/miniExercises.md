# Mini Exercises Day 2: State Management, cookies & sessions

[Exercise 1](#exercise-1-cookie-basics)
[Exercise 2](#exercise-2-session-basics)
[Exercise 3](#exercise-3-login-system-met-sessions)
[Exercise 4](#exercise-4-session-hijacking-demo)
[Exercise 5](#exercise-5-session-vs-cookie-decision-making)
[Exercise 6](#exercise-6-jwt-tokens)

---

## Exercise 1: Cookie basics 

### Deel a: Cookie read / write 

**Opdracht: Maak een theme switcher met cookies**

**Stappen:**
1. Maak een servlet `/theme` die een theme selector toont
2. Gebruik een cookie om de theme voorkeur op te slaan
3. Toon de huidige theme op de pagina

**Starter code:**
```java
@WebServlet("/theme")
public class ThemeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, 
                        HttpServletResponse response) 
            throws IOException {
        
        // TODO: Lees "theme" cookie
        String currentTheme = "light"; // default
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Vind de theme cookie
            }
        }
        
        // TODO: Genereer HTML met current theme
        // TODO: Toon form met light/dark selector
    }
    
    @Override
    protected void doPost(HttpServletRequest request, 
                         HttpServletResponse response) 
            throws IOException {
        
        // TODO: Lees nieuwe theme uit form
        String newTheme = request.getParameter("theme");
        
        // TODO: Maak cookie en zet deze
        Cookie themeCookie = new Cookie("theme", newTheme);
        themeCookie.setMaxAge(86400); // 24 uur
        themeCookie.setPath("/");
        themeCookie.setHttpOnly(true);
        response.addCookie(themeCookie);
        
        // TODO: Redirect terug naar GET
        response.sendRedirect("/theme");
    }
}
```

**Requirements:**
- Form met radio buttons: Light / Dark
- Cookie met 24 uur expiry
- HttpOnly flag gezet
- Page styling verandert op basis van theme

**Test checklist:**
- [ ] Submit form → theme verandert
- [ ] Refresh page → theme blijft staan
- [ ] DevTools → Application → Cookies → zie "theme" cookie
- [ ] Close browser, reopen → theme nog steeds correct
- [ ] Cookie heeft HttpOnly flag

### Deel b: Cookie inspectie

**Gebruik DevTools om cookies te inspecteren:**

1. Open DevTools → **Application** tab
2. Ga naar **Cookies** → `http://localhost:8080`
3. Bekijk alle cookies die gezet zijn

**Vragen te beantwoorden:**
1. Wat is de waarde van je theme cookie?
2. Wat is de expiry date?
3. Is HttpOnly gezet?
4. Is Secure gezet? (Waarom niet in development?)
5. Wat is het Path?

**Experiment:**
1. Verander de cookie waarde handmatig naar "hacker"
2. Refresh de page
3. Wat gebeurt er?
4. Waarom is dit een security probleem?

### Deel c: Cookie security test

**Probeer cookies te lezen via JavaScript:**

```javascript
// Open browser console en type:
document.cookie
```

**Vragen:**
- Kun je de cookie lezen?
- Waarom wel/niet?
- Wat zou er gebeuren zonder HttpOnly flag?

---

### BONUS (optioneel): Multi-cookie preferences

**Uitbreiding: Sla meerdere preferences op**

**Opdracht:**
1. Voeg language preference toe (nl/en)
2. Voeg font size preference toe (small/medium/large)
3. Sla elk in een aparte cookie op
4. Toon alle preferences op de pagina
5. Maak een "Reset to defaults" knop

**Extra extra challenge:**
- Maak een Cookie Manager pagina die ALLE cookies toont
- Toon naam, waarde, expiry, flags
- Knop om individuele cookies te verwijderen

**Verwacht resultaat:**
```
Current Preferences:
- Theme: dark
- Language: nl
- Font size: large

[Reset Preferences]
```

---

## Exercise 2: Session basics

### Deel a: Session opzetten

**Opdracht: Maak een simpele counter met sessions**

**Stappen:**
1. Maak een servlet `/counter` 
2. Gebruik session om een teller bij te houden
3. Toon de huidige waarde
4. Knop om te incrementen

```java
@WebServlet("/counter")
public class CounterServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, 
                        HttpServletResponse response) 
            throws IOException {
        
        // TODO: Haal (of maak) session
        HttpSession session = request.getSession(true);
        
        // TODO: Haal counter uit session (of start bij 0)
        Integer counter = (Integer) session.getAttribute("counter");
        if (counter == null) {
            counter = 0;
        }
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html><body>");
        out.println("<h1>Session counter</h1>");
        out.println("<p>Current count: " + counter + "</p>");
        out.println("<p>Session ID: " + session.getId() + "</p>");
        out.println("<form method='POST'>");
        out.println("  <button name='action' value='increment'>Increment</button>");
        out.println("  <button name='action' value='reset'>Reset</button>");
        out.println("</form>");
        out.println("</body></html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, 
                         HttpServletResponse response) 
            throws IOException {
        
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        
        Integer counter = (Integer) session.getAttribute("counter");
        if (counter == null) counter = 0;
        
        // TODO: Implementeer increment/reset
        if ("increment".equals(action)) {
        } else if ("reset".equals(action)) {
        }
        
        // TODO: Sla counter op in session
        // TODO: Redirect naar GET
    }
}
```

**Test checklist:**
- [ ] Click Increment → counter gaat omhoog
- [ ] Refresh page → counter blijft op zelfde waarde
- [ ] Open nieuwe tab → zelfde counter! (shared session)
- [ ] DevTools → Application → Cookies → zie JSESSIONID
- [ ] Click Reset → counter gaat naar 0

### Deel b: Session lifetime verkennen

**Experiment met session persistence:**

**Test 1: Zelfde browser, nieuwe tab**
1. Open counter op `/counter`
2. Increment naar 5
3. Open nieuwe tab, ga naar `/counter`
4. Wat zie je? (verwacht: 5)

**Test 2: Browser sluiten en heropenen**
1. Counter op 10
2. Sluit ALLE browser vensters
3. Heropen browser
4. Ga naar `/counter`
5. Wat zie je? (verwacht: nieuwe session, counter = 0)

**Test 3: Incognito mode**
1. Normal browser: counter = 7
2. Open incognito window
3. Ga naar `/counter`
4. Wat zie je? (verwacht: nieuwe session, counter = 0)

**Vragen:**
- Waarom deelt incognito niet de session?
- Hoe "weet" de server welke session bij jou hoort?
- Waar is de session data opgeslagen?

### Deel c: Session vs cookie comparison (10 min)

**Maak twee pagina's om het verschil te zien:**

**Pagina 1: Cookie counter (`/cookie-counter`)**
```java
// Sla counter op in cookie
Cookie counter = new Cookie("count", String.valueOf(count));
response.addCookie(counter);
```

**Pagina 2: Session counter (`/session-counter`)**
```java
// Sla counter op in session
session.setAttribute("count", count);
```

**Vergelijk met DevTools:**
1. Beide counters op 10 brengen
2. Open DevTools → Application
3. Bekijk Cookies → wat zie je?
4. Bekijk Session Storage → wat zie je?
5. Welke data is zichtbaar in de browser?

**Discussie:**
- Wat is het verschil in wat de browser ziet?
- Welke is veiliger voor gevoelige data?
- Welke heeft een size limiet?

---

### BONUS (optioneel): Shopping cart

**Opdracht: Maak een mini shopping cart met sessions**

**Requirements:**
1. Product lijst pagina:
   - Toon 5 producten met prijzen
   - "Add to Cart" knop per product

2. Cart pagina:
   - Toon alle items in cart
   - Toon totaal prijs
   - "Remove" knop per item
   - "Clear Cart" knop

3. Session management:
   - Gebruik `List<CartItem>` in session
   - CartItem heeft: name, price, quantity

**Starter structure:**
```java
class CartItem {
    String name;
    double price;
    int quantity;
}

// In session:
List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
if (cart == null) {
    cart = new ArrayList<>();
}
```

**Extra challenge:**
- Toon cart item count in header van elke pagina
- Implement quantity increment/decrement
- Add checkout button (just show summary)

---

## Exercise 3: Login system met sessions

### Deel a: Login Pagina

**Opdracht: Maak een "werkend" login systeem**

```java
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    // Hardcoded users voor demo
    private static final Map<String, String> USERS = Map.of(
        "admin", "admin123",
        "user", "password",
        "jan", "geheim"
    );
    
    @Override
    protected void doGet(HttpServletRequest request, 
                        HttpServletResponse response) 
            throws IOException {
        
        // TODO: Check if already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            response.sendRedirect("/profile");
            return;
        }
        
        // TODO: Toon login form
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String error = request.getParameter("error");
        
        out.println("<html><body>");
        out.println("<h1>Login</h1>");
        
        if (error != null) {
            out.println("<p style='color:red'>Invalid username or password!</p>");
        }
        
        out.println("<form method='POST'>");
        out.println("  <label>Username: <input name='username' required /></label><br/>");
        out.println("  <label>Password: <input name='password' type='password' required /></label><br/>");
        out.println("  <button type='submit'>Login</button>");
        out.println("</form>");
        out.println("</body></html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, 
                         HttpServletResponse response) 
            throws IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // TODO: Validate credentials
        if (USERS.containsKey(username) && 
            USERS.get(username).equals(password)) {
            
            // TODO: Create session and store username
            HttpSession session = request.getSession(true);
            session.setAttribute("username", username);
            session.setAttribute("loginTime", System.currentTimeMillis());
            
            // TODO: Redirect to profile
            response.sendRedirect("/profile");
        } else {
            // TODO: Redirect back to login with error
            response.sendRedirect("/login?error=true");
        }
    }
}
```

**Test checklist:**
- [ ] Login met correcte credentials → redirect naar profile
- [ ] Login met foute credentials → error message
- [ ] Na inloggen: profile page toont username
- [ ] JSESSIONID cookie gezet na login

### Deel b: Protected profile page

**Opdracht: Maak een profile pagina die alleen ingelogde users kunnen zien**

```java
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, 
                        HttpServletResponse response) 
            throws IOException {
        
        // TODO: Check if user is logged in
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("username") == null) {
            // TODO: Redirect to login
            response.sendRedirect("/login");
            return;
        }
        
        // TODO: Get user info from session
        String username = (String) session.getAttribute("username");
        Long loginTime = (Long) session.getAttribute("loginTime");
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html><body>");
        out.println("<h1>Profile</h1>");
        out.println("<p>Welcome, " + username + "!</p>");
        out.println("<p>Session ID: " + session.getId() + "</p>");
        out.println("<p>Logged in at: " + new Date(loginTime) + "</p>");
        out.println("<p>Session created: " + new Date(session.getCreationTime()) + "</p>");
        out.println("<p>Last accessed: " + new Date(session.getLastAccessedTime()) + "</p>");
        out.println("<form action='/logout' method='POST'>");
        out.println("  <button type='submit'>Logout</button>");
        out.println("</form>");
        out.println("</body></html>");
    }
}
```

**Test scenarios:**
1. Direct naar `/profile` zonder login → redirect naar login
2. Login eerst, dan `/profile` → zie profile
3. Check session timestamps in profile

### Deel c: Logout implementeren

**Opdracht: Maak logout functionaliteit**

```java
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, 
                         HttpServletResponse response) 
            throws IOException {
        
        // TODO: Get session (don't create new one!)
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // TODO: Invalidate session (destroys it)
            session.invalidate();
        }
        
        // TODO: Redirect to login
        response.sendRedirect("/login");
    }
}
```

**Test flow:**
1. Login → profile page
2. Click logout
3. Redirect naar login
4. Probeer terug naar profile → redirect naar login (niet ingelogd)
5. Check DevTools → JSESSIONID cookie weg? (afhankelijk van server implementation)

---

### BONUS (optioneel): Advanced authentication 

**Level 1: Remember me**

**Opdracht: Implement "remember me" functionaliteit**

```java
// In login POST:
String rememberMe = request.getParameter("remember");

if ("on".equals(rememberMe)) {
    // Create long-lived cookie (30 days)
    String token = UUID.randomUUID().toString();
    
    Cookie rememberCookie = new Cookie("remember_token", token);
    rememberCookie.setMaxAge(30 * 24 * 60 * 60); // 30 dagen
    rememberCookie.setHttpOnly(true);
    response.addCookie(rememberCookie);
    
    // Store token in session or database
    session.setAttribute("remember_token", token);
}

// In subsequent visits:
// Check remember_token cookie
// Auto-login if valid
```

**Level 2: Session timeout warning**

**Opdracht: Waarschuw users voordat session expires**

```java
// Set session timeout to 5 minutes (for testing)
session.setMaxInactiveInterval(5 * 60); // 5 minutes

// Add JavaScript to profile page:
<script>
// Warn at 4 minutes (1 minute before timeout)
setTimeout(() => {
    if (confirm('Your session will expire in 1 minute. Continue?')) {
        // Make request to keep session alive
        fetch('/keepalive');
    }
}, 4 * 60 * 1000);
</script>
```

---

## Exercise 4: Session hijacking demo

### Deel a: Session hijacking uitvoeren

**WAARSCHUWING: Dit is een security demonstratie. Deze code is niet secure!**

**Setup: Maak een "Secret" pagina**

```java
@WebServlet("/secret")
public class SecretServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, 
                        HttpServletResponse response) 
            throws IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("/login");
            return;
        }
        
        String username = (String) session.getAttribute("username");
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html><body>");
        out.println("<h1>🔒 Secret Page</h1>");
        out.println("<p>Welcome, " + username + "!</p>");
        out.println("<div style='background:yellow; padding:10px;'>");
        out.println("<h2>Confidential Information:</h2>");
        out.println("<p>Credit Card: 1234-5678-9012-3456</p>");
        out.println("<p>Password: SuperSecret123</p>");
        out.println("<p>SSN: 123-45-6789</p>");
        out.println("</div>");
        out.println("<p><strong>Session ID: " + session.getId() + "</strong></p>");
        out.println("</body></html>");
    }
}
```

**De Attack Stappen:**

**Victim (jij in normale browser):**
1. Login via `/login`
2. Ga naar `/secret`
3. Zie de geheime info
4. Open DevTools → Application → Cookies
5. **KOPIEER de JSESSIONID waarde**

**Attacker (jij in incognito venster):**
1. Open incognito window
2. Ga naar `/secret` → wordt doorgestuurd naar login (niet ingelogd)
3. Open DevTools → Application → Cookies
4. **Voeg handmatig een cookie toe:**
   - Name: `JSESSIONID`
   - Value: [de gekopieerde waarde]
   - Domain: `localhost`
   - Path: `/`
5. Refresh de pagina
6. **JE ZIET DE SECRET DATA!** zonder in te loggen!

**Wat gebeurde er?**
- Server identificeert users alleen via session ID
- Je hebt de session ID "gestolen"
- Server denkt jij bent de victim
- Complete identity theft!

### Deel b: Security analyse (10 min)

**Discussie vragen:**

1. **Hoe zou een echte attacker de session ID stelen?**
   - WiFi sniffing (als geen HTTPS)
   - XSS attack (JavaScript leest cookie)
   - Browser extension malware
   - Man-in-the-middle attack

2. **Hoe voorkom je dit?**
   - HTTPS (encrypts alle traffic)
   - HttpOnly cookies (JavaScript kan niet lezen)
   - SameSite cookies (CSRF protection)
   - Session regeneration na login
   - IP address validation
   - Short session timeouts

3. **Waarom is HTTPS zo cruciaal?**
   ```
   HTTP (plain text):
   Cookie: JSESSIONID=abc123xyz
   ↑ Iedereen op het netwerk kan dit lezen!
   
   HTTPS (encrypted):
   [encrypted data]
   ↑ Alleen jij en server kunnen dit lezen
   ```

**Group activity:**
Maak een lijst van alle websites waar je ingelogd bent.
Hoeveel daarvan gebruiken HTTPS?
Wat als ze geen HTTPS gebruiken?

---

## Exercise 5: Session vs Cookie Decision Making (20 minutes)

### Opdracht: Kies de juiste oplossing

**Voor elk scenario, beslis: Cookie of Session?**

**Scenario 1: Theme preference**
- Data: "dark" or "light"
- Sensitivity: Low
- Size: ~5 bytes
- **Jouw keuze:** Cookie of Session? Waarom?

**Scenario 2: Shopping cart**
- Data: List van ProductID + quantity
- Sensitivity: Low-Medium
- Size: Kan groot worden (100+ items)
- **Jouw keuze:** Cookie of Session? Waarom?

**Scenario 3: Login status**
- Data: Username, role, permissions
- Sensitivity: High
- Size: ~50-100 bytes
- **Jouw keuze:** Cookie of Session? Waarom?

**Scenario 4: Language preference**
- Data: "nl" or "en"
- Sensitivity: Low
- Size: 2 bytes
- Needs to survive: Session timeout
- **Jouw keuze:** Cookie of Session? Waarom?

**Scenario 5: Form wizard data**
- Data: Multi-step form data (5 steps)
- Sensitivity: Medium-High
- Size: ~1KB
- Duration: Temporary (during wizard only)
- **Jouw keuze:** Cookie of Session? Waarom?

**Scenario 6: Analytics tracking ID**
- Data: UUID for analytics
- Sensitivity: Low
- Size: 36 bytes
- Needs to survive: Months/years
- **Jouw keuze:** Cookie of Session? Waarom?

### Groepsdiscussie

**Maak een decision tree:**
```
Start
  ↓
Sensitive data? 
  Yes → SESSION
  No → ↓
Large data (>1KB)?
  Yes → SESSION
  No → ↓
Needs to survive session timeout?
  Yes → COOKIE
  No → SESSION
```

**Discuss:**
- Are there exceptions to this tree?
- What about hybrid approaches?
- When would you store session ID in cookie + data on server?

---

### BONUS (optioneel): Build a preference manager (30 min)

**Opdracht: Maak een systeem dat slim besluit waar data wordt opgeslagen**

```java
public class PreferenceManager {
    
    public void set(String key, String value, 
                   boolean sensitive, 
                   boolean persistent,
                   HttpSession session,
                   HttpServletResponse response) {
        
        if (sensitive) {
            // Always use session for sensitive data
            session.setAttribute(key, value);
        } else if (persistent) {
            // Use cookie for non-sensitive, persistent data
            Cookie cookie = new Cookie(key, value);
            cookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        } else {
            // Use session for non-sensitive, temporary data
            session.setAttribute(key, value);
        }
    }
    
    public String get(String key,
                     HttpSession session,
                     HttpServletRequest request) {
        
        // Check session first
        Object value = session.getAttribute(key);
        if (value != null) {
            return (String) value;
        }
        
        // Check cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }
        
        return null;
    }
}
```

**Test je PreferenceManager:**
```java
PreferenceManager pm = new PreferenceManager();

// Sensitive data → session
pm.set("username", "john", true, false, session, response);

// Persistent preference → cookie
pm.set("theme", "dark", false, true, session, response);

// Temporary data → session
pm.set("wizardStep", "3", false, false, session, response);
```

---
