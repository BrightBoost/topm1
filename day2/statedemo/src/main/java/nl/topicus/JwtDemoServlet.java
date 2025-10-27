package nl.topicus;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * JWT Demo: Token-Based Authentication
 *
 * Demonstrates:
 * 1. Creating JWT tokens (server-side)
 * 2. Storing tokens (in cookies or localStorage)
 * 3. Validating tokens
 * 4. Extracting claims from tokens
 * 5. Token expiration
 * 6. Difference between JWT and Sessions
 *
 * Use case: Stateless authentication for APIs
 */
@WebServlet("/jwt-demo")
public class JwtDemoServlet extends HttpServlet {

    // Secret key for signing tokens (in production: use environment variable!)
    private static final String SECRET_KEY = "myVerySecretKeyForDemoOnlyDoNotUseInProduction12345";

    // Token expiration: 5 minutes (for demo purposes)
    private static final long EXPIRATION_MS = 5 * 60 * 1000; // 5 minutes

    // Hardcoded users for demo
    private static final Map<String, String> USERS = Map.of(
            "admin", "admin123",
            "user", "password",
            "maaike", "geheim"
    );

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Try to get JWT token from cookie
        String token = getTokenFromCookie(request);

        // Try to validate token and extract user info
        Claims claims = null;
        String username = null;
        String role = null;
        boolean tokenValid = false;
        String tokenError = null;

        if (token != null) {
            try {
                claims = validateToken(token);
                username = claims.getSubject();
                role = claims.get("role", String.class);
                tokenValid = true;
            } catch (ExpiredJwtException e) {
                tokenError = "Token expired at " + e.getClaims().getExpiration();
            } catch (SignatureException e) {
                tokenError = "Invalid token signature (token was tampered with)";
            } catch (Exception e) {
                tokenError = "Invalid token: " + e.getMessage();
            }
        }

