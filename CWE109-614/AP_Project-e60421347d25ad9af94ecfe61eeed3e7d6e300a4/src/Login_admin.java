

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.scribe.builder.ServiceBuilder;
import org.scribe.oauth.OAuthService;

/**
 * Servlet implementation class Login_admin
 */
@WebServlet("/Login_admin")
public class Login_admin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CLIENT_ID = "508507827379-gf3h608b2rbuafovsp2sn43bhgaa3e9b.apps.googleusercontent.com"; 
	  private static final String CLIENT_SECRET = "MJIjg18oByfpwjuN8ZxXbll3";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login_admin() {
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
		System.out.println("Login_admin");
		// TODO Auto-generated method stub
		ServiceBuilder builder= new ServiceBuilder(); 
	      OAuthService service = builder.provider(Google2Api.class) 
	         .apiKey(CLIENT_ID) 
	         .apiSecret(CLIENT_SECRET) 
	         .callback("http://localhost/project2/oauthcallback") 
	         .scope("openid email " + 
	               "https://www.googleapis.com/auth/plus.login " + 
	               "https://www.googleapis.com/auth/plus.me")  
	         .debug() 
	         .build(); //Now build the call
	      HttpSession sess = request.getSession(); 
	      sess.setAttribute("oauth2Service", service);
	      response.sendRedirect(service.getAuthorizationUrl(null)); 
	}

}
