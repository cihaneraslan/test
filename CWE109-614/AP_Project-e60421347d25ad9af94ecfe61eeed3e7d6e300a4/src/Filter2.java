

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Filter2
 */
@WebServlet("/Filter2")
public class Filter2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Filter2() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    void generateform(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	PrintWriter out=response.getWriter();
    out.println("<head><style> h3 { display: inline; }</style><link rel=\"stylesheet\" type=\"text/css\" href=\"style_menu.css\"> </head>");
	out.println("<div id=\"filter2\"><form action=\"Filter2_submit\" method=\"POST\">");

out.println("<h1><center>Educational Information</center></h1><br><br><p><h3>PhD STREAM</h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<select id=\"fstream\" name=\"fstream\"><option value=\"All\" selected=\"selected\">All</option>");
out.println("	<option value=\"Computer Science\">Computer Science</option>");
  out.println("<option value=\"Electronics and Commmunication Engineering\">Electronics and Commmunication Engineering</option>");
 out.println(" <option value=\"Commutational Biology\">Commutational Biology</option>");
out.println("</select></p>");
out.println("<p><h3>Graduation Degree</h3><span class=\"asteriskField\">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<select id=\"fgrad_degree\" name=\"fgrad_degree\"><option value=\"All\" selected=\"selected\">All</option><option value=\"B.Tech\">B.Tech</option><option value=\"B.Sc\">B.Sc</option>");
out.println("</select></p><p><h3>Post Graduation Degree</h3><span class=\"asteriskField\">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<select id=\"fpg_degree\" name=\"fpg_degree\"><option value=\"All\" selected=\"selected\">All</option><option value=\"M.Tech\">M.Tech</option><option value=\"MS\">MS</option></select></p>");
out.println("<p><h3>Class Xth Board</h3><span class=\"asteriskField\">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<select id=\"fxboard\" name=\"fxboard\"><option value=\"All\" selected=\"selected\">All</option><option value=\"CBSE\">CBSE</option><option value=\"ICSE\">ICSE</option></select></p>");
out.println("<p><h3>Class XIIth Board</h3><span class=\"asteriskField\">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<select id=\"fxiiboard\" name=\"fxiiboard\"><option value=\"All\" selected=\"selected\">All</option><option value=\"CBSE\">CBSE</option><option value=\"ICSE\">ICSE</option></select></p>");
out.println("<p><h3>Department(Graduation)</h3><span class=\"asteriskField\">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<select id=\"fgrad_dep\" name=\"fgrad_dep\"><option value=\"All\" selected=\"selected\">All</option><option value=\"CSE\">CSE</option><option value=\"ECE\">ECE</option><option value=\"IT\">IT</option><option value=\"Software-Engeneering\">Softaware-Engeneeering</option></select></p>");
out.println("<p><h3>Department(Post Graduation)</h3><span class=\"asteriskField\">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<select id=\"fpg_dep\" name=\"fpg_dep\"><option value=\"All\" selected=\"selected\">All</option><option value=\"CSE\">CSE</option><option value=\"ECE\">ECE</option><option value=\"IT\">IT</option><option value=\"Software-Engeneering\">Softaware-Engeneeering</option></select></p>");

out.println("<p><h3>University(Graduation)</h3><span class=\"asteriskField\">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<input type=\"text\" name=\"fgrad_univ\">");
out.println("<p><h3>University(Post Graduation)</h3><span class=\"asteriskField\">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<input type=\"text\" name=\"fpg_univ\"></p>	");		
out.println("<p><h3>State(Graduation)</h3><span class=\"asteriskField\">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<select id=\"fgrad_state\" name=\"fgrad_state\"><option value=\"\" selected=\"selected\">---------</option><option value=\"Andaman and Nicobar Islands\">Andaman and Nicobar Islands</option><option value=\"Andhra Pradesh\">Andhra Pradesh</option><option value=\"Arunachal Pradesh\">Arunachal Pradesh</option><option value=\"Assam\">Assam</option><option value=\"Bihar\">Bihar</option><option value=\"Chandigarh\">Chandigarh</option><option value=\"Chhatisgarh\">Chhatisgarh</option><option value=\"Dadra and Nagar Haveli\">Dadra and Nagar Haveli</option><option value=\"Daman and Diu\">Daman and Diu</option><option value=\"Delhi\">Delhi</option><option value=\"Goa\">Goa</option><option value=\"Gujarat\">Gujarat</option><option value=\"Haryana\">Haryana</option><option value=\"Himachal Pradesh\">Himachal Pradesh</option><option value=\"Jammu and Kashmir\">Jammu and Kashmir</option><option value=\"Jharkhand\">Jharkhand</option><option value=\"Karnataka\">Karnataka</option><option value=\"Kerala\">Kerala</option><option value=\"Lakshadweep\">Lakshadweep</option><option value=\"Madhya Pradesh\">Madhya Pradesh</option><option value=\"Maharashtra\">Maharashtra</option><option value=\"Manipur\">Manipur</option><option value=\"Meghalaya\">Meghalaya</option><option value=\"Mizoram\">Mizoram</option><option value=\"Nagaland\">Nagaland</option><option value=\"Orissa\">Orissa</option><option value=\"Pondicherry\">Pondicherry</option><option value=\"Punjab\">Punjab</option><option value=\"Rajasthan\">Rajasthan</option><option value=\"Sikkim\">Sikkim</option><option value=\"Tamil Nadu\">Tamil Nadu</option><option value=\"Tripura\">Tripura</option><option value=\"Uttaranchal\">Uttaranchal</option><option value=\"Uttar Pradesh\">Uttar Pradesh</option><option value=\"West Bengal\">West Bengal</option><option value=\"Other\">Other</option></select>");
out.println("<p><h3>State(Post Graduation)</h3><span class=\"asteriskField\">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<select id=\"fpg_state\" name=\"fpg_state\"><option value=\"\" selected=\"selected\">---------</option><option value=\"Andaman and Nicobar Islands\">Andaman and Nicobar Islands</option><option value=\"Andhra Pradesh\">Andhra Pradesh</option><option value=\"Arunachal Pradesh\">Arunachal Pradesh</option><option value=\"Assam\">Assam</option><option value=\"Bihar\">Bihar</option><option value=\"Chandigarh\">Chandigarh</option><option value=\"Chhatisgarh\">Chhatisgarh</option><option value=\"Dadra and Nagar Haveli\">Dadra and Nagar Haveli</option><option value=\"Daman and Diu\">Daman and Diu</option><option value=\"Delhi\">Delhi</option><option value=\"Goa\">Goa</option><option value=\"Gujarat\">Gujarat</option><option value=\"Haryana\">Haryana</option><option value=\"Himachal Pradesh\">Himachal Pradesh</option><option value=\"Jammu and Kashmir\">Jammu and Kashmir</option><option value=\"Jharkhand\">Jharkhand</option><option value=\"Karnataka\">Karnataka</option><option value=\"Kerala\">Kerala</option><option value=\"Lakshadweep\">Lakshadweep</option><option value=\"Madhya Pradesh\">Madhya Pradesh</option><option value=\"Maharashtra\">Maharashtra</option><option value=\"Manipur\">Manipur</option><option value=\"Meghalaya\">Meghalaya</option><option value=\"Mizoram\">Mizoram</option><option value=\"Nagaland\">Nagaland</option><option value=\"Orissa\">Orissa</option><option value=\"Pondicherry\">Pondicherry</option><option value=\"Punjab\">Punjab</option><option value=\"Rajasthan\">Rajasthan</option><option value=\"Sikkim\">Sikkim</option><option value=\"Tamil Nadu\">Tamil Nadu</option><option value=\"Tripura\">Tripura</option><option value=\"Uttaranchal\">Uttaranchal</option><option value=\"Uttar Pradesh\">Uttar Pradesh</option><option value=\"West Bengal\">West Bengal</option><option value=\"Other\">Other</option></select>");
out.println("<p><h3>Class X Board Percentage</h3><span class=\"asteriskField\">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<input type=\"checkbox\" name=\"check\" value=\"fx_lt\">Less Than<input type=\"checkbox\" name=\"fx\" value=\"fx_eq\">Equal<input type=\"checkbox\" name=\"fx\" value=\"fx_gt\">Greater Than&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<input type=\"text\" name=\"fx_percent\">");
out.println("<p><h3>Class XII Board Percentage</h3><span class=\"asteriskField\">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<input type=\"checkbox\" name=\"check\" value=\"fxii_lt\">Less Than<input type=\"checkbox\" name=\"fxii\" value=\"fxii_eq\">Equal<input type=\"checkbox\" name=\"fxii\" value=\"fxii_gt\">Greater Than&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<input type=\"text\" name=\"fxii_percent\">");
out.println("<p><h3>Graduation Percentage</h3><span class=\"asteriskField\">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<input type=\"checkbox\" name=\"check\" value=\"fgrad_lt\">Less Than<input type=\"checkbox\" name=\"fgrad\" value=\"fgrad_eq\">Equal<input type=\"checkbox\" name=\"fgrad\" value=\"fgrad_gt\">Greater Than&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<input type=\"text\" name=\"fgrad_percent\">");
out.println("<p><h3>Post Graduation Percentage</h3><span class=\"asteriskField\">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<input type=\"checkbox\" name=\"check\" value=\"fpg_lt\">Less Than<input type=\"checkbox\" name=\"fpg\" value=\"fpg_eq\">Equal<input type=\"checkbox\" name=\"fpg\" value=\"fpg_gt\">Greater Than&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<input type=\"text\" name=\"fpg_percent\">");
out.println("<p><h3>Gate Score</h3><span class=\"asteriskField\">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<input type=\"checkbox\" name=\"check\" value=\"fgate_lt\">Less Than<input type=\"checkbox\" name=\"fgate\" value=\"fgate_eq\">Equal<input type=\"checkbox\" name=\"fgate\" value=\"fgate_gt\">Greater Than&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
out.println("<input type=\"text\" name=\"fgate_score\"><br><center>");
out.println("<p><input type=\"submit\" value=\"Proceed\"></p></center></div>");

    	
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	PrintWriter out=response.getWriter();

		HttpSession session=request.getSession(false);  
        String n=(String)session.getAttribute("name");  
         
        out.print("Hello "+n);
        System.out.println((String)session.getAttribute("name"));
		request.getRequestDispatcher("filter2.html").include(request, response);

		//generateform(request,response);
		return;
	}

}
