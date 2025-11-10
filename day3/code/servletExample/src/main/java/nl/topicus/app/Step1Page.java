package nl.topicus.app;

import nl.topicus.app.components.ChoiceComponent;
import nl.topicus.framework.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Step 1: Choose your favorite programming language
 *
 * This page shows a grid of choices.
 * User clicks a choice → AJAX request → updates session → shows step 2
 */
public class Step1Page extends Page {

    private ChoiceComponent choices;

    public Step1Page(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);

        // Create the choice component
        choices = new ChoiceComponent("language-choices", "Kies je taal:");

        // Add choices
        choices.addChoice("java", "Java", "☕", "OOP en WORA");
        choices.addChoice("python", "Python", "🐍", "Simpel, veelzijdig, fun");
        choices.addChoice("javascript", "JavaScript", "⚡", "De taal van het web, hoofdpijn included");
        choices.addChoice("rust", "Rust", "🦀", "Veilig, snel, concurrent, might be a cult");

        // Store component in session (Wicket-style!)
        setSessionData("choiceComponent", choices);
    }

    @Override
    public void render() throws IOException {
        PrintWriter out = getWriter();

        writeHtmlHead(out, "Framework Ultra Light Demo: Step 1");

        out.println("<div class='container'>");
        out.println("  <header>");
        out.println("    <h1>Framework Ultra Light Demo</h1>");
        out.println("  </header>");
        out.println("  <div class='steps-indicator'>");
        out.println("    <span class='step active'>1. Keuze</span>");
        out.println("    <span class='step'>2. Details</span>");
        out.println("  </div>");

        // Render the component!
        out.println(choices.renderHtml());

        out.println("  <div id='loading' class='loading' style='display: none;'>");
        out.println("    <div class='spinner'></div>");
        out.println("    <p>Loading...</p>");
        out.println("  </div>");

        out.println("</div>");

        writeHtmlFooter(out);
    }
}