

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Filter1_submit
 */
@WebServlet("/Filter1_submit")
public class Filter1_submit extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Filter1_submit() {
        super();
        // TODO Auto-generated constructor stub
    }

    void getdetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
    	//Cookie cemail,cname,cenrollno,ccorr_address,cmobile,cphd,cphd1,cphd2,cphd3,cgender,ccategory,cphydis,cdob,cdefence,cfather,cnationality,cperm_address,cpin;
    	HttpSession session=request.getSession();
    	if(request.getParameter("fname").length()!=0)
    		session.setAttribute("name",request.getParameter("fname"));
        else
        	session.setAttribute("name","\0");
        if(request.getParameter("femail").length()!=0)
        	session.setAttribute("email",request.getParameter("femail"));
        else
        	session.setAttribute("email","\0");
        System.out.println(request.getParameter("fenrollno"));
        if(request.getParameter("fenroll_no")!=null)
        	session.setAttribute("enrollno",request.getParameter("fenroll_no"));
       else
           session.setAttribute("enrollno","\0");

        System.out.println("Enroll no = "+request.getParameter("fenroll_no"));
        session.setAttribute("gender",request.getParameter("fgender"));
        session.setAttribute("category",request.getParameter("fcategory"));
        session.setAttribute("pd",request.getParameter("fpd"));
        session.setAttribute("dob",request.getParameter("fdob"));
        session.setAttribute("date",request.getParameter("fdate"));
		

		
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
		response.setContentType("text/html");
		getdetails(request,response);
		System.out.println("in filter1_submit");
		int v=1;
		//v=validate(request,response);
		request.getRequestDispatcher("Filter2").forward(request, response);


		


	}

}
