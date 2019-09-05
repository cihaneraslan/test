

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class update
 */
@WebServlet("/update")
public class update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public update() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		response.setContentType("application/txt");
		response.addHeader("Content-Disposition", "filename="+request.getParameter("act")+".txt");
		Candidate s=null;
		ObjectInputStream in=new ObjectInputStream(new FileInputStream("E:\\project\\Records\\"+request.getParameter("act")+".dat"));
		try {
			s=(Candidate)in.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}in.close();
		BufferedWriter bw;
		try{
			bw=new BufferedWriter(new FileWriter("./src/temp"+request.getParameter("act")+".txt"));
			bw.write("Name: "+s.Name+"\nEnroll no: "+s.Enrollno+"\nEmail: "+s.Email+"\nCorrespondence Address: "+s.Corr_Address+"\nMobile: "+s.Mobile+"\nStream: "+s.Stream+"\nPHD Area1: "+s.Area1+"\nGender: "+s.Gender+"\nCategory: "+s.Category+"\nPhysically Disabled: "+s.Handicap+"\nDOB: "+s.DOB+"\nWard of Defense Employee? "+s.Defense+"\nFather's Name: "+s.FName+"\nNationality: "+s.Nationality+"\nPermanent Address: "+s.Perm_Address+"\nPIN: "+s.PIN+"\n");
			bw.write("X Board: "+s.XBoard+"\nX Marks: "+s.XMarks+"\nX Year: "+s.XYear+"\nXII Board: "+s.XIIBoard+"\nXII Marks: "+s.XIIMarks+"\nXII Year: "+s.XIIYear+"\nDegree: "+s.Degree+"\nDepartment: "+s.Department+"\nCollege: "+s.College+"\nUniversity: "+s.University+"\nCity: "+s.City+"\nState: "+s.State+"\nGraduation Year: "+s.GradYear+"\nCGPA or Marks specified?"+s.cgpaormarks+"\nCGPA: "+s.CGPA+"\nMarks: "+s.Marks+"\nApplying for ECE? "+s.ApplyECE+"\nPG Done? "+s.PGDone+"\nAny Other Degree? "+s.OtherDegree+"\nTaken GATE? "+s.TakenGATE+"\nAchievements: "+s.Achievements);
			bw.write("\nTimeStamp: "+s.TimeStamp);
			bw.close();
			}
			catch(Exception E){E.printStackTrace();}
		FileInputStream fis = new FileInputStream("./src/temp"+request.getParameter("act")+".txt");
	    BufferedInputStream bis = new BufferedInputStream(fis);
	    ServletOutputStream sos = response.getOutputStream();
	      byte[] buffer = new byte[2048];
	      while (true) {
	        int bytesRead = bis.read(buffer, 0, buffer.length);
	        if (bytesRead < 0) {
	          break;
	        }
	      sos.write(buffer, 0, bytesRead);
	      sos.flush();
	      }
	}
}
