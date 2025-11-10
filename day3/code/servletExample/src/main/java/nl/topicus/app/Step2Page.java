package nl.topicus.app;

import nl.topicus.app.components.DetailComponent;
import nl.topicus.framework.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Step 2: Show details about the selected language
 *
 * This page is rendered via AJAX (no full page refresh!)
 * Gets the selected choice from session and shows details
 */
public class Step2Page extends Page {

    private DetailComponent details;

    public Step2Page(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);

        // Get the selected choice from session
        String selectedLanguage = getSessionData("selectedLanguage", String.class);

        // Create detail component
        details = new DetailComponent("language-details");

        // Set content based on selection
        setDetailsForLanguage(selectedLanguage);
    }

    private void setDetailsForLanguage(String language) {
        if (language == null) language = "unknown";

        switch (language) {
            case "java":
                details.setContent(
                        "Java ☕",
                        "☕",
                        "Java is een veelgebruikte, object-georiënteerde programmeertaal die draait op de Java Virtual Machine (JVM).",
                        "Platform-onafhankelijk: Write Once, Run Anywhere",
                        "Sterk getypeerd met compile-time checking",
                        "Grote standaard library",
                        "Gebruikt voor enterprise applicaties, Android apps, en meer",
                        "Frameworks: Spring, Hibernate, Apache Wicket"
                );
                break;

            case "python":
                details.setContent(
                        "Python 🐍",
                        "🐍",
                        "Python is een high-level programmeertaal bekend om zijn eenvoudige syntax en leesbaarheid.",
                        "Simpele, leesbare syntax",
                        "Dynamisch getypeerd",
                        "Enorme ecosysteem van libraries",
                        "Perfect voor data science, machine learning, web development",
                        "Frameworks: Django, Flask, FastAPI"
                );
                break;

            case "javascript":
                details.setContent(
                        "JavaScript ⚡",
                        "⚡",
                        "JavaScript is de programmeertaal van het web, draait in browsers en op servers (Node.js).",
                        "De enige taal die native in browsers draait",
                        "Event-driven en asynchronous",
                        "JSON (JavaScript Object Notation) is gebaseerd op JS",
                        "Gebruikt voor frontend EN backend development",
                        "Frameworks: React, Vue, Angular, Node.js"
                );
                break;

            case "rust":
                details.setContent(
                        "Rust 🦀",
                        "🦀",
                        "Rust is een systeemprogrammeertaal gefocust op veiligheid, snelheid en concurrency.",
                        "Memory safety zonder garbage collector",
                        "Zero-cost abstractions",
                        "Voorkomt data races en null pointer errors",
                        "Gebruikt voor systeem programming, WebAssembly, embedded",
                        "Frameworks: Actix, Rocket, Tokio"
                );
                break;

            default:
                details.setContent(
                        "Onbekende Keuze",
                        "❓",
                        "Er is geen taal geselecteerd.",
                        "Ga terug en maak een keuze"
                );
        }

        // Store in session
        setSessionData("detailComponent", details);
    }

    @Override
    public void render() throws IOException {
        PrintWriter out = getWriter();

        writeHtmlHead(out, "Framework Ultra Light Demo - Step 2");

        out.println("<div class='container'>");
        out.println("  <header>");
        out.println("    <h1>Framework Ultra Light Demo</h1>");
        out.println("  </header>");

        out.println("  <div class='steps-indicator'>");
        out.println("    <span class='step completed'>1. Keuze ✓</span>");
        out.println("    <span class='step active'>2. Details</span>");
        out.println("  </div>");

        // Render the detail component!
        out.println(details.renderHtml());

        out.println("</div>");

        writeHtmlFooter(out);
    }
}
