package id.jug.spring.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by galih.lasahido@gmail.com
 */
@WebServlet("/session")
public class SessionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {
            String attributeName = req.getParameter("attributeName");
            String attributeValue = req.getParameter("attributeValue");
            req.getSession().setAttribute(attributeName, attributeValue);
            resp.sendRedirect(req.getContextPath() + "/");
    }

    private static final long serialVersionUID = 2878267318695777395L;

}