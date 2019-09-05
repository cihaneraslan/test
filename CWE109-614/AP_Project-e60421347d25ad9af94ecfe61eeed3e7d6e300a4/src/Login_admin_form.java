

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login_admin_form
 */
@WebServlet("/Login_admin_form")
public class Login_admin_form extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login_admin_form() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out=response.getWriter();
		//System.out.println(error);
		//request.getRequestDispatcher("Login_Admin.html").include(request, response);
		out.println("<head><style>h3 { display: inline; }</style><link rel=\"stylesheet\" type=\"text/css\" href=\"style_menu.css\"> </head>");
		out.println("<div id=\"admin_login\"><center><h1>WELCOME TO PhD ADMISSION SYSTEM</h1><br><p><h2>ENTER THE DETAILS TO PROCEED</h2></p><br><form action=\"Login_admin\" method=\"POST\"><p><h3>USERNAME</h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"text\" name=\"user_username\"></p><p><h3>PASSWORD</h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"text\" name=\"admin_password\"></p><br><p><input id=\"button\" type=\"submit\" name=\"LogIn_Admin\" value=\"Log In\"></p></form></center></div>");
	}

}
