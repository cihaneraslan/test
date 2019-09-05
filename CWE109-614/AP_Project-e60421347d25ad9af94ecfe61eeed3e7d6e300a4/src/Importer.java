
//@author Name: Adesh,Rishabh Roll no:2014004,2014086 respectively
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Random;

public class Importer {

	static Candidate c;
	static BufferedReader fopen;
	static{
		try{
		c=new Candidate();
		fopen=new BufferedReader(new FileReader("C:\\Users\\adesh\\Desktop\\Data_to_Import.csv"));
		fopen.readLine();}
		catch(Exception e){}
	}
	static String line;
	static void readCandidate(){
		//try{
		//	BufferedReader in=new BufferedReader(new FileReader("./src/Records/LastID.txt"));
			c.Enrollno=new Random().nextInt(10000000);
		//	in.close();
	    //}
	    //catch(Exception e){}
		line=line.replace(", ", "^");
		
		String words[]=new String[100];
		for(int i=0;i<100;i++)
			words[i]=new String();
		words=line.split(",");
		words[2]=words[2].replace("^", ", ");
		words[15]=words[15].replace("^", ", ");
		words[2]=words[2].replace("\"", "");
		words[15]=words[15].replace("\"", "");


		c.Email=words[0];
		c.Name=words[1];
		c.Corr_Address=words[2];
		c.Mobile=Long.parseLong(words[3]);
		c.Stream=words[4];
		c.Area1=words[5];
		c.Area2=words[6];
		c.Area3=words[7];
		
		c.Gender=words[8];
		c.Category=words[9];
		if(words[10].equals("Yes"))
			c.Handicap=true;
		else
			c.Handicap=false;
		try{
		SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
		c.DOB=formatter.parse(words[11]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
		catch(Exception e){}
		if(words[10].equals("Yes"))
			c.Defense=true;
		else
			c.Defense=false;
		c.FName=words[13];
		c.Nationality=words[14];
		c.Perm_Address=words[15];
		c.PIN=Integer.parseInt(words[16]);
		c.XBoard=words[17];
		c.XMarks=Float.parseFloat(words[18]);
		c.XYear=Integer.parseInt(words[19]);
		c.XIIBoard=words[20];
		c.XIIMarks=Float.parseFloat(words[21]);
		c.XIIYear=Integer.parseInt(words[22]);
		c.Degree=words[23];
		c.Department=words[24];
		c.College=words[25];
		c.University=words[26];
		c.City=words[27];
		c.State=words[28];
		c.GradYear=Integer.parseInt(words[29]);
		if(words[30].split(":")[0].equals("CGPA"))
		{
		c.cgpaormarks=true;
		c.CGPA=Float.parseFloat(words[30].split(":")[1]);
		}
		else
		{
		c.cgpaormarks=false;
		c.Marks=Float.parseFloat(words[30].split(":")[1]);
		}
		
		
		
		
		if(words[31].equals("Yes"))
			c.ApplyECE=true;
		else
			c.ApplyECE=false;
		c.Pref1=words[32];
		c.Pref2=words[33];
		c.Pref3=words[34];
		c.Pref4=words[35];
		if(words[36].equals("Yes"))
			c.PGDone=true;
		else
			c.PGDone=false;
		c.PGDegree=words[37];
		c.PGDepartment=words[38];
		c.PGCollege=words[39];
		c.PGThesis=words[40];
		c.PGCity=words[41];
		c.PGState=words[42];
		if(words[43].isEmpty()==false)
		c.PGYear=Integer.parseInt(words[43]);
		if(words[44].isEmpty()==false)
		if(words[44].split(":")[0].equals("CGPA"))
		{
		c.pgcgpaormarks=true;
		c.PGCGPA=Float.parseFloat(words[44].split(":")[1]);
		}
		else
		{
		c.pgcgpaormarks=false;
		c.PGMarks=Float.parseFloat(words[44].split(":")[1]);
		}
		//other academic degree c.Email=words[45];
		if(words[45].equals("Yes"))
			c.OtherDegree=true;
		else
			c.OtherDegree=false;
		
		c.OtherExam=words[46];
		c.OtherSubject=words[47];
		c.OtherYear=words[48];
		if(words[49].isEmpty()==false)
		c.OtherScore=Float.parseFloat(words[49]);
		if(words[50].isEmpty()==false)
		c.OtherRank=Integer.parseInt(words[50]);
		if(words[51].equals("Yes"))
			c.TakenGATE=true;
		else
			c.TakenGATE=false;
		
		c.GateArea=words[52];
		if(words[53].isEmpty()==false)
		c.GateYear=Integer.parseInt(words[53]);
		if(words[54].isEmpty()==false)
		c.GateMarks=Float.parseFloat(words[54]);
		if(words[55].isEmpty()==false)
		c.GateScore=Float.parseFloat(words[55]);
		if(words[56].isEmpty()==false)
		c.GateRank=Integer.parseInt(words[56]);
		c.Achievements=words[57];
		//c.Display(1);
		//c.Display(2);
		
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			c.TimeStamp=formatter.parse(words[58]);
			}
			catch(Exception e){}
		//System.out.println(c.TimeStamp);
	}
	static void writeCandidate() throws FileNotFoundException, IOException{
		ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream("E:\\project\\Records\\"+c.Enrollno+".dat"));
    	out.writeObject(c);
    	out.close();
    	BufferedWriter o=new BufferedWriter(new FileWriter("E:\\project\\Records\\Registered.txt",true));
		o.write(c.Email+" "+c.Enrollno+"\n");
		o.close();
		
	}
	public static void main(String[] args) throws IOException {
		
		while((line=fopen.readLine())!=null)
		{
			readCandidate();
			try{
			writeCandidate();
			}
			catch(Exception e){}
		}
	}

}
