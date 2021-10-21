package mate.controller.driver;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mate.exception.AuthenticationException;
import mate.lib.Injector;
import mate.model.Driver;
import mate.service.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController extends HttpServlet {
    private static Logger logger = LogManager.getLogger(LoginController.class);
    private static final String SESSION_ATTRIBUTE_ID = "driver_id";
    private static final Injector injector = Injector.getInstance("mate");
    private final AuthenticationService authenticationService = (AuthenticationService) injector
            .getInstance(AuthenticationService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/drivers/login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        HttpSession session = req.getSession();
        try {
            Driver driver = authenticationService.login(login, password);
            session.setAttribute(SESSION_ATTRIBUTE_ID, driver.getId());
            logger.info("SessionId: " + session.getId()
                    + "; Driver successfully logged. Driver Id: " + driver.getId());
            resp.sendRedirect("/index");
        } catch (AuthenticationException e) {
            logger.error("SessionId: " + session.getId()
                    + "; Driver with login and password not found");
            req.setAttribute("errorMsg",e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/drivers/login.jsp").forward(req,resp);
        }
    }
}
