

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Form1_submit
 */
@WebServlet("/Form1_submit")
public class Form1_submit extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Form1_submit() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Form1_submit");
		Cookie cemail=new Cookie("email",(String) request.getSession().getAttribute("email"));
		Cookie cname=new Cookie("name",(String) request.getSession().getAttribute("name"));
		Cookie cenrollno=new Cookie("enrollno",(String)request.getSession().getAttribute("enrollno"));
		Cookie ccorr_address=new Cookie("corr_address",request.getParameter("corr_address"));
		Cookie cmobile=new Cookie("mobile",request.getParameter("mobile"));
		Cookie cphd=new Cookie("phd",request.getParameter("phd"));
		Cookie cphd1=new Cookie("phd1",request.getParameter("phd1"));
		Cookie cphd2=new Cookie("phd2",request.getParameter("phd2"));
		Cookie cphd3=new Cookie("phd3",request.getParameter("phd3"));
		Cookie cgender=new Cookie("gender",request.getParameter("gender"));
		Cookie ccategory=new Cookie("category",request.getParameter("category"));
		Cookie cphydis=new Cookie("phydis",request.getParameter("phydis"));
		Cookie cdob=new Cookie("dob",request.getParameter("dob"));
		Cookie cdefence=new Cookie("defence",request.getParameter("defence"));
		Cookie cfather=new Cookie("father",request.getParameter("father"));
		cfather.setSecure(true);
		Cookie cnationality=new Cookie("nationality",request.getParameter("nationality"));
		Cookie cperm_address=new Cookie("perm_address",request.getParameter("perm_address"));
		Cookie cpin=new Cookie("pin",request.getParameter("pin"));
		response.addCookie(cemail);response.addCookie(cname);response.addCookie(cenrollno);response.addCookie(ccorr_address);response.addCookie(cmobile);response.addCookie(cphd);response.addCookie(cphd1);response.addCookie(cphd2);response.addCookie(cphd3);response.addCookie(cgender);response.addCookie(ccategory);response.addCookie(cdob);response.addCookie(cperm_address);response.addCookie(cpin);response.addCookie(cfather);response.addCookie(cnationality);response.addCookie(cdefence);response.addCookie(cphydis);
//		System.out.println(cemail.getValue());
//		System.out.println(cname.getValue());
//		System.out.println(cenrollno.getValue());
//		System.out.println(ccorr_address.getValue());
//		System.out.println(cmobile.getValue());
//		System.out.println(cphd.getValue());
//		System.out.println(cphd1.getValue());System.out.println(cphd2.getValue());System.out.println(cphd3.getValue());
//		System.out.println(cgender.getValue());
//		System.out.println(ccategory.getValue());
//		System.out.println(cphydis.getValue());
//		System.out.println(cdob.getValue());
//		System.out.println(cdefence.getValue());
//		System.out.println(cfather.getValue());
//		System.out.println(cnationality.getValue());
//		System.out.println(cperm_address.getValue());
//		System.out.println(cpin.getValue());
		request.getRequestDispatcher("Form2").forward(request, response);
	}

}
