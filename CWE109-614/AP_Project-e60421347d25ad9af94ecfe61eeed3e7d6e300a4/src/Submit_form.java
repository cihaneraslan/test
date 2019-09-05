

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Submit_form
 */
@WebServlet("/Submit_form")
public class Submit_form extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Submit_form() {
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
		System.out.println("Submit_form");
		// TODO Auto-generated method stub
		//try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		PrintWriter out=response.getWriter();
		Cookie[] ck=request.getCookies();
		Candidate c=new Candidate();
		for (Cookie cookie: ck) {
			String cookieName = cookie.getName();
			String cookieValue = cookie.getValue();
			System.out.println("cookie  "+cookieName+" "+cookieValue);
			out.println(cookieName+" "+cookieValue);
			if(cookieName.equals("email"))
				c.Email=cookieValue;
			if(cookieName.equals("name"))
				c.Name=cookieValue;
			if(cookieName.equals("enrollno"))
				c.Enrollno=Integer.parseInt(cookieValue);
			if(cookieName.equals("corr_address"))
				c.Corr_Address=cookieValue;
			if(cookieName.equals("mobile"))
				c.Mobile=Long.parseLong(cookieValue);
			if(cookieName.equals("phd"))
				c.Stream=cookieValue;
			if(cookieName.equals("phd1"))
				c.Area1=cookieValue;
			if(cookieName.equals("phd2"))
				c.Area2=cookieValue;
			if(cookieName.equals("phd3"))
				c.Area3=cookieValue;
			if(cookieName.equals("gender"))
				c.Gender=cookieValue;
			if(cookieName.equals("category"))
				c.Category=cookieValue;
	    	if(cookieName.equals("phydis")){
	    		if(cookieValue.equals("Yes"))
	    			c.Handicap=true;
	    		if(cookieValue.equals("No"))
	    			c.Handicap=false;
	    	}
	    	if(cookieName.equals("dob"))
	    		c.DOB= LocalDate.parse(cookieValue, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	    	if(cookieName.equals("defence")){
	    		if(cookieValue.equals("Yes"))
	    			c.Defense=true;
	    		if(cookieValue.equals("No"))
	    			c.Defense=false;
	    		}
	    	if(cookieName.equals("father"))
	    		c.FName=cookieValue;
	    	if(cookieName.equals("nationality"))
	    		c.Nationality=cookieValue;
	    	if(cookieName.equals("perm_address"))
	    		c.Perm_Address=cookieValue;
	    	if(cookieName.equals("pin"))
	    		c.PIN=Integer.parseInt(cookieValue);
	    	if(cookieName.equals("XBoard"))
	    		c.XBoard=cookieValue;
	    	if(cookieName.equals("XMarks"))
	    		c.XMarks=Float.parseFloat(cookieValue);
	    	if(cookieName.equals("XYear"))
	    		c.XYear=Integer.parseInt(cookieValue);
	    	if(cookieName.equals("XIIBoard"))
	    		c.XIIBoard=cookieValue;
	    	if(cookieName.equals("XIIMarks"))
	    		c.XIIMarks=Float.parseFloat(cookieValue);
	    	if(cookieName.equals("XIIYear"))
	    		c.XIIYear=Integer.parseInt(cookieValue);
	    	if(cookieName.equals("degree"))
	    		c.Degree=cookieValue;
	    	if(cookieName.equals("dept"))
	    		c.Department=cookieValue;
	    	if(cookieName.equals("college"))
	    		c.College=cookieValue;
	    	if(cookieName.equals("university"))
	    		c.University=cookieValue;
	    	if(cookieName.equals("city"))
	    		c.City=cookieValue;
	    	if(cookieName.equals("state"))
	    		c.State=cookieValue;
	    	if(cookieName.equals("grad_year"))
	    		c.GradYear=Integer.parseInt(cookieValue);
	    	if(cookieName.equals("corm") && cookieValue.equals("cgpa"))
	    			c.cgpaormarks=true;
	    	if(cookieName.equals("cgpa"))
	    		if(cookieValue.length()!=0)
	    			c.CGPA=Float.parseFloat(cookieValue);
	    	if(cookieName.equals("total_cgpa"))
	    		c.TotalCGPA=Integer.parseInt(cookieValue);
	    	if(cookieName.equals("corm") && cookieValue.equals("marks"))
	    		{
	    			c.cgpaormarks=false;
	    		}
	    	if(cookieName.equals("marks"))
	    		if(cookieValue.length()!=0)
	    				c.Marks=Float.parseFloat(cookieValue);
	    	if(cookieName.equals("pg_pref1"))
	    		c.Area1=cookieValue;
	    	if(cookieName.equals("pg_pref2"))
	    		c.Area2=cookieValue;
	    	if(cookieName.equals("pg_pref3"))
	    		c.Area3=cookieValue;
	    	if(cookieName.equals("opt_checkbox1")){
	    		if(cookieValue.equals("Yes"))
	    			c.ApplyECE=true;
	    		else
	    			c.ApplyECE=false;
	    		}
	    	if(cookieName.equals("opt_checkbox2")){
	    		if(cookieValue.equals("Yes"))
	    			c.PGDone=true;
	    		else
	    			c.PGDone=false;
	    		}
	    	if(cookieName.equals("opt_checkbox3")){
	    		if(cookieValue.equals("Yes"))
	    			c.OtherDegree=true;
	    		else
	    			c.OtherDegree=false;
	    		}
	    	if(cookieName.equals("opt_checkbox4")){
	    		if(cookieValue.equals("Yes"))
	    			c.TakenGATE=true;
	    		else
	    			c.TakenGATE=false;
	    		}
	    	if(cookieName.equals("pg_degree"))
	    		c.PGDegree=cookieValue;
	    	if(cookieName.equals("pg_department"))
	    		c.PGDepartment=cookieValue;
	    	if(cookieName.equals("pg_college"))
	    		c.PGCollege=cookieValue;
	    	if(cookieName.equals("pg_city"))
	    		c.PGCity=cookieValue;
	    	if(cookieName.equals("pg_state"))
	    		c.PGState=cookieValue;
	    	if(cookieName.equals("pg_grad_year"))
	    		if(cookieValue.length()!=0)
	    		c.PGYear=Integer.parseInt(cookieValue);
	    	if(cookieName.equals("pg_corm") && cookieValue.equals("cgpa"))
	    			c.pgcgpaormarks=true;
	    	if(cookieName.equals("pg_cgpa"))
	    		if(cookieValue.length()!=0)
	    			c.PGCGPA=Float.parseFloat(cookieValue);
	    	if(cookieName.equals("pg_totcgpa"))
	    		if(cookieValue.length()!=0)
	    		c.PGTotalCGPA=Integer.parseInt(cookieValue);
	    	if(cookieName.equals("pg_corm") && cookieValue.equals("marks"))
	    			c.pgcgpaormarks=false;
	    	if(cookieName.equals("pg_marks"))
	    		if(cookieValue.length()!=0)
	    				c.PGMarks=Float.parseFloat(cookieValue);
	    	if(cookieName.equals("other_exam"))
	    		c.OtherExam=cookieValue;
	    	if(cookieName.equals("other_subject"))
	    		c.OtherSubject=cookieValue;
	    	if(cookieName.equals("other_year"))
	    		c.OtherYear=cookieValue;
	    	if(cookieName.equals("other_score"))
	    		if(cookieValue.length()!=0)
	    			c.OtherScore=Float.parseFloat(cookieValue);
	    	if(cookieName.equals("other_rank"))
	    		if(cookieValue.length()!=0)
	    			c.OtherRank=Integer.parseInt(cookieValue);
	    	if(cookieName.equals("gate_area"))
	    		c.GateArea=cookieValue;
	    	if(cookieName.equals("gate_year"))
	    		if(cookieValue.length()!=0)
	    		c.GateYear=Integer.parseInt(cookieValue);
	    	if(cookieName.equals("gate_marks"))
	    		if(cookieValue.length()!=0)
	    		c.GateMarks=Float.parseFloat(cookieValue);
	    	if(cookieName.equals("gate_score"))
	    		if(cookieValue.length()!=0)
	    			c.GateScore=Float.parseFloat(cookieValue);
	    	if(cookieName.equals("gate_rank"))
	    		if(cookieValue.length()!=0)
	    			c.GateRank=Integer.parseInt(cookieValue);
	    	if(cookieName.equals("achievements"))
	    		c.Achievements=cookieValue;
	    	
		}
		c.Display(1);
		c.Display(2);
		File f=new File("E:\\project\\Records\\"+c.Enrollno+".dat");
		f.getParentFile().mkdirs(); f.createNewFile();
		ObjectOutputStream o=new ObjectOutputStream(new FileOutputStream("E:\\project\\Records\\"+c.Enrollno+".dat"));
    	o.writeObject(c);
    	o.close();
    	BufferedWriter bw=new BufferedWriter(new FileWriter("E:\\project\\Records\\Registered.txt",true));
    	bw.write(c.Email+" "+c.Enrollno+"\n");
    	bw.close();
	}
	
}
