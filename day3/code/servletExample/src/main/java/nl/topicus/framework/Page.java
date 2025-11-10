package nl.topicus.framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Base Page class - inspired by Wicket's WebPage
 *
 * Every page in Wicket Ultra Light extends this class.
 * Provides:
 * - Access to request, response, session
 * - Helper methods for common tasks
 * - Lifecycle: render() method
 */
public abstract class Page {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    public Page(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }

    /**
     * Every page must implement render()
     * This method generates the HTML for the page
     */
    public abstract void render() throws IOException;

    /**
     * Render page as JSON (for AJAX requests)
     * Override this if your page supports AJAX updates
     */
    public void renderJson() throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"JSON rendering not implemented\"}");
    }

    // ============================================================================
    // Helper Methods - Make life easier!
    // ============================================================================

    /**
     * Get PrintWriter for writing HTML
     */
    protected PrintWriter getWriter() throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        return response.getWriter();
    }

    /**
     * Store data in session
     */
    protected void setSessionData(String key, Object value) {
        session.setAttribute(key, value);
    }

    /**
     * Get data from session
     */
    protected Object getSessionData(String key) {
        return session.getAttribute(key);
    }

    /**
     * Get data from session with type casting
     */
    @SuppressWarnings("unchecked")
    protected <T> T getSessionData(String key, Class<T> type) {
        return (T) session.getAttribute(key);
    }

    /**
     * Get request parameter
     */
    protected String getParameter(String name) {
        return request.getParameter(name);
    }

    /**
     * Check if this is an AJAX request
     */
    protected boolean isAjaxRequest() {
        String header = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(header);
    }

    /**
     * Redirect to another page
     */
    protected void redirectTo(String url) throws IOException {
        response.sendRedirect(request.getContextPath() + url);
    }

    /**
     * Write HTML head with title and includes
     */
    protected void writeHtmlHead(PrintWriter out, String title) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='nl'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>" + title + "</title>");
        out.println("    <link rel='stylesheet' href='" + request.getContextPath() + "/css/style.css'>");
        out.println("</head>");
        out.println("<body>");
    }

    /**
     * Write HTML footer with script includes
     */
    protected void writeHtmlFooter(PrintWriter out) {
        out.println("    <script src='" + request.getContextPath() + "/js/framework.js'></script>");
        out.println("    <script src='" + request.getContextPath() + "/js/app.js'></script>");
        out.println("</body>");
        out.println("</html>");
    }
}