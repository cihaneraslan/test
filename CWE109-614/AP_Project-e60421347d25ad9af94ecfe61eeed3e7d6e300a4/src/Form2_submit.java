

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class Form2_submit
 */
@WebServlet("/Form2_submit")
@MultipartConfig
public class Form2_submit extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Form2_submit() {
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
	private static String getFileName(Part part) {
	    for (String cd : part.getHeader("content-disposition").split(";")) {
	        if (cd.trim().startsWith("filename")) {
	            String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	            return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
	        }
	    }
	    return null;
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Form2_submit");
		Part filePart = request.getPart("upload_cv"); 
		String name=getFileName(filePart);
		System.out.println("CV Name is : "+name);
	    BufferedReader br=new BufferedReader(new InputStreamReader(filePart.getInputStream()));
	    File f=new File("E:\\project\\CV\\"+request.getSession().getAttribute("enrollno")+"."+name.split("\\.")[1]);
		f.getParentFile().mkdirs(); f.createNewFile();
	    BufferedWriter bw=new BufferedWriter(new FileWriter("E:\\project\\CV\\"+request.getSession().getAttribute("enrollno")+"."+name.split("\\.")[1]));
	    String line;
	    while((line=br.readLine())!=null)
	    	bw.write(line+"\n");
	    br.close();bw.close();
	    filePart = request.getPart("upload_sop"); 
		name=getFileName(filePart);
		System.out.println("SOP Name is : "+name);
	    br=new BufferedReader(new InputStreamReader(filePart.getInputStream()));
	    f=new File("E:\\project\\SOP\\"+request.getSession().getAttribute("enrollno")+"."+name.split("\\.")[1]);
		f.getParentFile().mkdirs(); f.createNewFile();
	    bw=new BufferedWriter(new FileWriter("E:\\project\\SOP\\"+request.getSession().getAttribute("enrollno")+"."+name.split("\\.")[1]));
	    while((line=br.readLine())!=null)
	    	bw.write(line+"\n");
	    br.close();bw.close();
		Cookie cXBoard=new Cookie("XBoard",request.getParameter("XBoard"));
		Cookie cXMarks=new Cookie("XMarks",request.getParameter("XMarks"));
		Cookie cXYear=new Cookie("XYear",request.getParameter("XYear"));
		Cookie cXIIBoard=new Cookie("XIIBoard",request.getParameter("XIIBoard"));
		Cookie cXIIMarks=new Cookie("XIIMarks",request.getParameter("XIIMarks"));
		Cookie cXIIYear=new Cookie("XIIYear",request.getParameter("XIIYear"));
		Cookie cdegree=new Cookie("degree",request.getParameter("degree"));
		Cookie cdept=new Cookie("dept",request.getParameter("dept"));
		Cookie ccollege=new Cookie("college",request.getParameter("college"));
		Cookie cuniversity=new Cookie("university",request.getParameter("university"));
		Cookie ccity=new Cookie("city",request.getParameter("city"));
		Cookie cstate=new Cookie("state",request.getParameter("state"));
		Cookie cgrad_year=new Cookie("grad_year",request.getParameter("grad_year"));
		Cookie ccorm=new Cookie("corm",request.getParameter("corm"));
		Cookie ccgpa=new Cookie("cgpa",request.getParameter("cgpa"));
		Cookie ctotal_cgpa=new Cookie("total_cgpa",request.getParameter("total_cgpa"));
		Cookie cmarks=new Cookie("marks",request.getParameter("marks"));
		response.addCookie(cXBoard);response.addCookie(cXIIBoard);response.addCookie(cXMarks);response.addCookie(cXIIMarks);response.addCookie(cXYear);response.addCookie(cXIIYear);response.addCookie(cdegree);response.addCookie(cdept);response.addCookie(ccollege);response.addCookie(cuniversity);response.addCookie(ccity);response.addCookie(cstate);response.addCookie(cgrad_year);response.addCookie(ccorm);response.addCookie(ccgpa);response.addCookie(cmarks);response.addCookie(ctotal_cgpa);
		System.out.println("File CV: "+request.getParameter("upload_cv"));
		System.out.println("File SOP: "+request.getParameter("upload_sop"));
		////////////////////////////////////////////////////////////////
		Cookie copt_checkbox1=new Cookie("opt_checkbox1",(request).getParameter("opt_checkbox1"));
		Cookie copt_checkbox2=new Cookie("opt_checkbox2",(request).getParameter("opt_checkbox2"));
		Cookie copt_checkbox3=new Cookie("opt_checkbox3",(request).getParameter("opt_checkbox3"));
		Cookie copt_checkbox4=new Cookie("opt_checkbox4",(request).getParameter("opt_checkbox4"));
		
		Cookie cpg_pref1=new Cookie("pg_pref1",request.getParameter("pg_pref1"));
		Cookie cpg_pref2=new Cookie("pg_pref2",request.getParameter("pg_pref2"));
		Cookie cpg_pref3=new Cookie("pg_pref3",request.getParameter("pg_pref3"));
		Cookie cpg_pref4=new Cookie("pg_pref4",request.getParameter("pg_pref4"));
		
		Cookie cpg_college=new Cookie("pg_college",request.getParameter("pg_college"));
		Cookie cpg_city=new Cookie("pg_city",request.getParameter("pg_city"));
		Cookie cpg_state=new Cookie("pg_state",request.getParameter("pg_state"));
		Cookie cpg_department=new Cookie("pg_department",request.getParameter("pg_department"));
		Cookie cpg_degree=new Cookie("pg_degree",request.getParameter("pg_degree"));
		Cookie cpg_thesis=new Cookie("pg_thesis",request.getParameter("pg_thesis"));
		Cookie cpg_year=new Cookie("pg_year",request.getParameter("pg_year"));
		Cookie cpg_corm=new Cookie("pg_corm",request.getParameter("pg_corm"));
		Cookie cpg_cgpa=new Cookie("pg_cgpa",request.getParameter("pg_cgpa"));
		Cookie cpg_totcgpa=new Cookie("pg_totcgpa",request.getParameter("pg_totcgpa"));
		Cookie cpg_marks=new Cookie("pg_marks",request.getParameter("pg_marks"));
		
		Cookie cother_exam=new Cookie("other_exam",request.getParameter("other_exam"));
		Cookie cother_subject=new Cookie("other_subject",request.getParameter("other_subject"));
		Cookie cother_year=new Cookie("other_year",request.getParameter("other_year"));
		Cookie cother_score=new Cookie("other_score",request.getParameter("other_score"));
		Cookie cother_rank=new Cookie("other_rank",request.getParameter("other_rank"));
		
		Cookie cgate_area=new Cookie("gate_area",request.getParameter("gate_area"));
		Cookie cgate_year=new Cookie("gate_year",request.getParameter("gate_year"));
		Cookie cgate_marks=new Cookie("gate_marks",request.getParameter("gate_marks"));
		Cookie cgate_score=new Cookie("gate_score",request.getParameter("gate_score"));
		Cookie cgate_rank=new Cookie("gate_rank",request.getParameter("gate_rank"));
		
		Cookie cachiev=new Cookie("achievements",request.getParameter("achievements"));
		
		response.addCookie(cXBoard);response.addCookie(cXIIBoard);response.addCookie(cXMarks);response.addCookie(cXIIMarks);response.addCookie(cXYear);response.addCookie(cXIIYear);
		response.addCookie(cdegree);response.addCookie(cdept);response.addCookie(ccollege);response.addCookie(cuniversity);response.addCookie(ccity);response.addCookie(cstate);
		response.addCookie(cgrad_year);response.addCookie(ccorm);response.addCookie(ccgpa);response.addCookie(cmarks);response.addCookie(ctotal_cgpa);response.addCookie(copt_checkbox1);
		response.addCookie(copt_checkbox2);response.addCookie(copt_checkbox3);response.addCookie(copt_checkbox4);response.addCookie(cpg_pref1);response.addCookie(cpg_pref2);response.addCookie(cpg_pref3);
		response.addCookie(cpg_pref4);response.addCookie(cpg_college);response.addCookie(cpg_city);response.addCookie(cpg_state);response.addCookie(cpg_department);response.addCookie(cpg_degree);
		
		response.addCookie(cpg_thesis);response.addCookie(cpg_year);response.addCookie(cpg_corm);response.addCookie(cpg_cgpa);response.addCookie(cpg_totcgpa);response.addCookie(cpg_marks);
		response.addCookie(cother_exam);response.addCookie(cother_subject);response.addCookie(cother_year);response.addCookie(cother_score);response.addCookie(cother_rank);response.addCookie(cgate_area);
		response.addCookie(cgate_year);response.addCookie(cgate_marks);response.addCookie(cgate_score);response.addCookie(cgate_rank);response.addCookie(cachiev);
		response.sendRedirect("Submit_form");
	}

}
