

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Filter3
 */
@WebServlet("/Filter3")
public class Filter3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Filter3() {
        super();
        // TODO Auto-generated constructor stub
    }
    void generateform(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	PrintWriter out=response.getWriter();
    	HttpSession session=request.getSession();
    	String msg=(String)session.getAttribute("errors");
        System.out.println("error msg= "+msg);

    	out.println("<head><style> h3 { display: inline; } </style><link rel=\"stylesheet\" type=\"text/css\" href=\"style_menu.css\"> </head>");
    	
    	out.println("<div id=\"filter3\"><form action=\"Filter3_submit\" method=\"POST\">");
    	out.println("<p><h3>Application Dated From</h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    	out.println("<input type=\"date\" name=\"fdate_from\"></p><p><h3>Application Dated Upto</h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    	out.println("<input type=\"date\" name=\"fdate_upto\"></p><br><center>");
    	out.println("<input type=\"submit\" value=\"Show Results\"></center><h2>"+msg+"</h2></div>");
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
		HttpSession session=request.getSession(false);
		System.out.println((String)session.getAttribute("xboard"));
        System.out.println((String)session.getAttribute("grad_state"));
        System.out.println((String)session.getAttribute("grad_percent"));
		request.getRequestDispatcher("filter3.html").include(request, response);

		//generateform(request,response);
		return;
	}

}
