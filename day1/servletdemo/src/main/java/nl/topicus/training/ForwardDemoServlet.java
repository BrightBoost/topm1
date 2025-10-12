package nl.topicus.training;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/forward-demo")
public class ForwardDemoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("ForwardDemoServlet: Forwarding to /forwarded");

        // FORWARD: Server-side forward, URL blijft hetzelfde
        request.setAttribute("message", "Dit is een forward!");
        request.getRequestDispatcher("/forwarded").forward(request, response);
    }
}
