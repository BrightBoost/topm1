package nl.topicus.shared.rendering;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for building HTML programmatically.
 * 
 * This demonstrates how Wicket components build HTML:
 * - Label → <span>text</span>
 * - TextField → <input type="text">
 * - Form → <form>...</form>
 * 
 * Example:
 *   HtmlBuilder.form("/order")
 *       .add(HtmlBuilder.input("text", "name", ""))
 *       .add(HtmlBuilder.button("Submit"))
 *       .build();
 */
public class HtmlBuilder {
    
    /**
     * Build an HTML form.
     */
    public static FormBuilder form(String action) {
        return new FormBuilder(action);
    }
    
    /**
     * Build an input field.
     */
    public static String input(String type, String name, String value) {
        return String.format("<input type=\"%s\" name=\"%s\" value=\"%s\">", 
                           escape(type), escape(name), escape(value));
    }
    
    /**
     * Build a labeled input field.
     */
    public static String labeledInput(String label, String type, String name, String value) {
        return String.format(
            "<div class=\"form-group\">" +
            "<label for=\"%s\">%s</label>" +
            "<input type=\"%s\" id=\"%s\" name=\"%s\" value=\"%s\">" +
            "</div>",
            escape(name), escape(label), escape(type), 
            escape(name), escape(name), escape(value)
        );
    }
    
    /**
     * Build a button.
     */
    public static String button(String text) {
        return String.format("<button type=\"submit\">%s</button>", escape(text));
    }
    
    /**
     * Build a list of errors.
     */
    public static String errorList(List<String> errors) {
        if (errors == null || errors.isEmpty()) {
            return "";
        }
        
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"error-box\">");
        html.append("<h3>Please correct the following errors:</h3>");
        html.append("<ul>");
        for (String error : errors) {
            html.append("<li>").append(escape(error)).append("</li>");
        }
        html.append("</ul>");
        html.append("</div>");
        return html.toString();
    }
    
    /**
     * Build a success message.
     */
    public static String successBox(String message) {
        return String.format(
            "<div class=\"success-box\"><strong>Success!</strong> %s</div>",
            escape(message)
        );
    }
    
    /**
     * Build a div with class and content.
     */
    public static String div(String className, String content) {
        return String.format("<div class=\"%s\">%s</div>", escape(className), content);
    }
    
    /**
     * Escape HTML to prevent XSS attacks.
     * This is what Wicket does automatically!
     */
    public static String escape(String text) {
        if (text == null) return "";
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;");
    }
    
    /**
     * Builder for HTML forms.
     */
    public static class FormBuilder {
        private final String action;
        private final List<String> elements = new ArrayList<>();
        private String method = "post";
        
        public FormBuilder(String action) {
            this.action = action;
        }
        
        public FormBuilder method(String method) {
            this.method = method;
            return this;
        }
        
        public FormBuilder add(String html) {
            elements.add(html);
            return this;
        }
        
        public String build() {
            StringBuilder html = new StringBuilder();
            html.append("<form action=\"").append(escape(action))
                .append("\" method=\"").append(escape(method)).append("\">");
            
            for (String element : elements) {
                html.append(element);
            }
            
            html.append("</form>");
            return html.toString();
        }
    }
}
