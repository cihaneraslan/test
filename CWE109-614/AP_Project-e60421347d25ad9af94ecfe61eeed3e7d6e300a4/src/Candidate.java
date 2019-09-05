
//@author Name: Adesh Pandey , Rishabh Gupta Roll no: 2014004,2014086
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Candidate implements Serializable{

	private static final long serialVersionUID = 1L;
	public Candidate(){
		try{
			BufferedReader in=new BufferedReader(new FileReader("./src/Records/LastID.txt"));
			Enrollno=Integer.parseInt(in.readLine())+1;
			in.close();
			
	    }
	    catch(Exception e){}
}
	public Date TimeStamp=new Date();
	public String Name,Email,Corr_Address,Stream, Area1,Area2,Area3,Gender,Category,FName,Nationality,Perm_Address;
	public String XBoard,XIIBoard,Degree,Department,College,University,City,State,Achievements; 
	public Integer PIN,Enrollno,XYear,XIIYear,GradYear,TotalCGPA; public long Mobile;
	public Float CGPA,Marks,XMarks,XIIMarks;
	public LocalDate DOB;
	public boolean Handicap,Defense,cgpaormarks,pgcgpaormarks,ApplyECE,PGDone,OtherDegree,TakenGATE;
	public String Pref1,Pref2,Pref3,Pref4,PGCollege,PGCity,PGState,PGDepartment,PGDegree,PGTitle,PGThesis,OtherExam,OtherYear,OtherSubject,GateArea;
	public Integer OtherRank,GateYear,GateRank,PGYear,PGTotalCGPA;
	public Float PGCGPA,PGMarks,GateScore,GateMarks,OtherScore;
	public void Display(int i)
	{
		if(i==1)
		System.out.println(Name+"\n"+Enrollno+"\n"+Email+"\n"+Corr_Address+"\n"+Mobile+"\n"+Stream+"\n"+Area1+"\n"+Area2+"\n"+Area3+"\n"+Gender+"\n"+Category+"\n"+"Phy Disabled: "+Handicap+"\n"+DOB+"\nDefense? "+Defense+"\n"+FName+"\n"+Nationality+"\n"+Perm_Address+"\n"+PIN);
		if(i==2)
		System.out.println(XBoard+"\n"+XMarks+"\n"+XYear+"\n"+XIIBoard+"\n"+XIIMarks+"\n"+XIIYear+"\n"+Degree+"\n"+Department+"\n"+College+"\n"+University+"\n"+City+"\n"+State+"\n"+GradYear+"\n"+cgpaormarks+"\n"+CGPA+"\n"+Marks+"\n"+ApplyECE+"\n"+PGDone+"\n"+OtherDegree+"\n"+TakenGATE+"\n"+Achievements);
	}
}
