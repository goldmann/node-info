package pl.goldmann.work.helloworld;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/session")
public class SessionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");

        int timeout = 60;

        String t = req.getParameter("timeout");

        if (t != null) {
            timeout = Integer.valueOf(t);
        }

        HttpSession session = req.getSession(false);
        PrintWriter writer = resp.getWriter();

        if (session == null) {
            session = req.getSession(true);
            session.setMaxInactiveInterval(timeout);
            writer.println("Created new session '" + session.getId() + "' with timeout set to " + timeout + " seconds");
        } else {
            Cookie[] cookies = req.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    // Update the expire time for the cookie
                    if (cookie.getName().equals("JSESSIONID")) {
                        cookie.setMaxAge(timeout);
                        resp.addCookie(cookie);
                    }
                }
            }

            writer.println("Reused session '" + session.getId() + "'");
        }

        writer.println("Current hostname: " + System.getProperty("jboss.host.name"));
        writer.close();
    }
}
