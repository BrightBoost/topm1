# Client-Server Interaction Patterns - Complete Demo Suite

## 7 Demos Included

1. **Demo 0: Hardcoded HTML** - Why we need templates
2. **Demo 1: Templates WITHOUT PRG** - Shows double-submit problem  
3. **Demo 2: Templates WITH PRG** - How Wicket prevents duplicates
4. **Demo 3: AJAX Returning HTML** - Partial page updates
5. **Demo 4: Form Validation** - Server validation + Flash pattern
6. **Demo 5: Error Handling** - Different error types
7. **Demo 6: Component-Based Rendering** - Wicket's architecture

## Quick Start

```bash
mvn clean package
mvn jetty:run
```

## What You'll Learn

| You Build Manually | Wicket Does Automatically |
|-------------------|---------------------------|
| `TemplateRenderer.render("page.html")` | Reads YourPage.html |
| `html.replace("{{name}}", value)` | `add(new Label("name", value))` |
| `response.sendRedirect("/success")` | `setResponsePage(Success.class)` |
| Detect AJAX, return HTML fragment | `target.add(component)` |
| Validate, flash errors, redirect | Form validators + FeedbackPanel |
| Component classes with render() | Component hierarchy |

## Teaching Flow

### Part 1: Templates & PRG (45 min)
- Demo 0: Feel the pain of hardcoded HTML
- Demo 1: Templates are better, but has double-submit issue
- Demo 2: PRG pattern solves it 
- Exercise 0: Fix the double submit bug

### Part 2: AJAX (20 min)
- Demo 3: Partial updates without JSON
- Progressive enhancement (works without JS)

### Part 3: Validation & Errors (45 min)
- Demo 4: Server validation + flash pattern
- Exercise 1: Add AJAX enhancement (keep fallback working)
- Demo 5: Different error types, different handling
- Exercise 2: Adding servlet error handling

### Part 4: Components (30 min)
- Demo 6: Component hierarchy
- Reusable components

### Project work (half a day)

## Key Concepts

- HTML templates separate presentation from logic
- POST-Redirect-GET prevents double-submit
- AJAX with HTML enables partial updates
- Flash pattern provides one-time messages
- Server validation is mandatory
- Components encapsulate rendering

## Testing Each Demo

**Demo 0:** See how painful hardcoded HTML is  
**Demo 1:** Submit → F5 → See "Resubmit form?" warning  
**Demo 2:** Submit → F5 → No warning, safe!  
**Demo 3:** Add to cart → No page reload! Disable JS → Still works!  
**Demo 4:** Invalid input → See errors → F5 → Errors gone (flash!)  
**Demo 5:** Order too many (alter HTMl first) → Business logic error  
**Demo 6:** See components building the page  
