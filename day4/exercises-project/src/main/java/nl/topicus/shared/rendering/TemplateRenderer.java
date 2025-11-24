package nl.topicus.shared.rendering;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple template renderer that reads HTML files and replaces placeholders.
 * 
 * This demonstrates how Wicket reads HTML templates and processes them.
 * 
 * Placeholder syntax: {{variableName}}
 * 
 * Example:
 *   Template: <h1>Hello {{name}}!</h1>
 *   Data: Map.of("name", "World")
 *   Result: <h1>Hello World!</h1>
 */
public class TemplateRenderer {
    
    private static final String TEMPLATE_BASE_PATH = "/templates/";
    
    /**
     * Render a template with the given data.
     * 
     * This is similar to how Wicket:
     * 1. Reads the HTML file
     * 2. Finds wicket:id attributes
     * 3. Replaces them with component output
     * 
     * @param templatePath Path relative to /templates/ directory
     * @param data Variables to replace in template
     * @return Rendered HTML
     */
    public static String render(String templatePath, Map<String, String> data) {
        try {
            // Read the HTML template
            String html = readTemplate(templatePath);
            
            // Replace all {{placeholders}} with data
            for (Map.Entry<String, String> entry : data.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                String value = entry.getValue() != null ? entry.getValue() : "";
                html = html.replace(placeholder, value);
            }
            
            return html;
        } catch (IOException e) {
            return "<html><body><h1>Error loading template: " + templatePath + "</h1><p>" + 
                   e.getMessage() + "</p></body></html>";
        }
    }
    
    /**
     * Render a template with no data (just return raw HTML).
     */
    public static String render(String templatePath) {
        return render(templatePath, new HashMap<>());
    }
    
    /**
     * Read an HTML template from the classpath.
     * 
     * Templates must be in: src/main/resources/templates/
     * 
     * This is exactly what Wicket does when it loads YourPage.html files.
     * Wicket looks for HTML files on the classpath (in the same package as your Java class).
     * 
     * Why classpath and not webapp?
     * - Classpath resources are packaged in the JAR/WAR
     * - Available to Java code via getResourceAsStream()
     * - Same location as Java class files
     * - This is how Wicket keeps HTML and Java together!
     */
    private static String readTemplate(String templatePath) throws IOException {
        String fullPath = TEMPLATE_BASE_PATH + templatePath;
        
        try (InputStream is = TemplateRenderer.class.getResourceAsStream(fullPath)) {
            if (is == null) {
                throw new IOException("Template not found: " + fullPath);
            }
            
            byte[] bytes = is.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }
    
    /**
     * Convenience builder for creating data maps.
     * 
     * Usage:
     *   TemplateRenderer.render("form.html", 
     *       TemplateRenderer.data()
     *           .put("name", "John")
     *           .put("email", "john@example.com")
     *           .build()
     *   );
     */
    public static DataBuilder data() {
        return new DataBuilder();
    }
    
    public static class DataBuilder {
        private final Map<String, String> data = new HashMap<>();
        
        public DataBuilder put(String key, String value) {
            data.put(key, value);
            return this;
        }
        
        public DataBuilder put(String key, Object value) {
            data.put(key, value != null ? value.toString() : "");
            return this;
        }
        
        public Map<String, String> build() {
            return data;
        }
    }
}