        // Generate HTML response
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <title>JWT Demo</title>");
        out.println("    <style>");
        out.println("        * { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("        body {");
        out.println("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
        out.println("            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("            min-height: 100vh;");
        out.println("            padding: 40px 20px;");
        out.println("        }");
        out.println("        .container { max-width: 1000px; margin: 0 auto; }");
        out.println("        .card {");
        out.println("            background: white;");
        out.println("            border-radius: 15px;");
        out.println("            padding: 30px;");
        out.println("            margin-bottom: 20px;");
        out.println("            box-shadow: 0 10px 30px rgba(0,0,0,0.2);");
        out.println("        }");
        out.println("        .header {");
        out.println("            text-align: center;");
        out.println("            color: white;");
        out.println("            margin-bottom: 30px;");
        out.println("        }");
        out.println("        .header h1 {");
        out.println("            font-size: 42px;");
        out.println("            margin-bottom: 10px;");
        out.println("            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);");
        out.println("        }");
        out.println("        .status-box {");
        out.println("            padding: 20px;");
        out.println("            border-radius: 10px;");
        out.println("            margin-bottom: 20px;");
        out.println("        }");
        out.println("        .status-authenticated {");
        out.println("            background: linear-gradient(135deg, #4CAF50 0%, #45a049 100%);");
        out.println("            color: white;");
        out.println("        }");
        out.println("        .status-unauthenticated {");
        out.println("            background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);");
        out.println("            color: white;");
        out.println("        }");
        out.println("        .status-error {");
        out.println("            background: linear-gradient(135deg, #f44336 0%, #d32f2f 100%);");
        out.println("            color: white;");
        out.println("        }");
        out.println("        .status-icon { font-size: 48px; margin-bottom: 10px; }");
        out.println("        .status-title { font-size: 24px; font-weight: bold; margin-bottom: 10px; }");
        out.println("        .user-info {");
        out.println("            background: rgba(255,255,255,0.2);");
        out.println("            padding: 15px;");
        out.println("            border-radius: 8px;");
        out.println("            margin-top: 15px;");
        out.println("        }");
        out.println("        .user-info p { margin: 8px 0; }");
        out.println("        .form-group { margin-bottom: 20px; }");
        out.println("        label {");
        out.println("            display: block;");
        out.println("            margin-bottom: 8px;");
        out.println("            color: #555;");
        out.println("            font-weight: 500;");
        out.println("        }");
        out.println("        input[type='text'], input[type='password'] {");
        out.println("            width: 100%;");
        out.println("            padding: 12px;");
        out.println("            border: 2px solid #ddd;");
        out.println("            border-radius: 8px;");
        out.println("            font-size: 16px;");
        out.println("        }");
        out.println("        input:focus {");
        out.println("            outline: none;");
        out.println("            border-color: #667eea;");
        out.println("        }");
        out.println("        button {");
        out.println("            padding: 12px 30px;");
        out.println("            font-size: 16px;");
        out.println("            border: none;");
        out.println("            border-radius: 8px;");
        out.println("            cursor: pointer;");
        out.println("            font-weight: bold;");
        out.println("            transition: all 0.3s;");
        out.println("        }");
        out.println("        .login-btn {");
        out.println("            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("            color: white;");
        out.println("        }");
        out.println("        .logout-btn {");
        out.println("            background-color: #f44336;");
        out.println("            color: white;");
        out.println("        }");
        out.println("        .decode-btn {");
        out.println("            background-color: #2196F3;");
        out.println("            color: white;");
        out.println("            margin-top: 10px;");
        out.println("        }");
        out.println("        .token-box {");
        out.println("            background-color: #f8f9fa;");
        out.println("            padding: 20px;");
        out.println("            border-radius: 10px;");
        out.println("            border-left: 4px solid #667eea;");
        out.println("            margin: 20px 0;");
        out.println("        }");
        out.println("        .token-box h3 { margin-bottom: 15px; color: #333; }");
        out.println("        .token-content {");
        out.println("            background: white;");
        out.println("            padding: 15px;");
        out.println("            border-radius: 5px;");
        out.println("            font-family: 'Courier New', monospace;");
        out.println("            font-size: 12px;");
        out.println("            word-break: break-all;");
        out.println("            max-height: 150px;");
        out.println("            overflow-y: auto;");
        out.println("            border: 1px solid #ddd;");
        out.println("        }");
        out.println("        .token-parts {");
        out.println("            display: grid;");
        out.println("            grid-template-columns: repeat(3, 1fr);");
        out.println("            gap: 10px;");
        out.println("            margin-top: 15px;");
        out.println("        }");
        out.println("        .token-part {");
        out.println("            background: white;");
        out.println("            padding: 10px;");
        out.println("            border-radius: 5px;");
        out.println("            border: 2px solid #ddd;");
        out.println("        }");
        out.println("        .token-part h4 {");
        out.println("            font-size: 12px;");
        out.println("            color: #666;");
        out.println("            margin-bottom: 5px;");
        out.println("        }");
        out.println("        .token-part code {");
        out.println("            font-size: 10px;");
        out.println("            word-break: break-all;");
        out.println("        }");
        out.println("        .comparison {");
        out.println("            display: grid;");
        out.println("            grid-template-columns: 1fr 1fr;");
        out.println("            gap: 20px;");
        out.println("            margin-top: 20px;");
        out.println("        }");
        out.println("        .comparison-box {");
        out.println("            background: #f8f9fa;");
        out.println("            padding: 20px;");
        out.println("            border-radius: 10px;");
        out.println("        }");
        out.println("        .comparison-box h3 { color: #333; margin-bottom: 15px; }");
        out.println("        .comparison-box ul { margin-left: 20px; }");
        out.println("        .comparison-box li { margin: 8px 0; color: #555; }");
        out.println("        .explanation {");
        out.println("            background-color: #e3f2fd;");
        out.println("            border-left: 4px solid #2196F3;");
        out.println("            padding: 20px;");
        out.println("            border-radius: 10px;");
        out.println("            margin-top: 20px;");
        out.println("        }");
        out.println("        .explanation h3 { color: #1976d2; margin-bottom: 10px; }");
        out.println("        .explanation ul { margin-left: 20px; color: #1976d2; }");
        out.println("        .explanation li { margin: 8px 0; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");

        out.println("    <div class='container'>");

        // Header
        out.println("        <div class='header'>");
        out.println("            <h1>JWT Authentication Demo</h1>");
        out.println("            <p>JSON Web Tokens: Stateless Authentication</p>");
        out.println("        </div>");

        // Status Card
        out.println("        <div class='card'>");

        if (tokenValid && username != null) {
            // Authenticated
            out.println("            <div class='status-box status-authenticated'>");
            out.println("                <div class='status-title'>Authenticated</div>");
            out.println("                <p>You are logged in with a valid JWT token</p>");
            out.println("                <div class='user-info'>");
            out.println("                    <p><strong>Username:</strong> " + escapeHtml(username) + "</p>");
            out.println("                    <p><strong>Role:</strong> " + escapeHtml(role) + "</p>");

            Date expiration = claims.getExpiration();
            long secondsUntilExpiry = (expiration.getTime() - System.currentTimeMillis()) / 1000;
            out.println("                    <p><strong>Token expires in:</strong> " + secondsUntilExpiry + " seconds</p>");
            out.println("                    <p><strong>Issued at:</strong> " +
                    LocalDateTime.ofInstant(claims.getIssuedAt().toInstant(), ZoneId.systemDefault()) + "</p>");

            out.println("                </div>");
            out.println("                <form method='POST' style='margin-top: 20px;'>");
            out.println("                    <input type='hidden' name='action' value='logout' />");
            out.println("                    <button type='submit' class='logout-btn'>Logout</button>");
            out.println("                </form>");
            out.println("            </div>");

        } else if (tokenError != null) {
            // Token invalid/expired
            out.println("            <div class='status-box status-error'>");
            out.println("                <div class='status-title'>Token Error</div>");
            out.println("                <p>" + escapeHtml(tokenError) + "</p>");
            out.println("                <p style='margin-top: 10px; font-size: 14px;'>Please login again to get a new token</p>");
            out.println("            </div>");

        } else {
            // Not authenticated
            out.println("            <div class='status-box status-unauthenticated'>");
            out.println("                <div class='status-title'>Not Authenticated</div>");
            out.println("                <p>No valid JWT token found. Please login below.</p>");
            out.println("            </div>");
        }

        out.println("        </div>");

        // Login Form (if not authenticated)
        if (!tokenValid) {
            out.println("        <div class='card'>");
            out.println("            <h2>Login</h2>");
            out.println("            <form method='POST'>");
            out.println("                <input type='hidden' name='action' value='login' />");
            out.println("                <div class='form-group'>");
            out.println("                    <label for='username'>Username:</label>");
            out.println("                    <input type='text' id='username' name='username' required />");
            out.println("                    <small style='color: #666;'>Try: admin, user, or maaike</small>");
            out.println("                </div>");
            out.println("                <div class='form-group'>");
            out.println("                    <label for='password'>Password:</label>");
            out.println("                    <input type='password' id='password' name='password' required />");
            out.println("                    <small style='color: #666;'>Passwords: admin123, password, geheim</small>");
            out.println("                </div>");
            out.println("                <button type='submit' class='login-btn'>Login & Get JWT</button>");
            out.println("            </form>");
            out.println("        </div>");
        }

        // Token Display
        if (token != null) {
            out.println("        <div class='card'>");
            out.println("            <div class='token-box'>");
            out.println("                <h3>Your JWT Token</h3>");
            out.println("                <div class='token-content'>" + escapeHtml(token) + "</div>");

            // Split token into parts
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                out.println("                <div class='token-parts'>");
                out.println("                    <div class='token-part' style='border-color: #ff9800;'>");
                out.println("                        <h4>HEADER</h4>");
                out.println("                        <code>" + escapeHtml(parts[0]) + "</code>");
                out.println("                    </div>");
                out.println("                    <div class='token-part' style='border-color: #9c27b0;'>");
                out.println("                        <h4>PAYLOAD</h4>");
                out.println("                        <code>" + escapeHtml(parts[1]) + "</code>");
                out.println("                    </div>");
                out.println("                    <div class='token-part' style='border-color: #00bcd4;'>");
                out.println("                        <h4>SIGNATURE</h4>");
                out.println("                        <code>" + escapeHtml(parts[2]) + "</code>");
                out.println("                    </div>");
                out.println("                </div>");
            }

            out.println("                <p style='margin-top: 15px; font-size: 14px; color: #666;'>");
            out.println("                    💡 <strong>Try this:</strong> Copy the token and decode it at <a href='https://jwt.io' target='_blank'>jwt.io</a>");
            out.println("                </p>");
            out.println("            </div>");
            out.println("        </div>");
        }

        // Comparison: JWT vs Sessions
        out.println("        <div class='card'>");
        out.println("            <h2>JWT vs Sessions</h2>");
        out.println("            <div class='comparison'>");

        out.println("                <div class='comparison-box' style='border-left: 4px solid #667eea;'>");
        out.println("                    <h3>Sessions</h3>");
        out.println("                    <ul>");
        out.println("                        <li><strong>Storage:</strong> Server memory</li>");
        out.println("                        <li><strong>Browser has:</strong> Session ID only</li>");
        out.println("                        <li><strong>Stateful:</strong> Server must store session data</li>");
        out.println("                        <li><strong>Revocation:</strong> Easy (delete session)</li>");
        out.println("                        <li><strong>Scaling:</strong> Harder (shared storage needed)</li>");
        out.println("                        <li><strong>Best for:</strong> Traditional web apps</li>");
        out.println("                    </ul>");
        out.println("                </div>");

        out.println("                <div class='comparison-box' style='border-left: 4px solid #4CAF50;'>");
        out.println("                    <h3>JWT</h3>");
        out.println("                    <ul>");
        out.println("                        <li><strong>Storage:</strong> Client-side (cookie/localStorage)</li>");
        out.println("                        <li><strong>Browser has:</strong> Complete token with data</li>");
        out.println("                        <li><strong>Stateless:</strong> No server storage needed</li>");
        out.println("                        <li><strong>Revocation:</strong> Hard (valid until expiry)</li>");
        out.println("                        <li><strong>Scaling:</strong> Easy (no shared state)</li>");
        out.println("                        <li><strong>Best for:</strong> APIs, microservices</li>");
        out.println("                    </ul>");
        out.println("                </div>");

        out.println("            </div>");
        out.println("        </div>");

        // Explanation
        out.println("        <div class='card'>");
        out.println("            <div class='explanation'>");
        out.println("                <h3>How JWT Works</h3>");
        out.println("                <ol style='margin-left: 20px; color: #1976d2;'>");
        out.println("                    <li style='margin: 12px 0;'><strong>User logs in</strong> with username/password</li>");
        out.println("                    <li style='margin: 12px 0;'><strong>Server validates</strong> credentials</li>");
        out.println("                    <li style='margin: 12px 0;'><strong>Server creates JWT</strong> containing user info (username, role, expiry)</li>");
        out.println("                    <li style='margin: 12px 0;'><strong>Server signs JWT</strong> with secret key (prevents tampering)</li>");
        out.println("                    <li style='margin: 12px 0;'><strong>Server sends JWT</strong> to browser (in cookie)</li>");
        out.println("                    <li style='margin: 12px 0;'><strong>Browser stores JWT</strong> and sends it with every request</li>");
        out.println("                    <li style='margin: 12px 0;'><strong>Server validates signature</strong> and reads user info from token</li>");
        out.println("                    <li style='margin: 12px 0;'><strong>No database lookup needed!</strong> (stateless)</li>");
        out.println("                </ol>");
        out.println("                <p style='margin-top: 20px; padding: 15px; background: rgba(255,255,255,0.5); border-radius: 5px;'>");
        out.println("                    <strong>Important:</strong> JWT contents are NOT encrypted, just Base64 encoded. Anyone can decode and READ the token. ");
        out.println("                    But they cannot MODIFY it without breaking the signature. Never put passwords in JWT!");
        out.println("                </p>");
        out.println("            </div>");
        out.println("        </div>");

        out.println("    </div>");

        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("login".equals(action)) {
            // LOGIN: Validate credentials and create JWT
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // Validate credentials
            if (username != null && USERS.containsKey(username) &&
                    USERS.get(username).equals(password)) {

                // Credentials valid! Create JWT token
                String role = "admin".equals(username) ? "admin" : "user";
                String token = createToken(username, role);

                // Store token in cookie
                Cookie tokenCookie = new Cookie("jwt_token", token);
                tokenCookie.setMaxAge((int) (EXPIRATION_MS / 1000)); // Same as token expiry
                tokenCookie.setPath("/");
                tokenCookie.setHttpOnly(true); // Security!
                response.addCookie(tokenCookie);

                System.out.println("JWT created for user: " + username);
                System.out.println("Token: " + token);

            } else {
                System.out.println("Login failed for user: " + username);
            }

        } else if ("logout".equals(action)) {
            // LOGOUT: Delete token cookie
            Cookie tokenCookie = new Cookie("jwt_token", "");
            tokenCookie.setMaxAge(0); // Delete immediately
            tokenCookie.setPath("/");
            response.addCookie(tokenCookie);

            System.out.println("User logged out - token deleted");
        }

        // Redirect to GET
        response.sendRedirect("/jwt-demo");
    }

    /**
     * Create a JWT token with user information
     */
    private String createToken(String username, String role) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiry = now.plusMinutes(5); // Token valid for 5 minutes

        return Jwts.builder()
                .setSubject(username)                               // Username (standard claim)
                .claim("role", role)                                // Custom claim: role
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))       // When created
                .setExpiration(Date.from(expiry.atZone(ZoneId.systemDefault()).toInstant()))  // When expires
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)    // Sign with secret
                .compact();                                          // Build token string
    }

    /**
     * Validate a JWT token and extract claims
     */
    private Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)              // Use same secret key
                .build()
                .parseSignedClaims(token)                  // Parse and validate
                .getBody();                             // Get claims (user info)
    }

    /**
     * Get JWT token from cookie
     */
    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Escape HTML to prevent XSS
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}