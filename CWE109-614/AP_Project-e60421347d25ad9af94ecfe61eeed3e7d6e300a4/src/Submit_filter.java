

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Submit_filter
 */
@WebServlet("/Submit_filter")
public class Submit_filter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	ArrayList<Candidate> cands=new ArrayList<Candidate> ();
	ArrayList<Candidate> main=new ArrayList<Candidate>();
	ArrayList<Candidate> filtered=new ArrayList<Candidate>();
	String email,name,category,gender,f_dob;
	String phdStream,XBoard,XIIBoard,gradDegree,pgDegree,gradDepartment,pgDepartment,ug_university,pg_university,ug_state,pg_state;
	boolean pd;
	int enrollno;
	
	Float xpercent,xiipercent,gradpercent,pgpercent,gate_score;
	LocalDate dob,date_from,date_upto;;
	boolean isname=false,isenrollno=false,isemail=false,iscategory=false,isgender=false,ispd=false,isdob=false,isphdStream=false,isgradDegree=false,ispgDegree=false,isXBoard=false,isXIIBoard=false,isgradDepartment=false,ispgDepartment=false,isug_university=false,ispg_university=false,isug_state=false,ispg_state=false,isxpercent=false,isxiipercent=false,isgradpercent=false,ispgpercent=false,isgate_score=false,isdate_from=false,isdate_upto=false;
	boolean x_gt,x_eq,x_lt,xii_gt,xii_eq,xii_lt,grad_gt,grad_eq,grad_lt,pg_gt,pg_eq,pg_lt,gate_gt,gate_eq,gate_lt;

    public Submit_filter() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	void validate()
	{
		if(isname){
			for(int i=0;i<main.size();i++)
				{
				System.out.println("Checking namesss : main size: "+main.size());
					System.out.println("Checking "+main.get(i).Name+" vs "+name);
					if(main.get(i).Name.equals(name))
						filtered.add(main.get(i));
				}
		main.clear();
		main.addAll(filtered);
		filtered.clear();
		System.out.println("Checking name : main size: "+main.size());}
		if(isemail){
			System.out.println("Checking emailsss : main size: "+main.size());
			for(int i=0;i<main.size();i++)
				{
				System.out.println("Checking with email: "+main.get(i).Email);
				if(main.get(i).Email.equals(email))
					filtered.add(main.get(i));
				}
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(isenrollno){
			System.out.println("Enroll no checking "+enrollno);
			for(int i=0;i<main.size();i++)
				{	
				System.out.println("CHecking with "+main.get(i).Enrollno);
				if(main.get(i).Enrollno.equals(enrollno))
				{
					System.out.println("Succeeded");
					filtered.add(main.get(i));
				}
				}
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(iscategory){
			for(int i=0;i<main.size();i++)
				if(main.get(i).Category.equals(category))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(isgender){
			for(int i=0;i<main.size();i++)
				if(main.get(i).Gender.equals(gender))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(ispd){
			for(int i=0;i<main.size();i++)
				if(main.get(i).Handicap==pd)
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(isdob && f_dob.equals("Before")){
			for(int i=0;i<main.size();i++)
				if(main.get(i).DOB.isBefore(dob))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(isdob && f_dob.equals("After")){
			for(int i=0;i<main.size();i++)
				if(main.get(i).DOB.isAfter(dob))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(isdob && f_dob.equals("On")){
			for(int i=0;i<main.size();i++)
				if(main.get(i).DOB.isEqual(dob))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(isphdStream){
			for(int i=0;i<main.size();i++)
				if(main.get(i).Stream.equals(phdStream))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(isXBoard){
			for(int i=0;i<main.size();i++)
				if(main.get(i).XBoard.equals(XBoard))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(isXIIBoard){
			for(int i=0;i<main.size();i++)
				if(main.get(i).XIIBoard.equals(XIIBoard))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(isgradDegree){
			for(int i=0;i<main.size();i++)
				if(main.get(i).Degree.equals(gradDegree))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(ispgDegree){
			for(int i=0;i<main.size();i++)
				if(main.get(i).PGDone && main.get(i).PGDegree.equals(pgDegree))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(isgender){
			for(int i=0;i<main.size();i++)
				if(main.get(i).Gender.equals(gender))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(isgradDepartment){
			for(int i=0;i<main.size();i++)
				if(main.get(i).Department.equals(gradDepartment))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(ispgDepartment){
			for(int i=0;i<main.size();i++)
				if(main.get(i).PGDepartment!=null&&main.get(i).PGDepartment.equals(pgDepartment))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(isug_university){
			for(int i=0;i<main.size();i++)
				if(main.get(i).University.equals(ug_university))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(ispg_university){
			for(int i=0;i<main.size();i++)
				if(main.get(i).University.equals(pg_university))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(isug_state){
			for(int i=0;i<main.size();i++)
				if(main.get(i).State.equals(ug_state))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(ispg_state){
			for(int i=0;i<main.size();i++)
				if(!main.get(i).PGState.isEmpty()&&main.get(i).PGState.equals(pg_state))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(isxpercent)
		{
			System.out.println("Xpercentage checking");
			if(x_gt){
				System.out.println("Is Greater than"+xpercent);
				for(int i=0;i<main.size();i++)
					{
						System.out.println("Checking with "+main.get(i).XMarks);
						if(main.get(i).XMarks>xpercent)
							filtered.add(main.get(i));
					}
				//System.out.println(filtered);
			}
			if(x_lt){
				for(int i=0;i<main.size();i++)
					if(main.get(i).XMarks<xpercent)
						filtered.add(main.get(i));
			}
			if(x_eq){
				for(int i=0;i<main.size();i++)
					if(main.get(i).XMarks==xpercent)
						filtered.add(main.get(i));
				}
			if(x_gt ||x_lt || x_eq)
			{
				main.clear();
				main.addAll(filtered);
				filtered.clear();
			}
		}
		if(isxiipercent)
		{
			if(xii_gt){
				for(int i=0;i<main.size();i++)
					if(main.get(i).XIIMarks>xiipercent)
						filtered.add(main.get(i));
			}
			if(xii_lt){
				for(int i=0;i<main.size();i++)
					if(main.get(i).XIIMarks<xiipercent)
						filtered.add(main.get(i));
			}
			if(xii_eq){
				for(int i=0;i<main.size();i++)
					if(main.get(i).XIIMarks==xiipercent)
						filtered.add(main.get(i));
			}
			if(xii_gt ||xii_lt || xii_eq)
			{
				main.clear();
				main.addAll(filtered);
				filtered.clear();
			}
		}
		if(isgradpercent)
		{
			if(grad_gt){
				for(int i=0;i<main.size();i++){
					try{
					if(main.get(i).Marks>gradpercent)
						filtered.add(main.get(i));
					}catch(Exception e){}}
			}
			if(grad_lt){
				for(int i=0;i<main.size();i++){
					try{
					if(main.get(i).Marks<gradpercent)
						filtered.add(main.get(i));
					}catch(Exception e){}}
			}
			if(grad_eq){
				for(int i=0;i<main.size();i++){
					try{
					if(main.get(i).Marks==gradpercent)
						filtered.add(main.get(i));
					}catch(Exception e){}}
			}
			if(grad_gt || grad_lt || grad_eq)
			{
				main.clear();
				main.addAll(filtered);
				filtered.clear();
			}
		}
		if(ispgpercent)
		{
			if(pg_gt){
				for(int i=0;i<main.size();i++){
					try{
					if(main.get(i).PGDone &&main.get(i).PGMarks>pgpercent)
						filtered.add(main.get(i));
					}catch(Exception e){}}
			}
			if(pg_lt){
				for(int i=0;i<main.size();i++){
					try{
					if(main.get(i).PGDone &&main.get(i).PGMarks<pgpercent)
						filtered.add(main.get(i));
					}catch(Exception e){}	}
			}
			if(pg_eq){
				for(int i=0;i<main.size();i++){
					try{
					if(main.get(i).PGDone &&main.get(i).PGMarks==pgpercent)
						filtered.add(main.get(i));
					}catch(Exception e){}}
			}
			if(pg_gt || pg_lt || pg_eq)
			{
				main.clear();
				main.addAll(filtered);
				filtered.clear();
			}
		}
		if(isgate_score)
		{
			if(gate_gt){
				for(int i=0;i<main.size();i++){
					try{
					if(main.get(i).TakenGATE && main.get(i).GateScore>gate_score)
						filtered.add(main.get(i));
					}catch(Exception e){}}
			}
			if(gate_lt){
				for(int i=0;i<main.size();i++){
					try{
					if(main.get(i).TakenGATE &&  main.get(i).GateScore<gate_score)
						filtered.add(main.get(i));
					}catch(Exception e){}}
			}
			if(gate_eq){
				for(int i=0;i<main.size();i++){
					try{
					if(main.get(i).TakenGATE &&  main.get(i).GateScore==gate_score)
						filtered.add(main.get(i));
					}catch(Exception e){}}
			}
			if( (gate_gt || gate_lt || gate_eq))
			{
				System.out.println("Inside");
				main.clear();
				main.addAll(filtered);
				filtered.clear();
			}
		}
		if(isdate_from){
			for(int i=0;i<main.size();i++)
				if(date_from.isBefore(main.get(i).TimeStamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
		if(isdate_upto){
			for(int i=0;i<main.size();i++)
				if(date_upto.isAfter(main.get(i).TimeStamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
					filtered.add(main.get(i));
		main.clear();
		main.addAll(filtered);
		filtered.clear();}
	}
	public void is(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

    	HttpSession session=request.getSession(false);  
    	System.out.println("Insideeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");;
	if(( ((String)session.getAttribute("name")).equals("\0"))==false)
	{
		System.out.println("Name will be checked now");
		isname=true;
	}

	if(( ((String)session.getAttribute("email")).equals("\0"))==false)
	//if((String)session.getAttribute("email")!=null)
	{
	isemail=true;
	}

	//if(( ((String)session.getAttribute("enrollno")).equals("\0"))==true)
	try{
	Integer.parseInt((String)session.getAttribute("enrollno"));
	isenrollno=true;
	}
	catch(Exception e){isenrollno=false;}
	
		
	
	if( !((String)session.getAttribute("category")).equals("All"))
	{
		System.out.println("I WILL CHECK CATEGORY NOW");
	iscategory=true;
	}


	if((String)session.getAttribute("gender")!=null)
	{
	isgender=true;
	}

	if((String)session.getAttribute("pd")!=null)
	{
	ispd=true;
	}

	if((String)session.getAttribute("dob")!=null)
	{
	isdob=true;
	}

	if(!((String)session.getAttribute("stream")).equals("All"))// && ((String)session.getAttribute("stream")!=null))

	{
	isphdStream=true;
	}

	if(!((String)session.getAttribute("xboard")).equals("All"))// && ((String)session.getAttribute("xboard")!=null))
	{
	isXBoard=true;
	}

	if(!((String)session.getAttribute("xiiboard")).equals("All"))// && ((String)session.getAttribute("xiiboard")!=null))
	{
	isXIIBoard=true;
	}

	if(!((String)session.getAttribute("grad_degree")).equals("All"))// && ((String)session.getAttribute("grad_degree")!=null))
	{
	isgradDegree=true;
	}

	if(!((String)session.getAttribute("pg_degree")).equals("All"))// && ((String)session.getAttribute("pg_degree")!=null))
	{
	ispgDegree=true;
	}

	if(!((String)session.getAttribute("grad_dep")).equals("All"))// && ((String)session.getAttribute("grad_dep")!=null))
	{
	isgradDepartment=true;
	}

	if(!((String)session.getAttribute("pg_dep")).equals("All"))// && ((String)session.getAttribute("pg_dep")!=null))
	{
	ispgDepartment=true;
	}

	if(( ((String)session.getAttribute("grad_univ")).equals("\0"))==false)
	{
	isug_university=true;
	}

	if(( ((String)session.getAttribute("pg_univ")).equals("\0"))==false)
	{
	ispg_university=true;
	}

	if(!((String)session.getAttribute("grad_state")).equals("All"))// && ((String)session.getAttribute("grad_state")!=null))
	{
	isug_state=true;
	}

	if(!((String)session.getAttribute("pg_state")).equals("All"))// && ((String)session.getAttribute("pg_state")!=null))
	{
	ispg_state=true;
	}

	if(( ((String)session.getAttribute("x_percent")).equals("\0"))==false)
	{
	isxpercent=true;
	}

	if(( ((String)session.getAttribute("xii_percent")).equals("\0"))==false)
	{
	isxiipercent=true;
	}

	if(( ((String)session.getAttribute("grad_percent")).equals("\0"))==false)
	{
	isgradpercent=true;
	}
	if(( ((String)session.getAttribute("pg_percent")).equals("\0"))==false)
	{
	ispgpercent=true;
	}

	if(( ((String)session.getAttribute("gate_score")).equals("\0"))==false)
	{
	isgate_score=true;
	}
	//System.out.println(fdate_from.getValue());
	if(!((String)session.getAttribute("date_from")).equals(""))
	{
	isdate_from=true;
	System.out.println("date from set");
	}

	if(!((String)session.getAttribute("date_upto")).equals(""))
	{
		System.out.println("date upto set");
	isdate_upto=true;
	}



}

	
	void readfiles() throws FileNotFoundException, IOException, ClassNotFoundException
	{
		Candidate c;
		BufferedReader br=new BufferedReader(new FileReader("E:\\project\\Records\\Registered.txt"));
		String line;ObjectInputStream in;
		while((line=br.readLine())!=null){
			String file=line.split(" ")[1];
			in=new ObjectInputStream(new FileInputStream("E:\\project\\Records\\"+file+".dat"));
			c=(Candidate)in.readObject();
			cands.add(c);
			main.add(c);
			//System.out.println("Name is "+c.Name);
			//c.Display(1);
			}

		
	}
	
	void takevalues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
    	HttpSession session=request.getSession();  

    	name=(String)session.getAttribute("name");//,request.getParameter("fname"));
        email=(String)session.getAttribute("email");//,request.getParameter("femail"));
        if(!(((String)session.getAttribute("enrollno")).equals("\0")))
        	if(  ((String)session.getAttribute("enrollno")).length()!=0   )
        enrollno=Integer.parseInt((String)session.getAttribute("enrollno")); //',request.getParameter("fenrollno"));
        if(session.getAttribute("gender")!=null)
        gender=(String)session.getAttribute("gender");//,request.getParameter("fgender"));
        if(session.getAttribute("category")!=null)
        category=(String) session.getAttribute("category");//,request.getParameter("fcategory"));
        if(session.getAttribute("pd")!=null)
        //(String)session.getAttribute("pd");//,request.getParameter("fpd"));
        	pd=true;
		else
			pd=false;
        f_dob=(String)session.getAttribute("dob");
        try{
        dob=LocalDate.parse((String)session.getAttribute("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));;//,request.getParameter("fdate"));
        }catch(Exception e){}
       phdStream=(String)session.getAttribute("stream");
       gradDegree=(String) session.getAttribute("grad_degree");
       pgDegree=(String) session.getAttribute("pg_degree");
        XBoard=(String) session.getAttribute("xboard");
        XIIBoard=(String) session.getAttribute("xiiboard");
        gradDepartment=(String)session.getAttribute("grad_dep");
        pgDepartment=(String) session.getAttribute("pg_dep");
        ug_university=(String) session.getAttribute("grad_univ");
        pg_university= (String) session.getAttribute("pg_univ");
        ug_state=(String) session.getAttribute("grad_state");
        pg_state=(String) session.getAttribute("pg_state");
        
   		
    	if((String)session.getAttribute("x_lt")!=null)
    		x_lt=true;
    	else
    		x_lt=false;
    	if((String)session.getAttribute("x_eq")!=null)
    		x_eq=true;
    	else
    		x_eq=false;
    	if((String)session.getAttribute("x_gt")!=null)
    		x_gt=true;
    	else
    		x_gt=false;
    	if((String)session.getAttribute("xii_lt")!=null)
    		xii_lt=true;
    	else
    		xii_lt=false;
    	if((String)session.getAttribute("xii_eq")!=null)
    		xii_eq=true;
    	else
    		xii_eq=false;
    	if((String)session.getAttribute("xii_gt")!=null)
    		xii_gt=true;
    	else
    		xii_gt=false;
    	if((String)session.getAttribute("grad_lt")!=null)
    		grad_lt=true;
    	else
    		grad_lt=false;
    	if((String)session.getAttribute("grad_eq")!=null)
    		grad_eq=true;
    	else
    		grad_eq=false;
    	if((String)session.getAttribute("grad_gt")!=null)
    		grad_gt=true;
    	else
    		grad_gt=false;
    	if((String)session.getAttribute("pg_lt")!=null)
    		pg_lt=true;
    	else
    		pg_lt=false;
    	if((String)session.getAttribute("pg_eq")!=null)
    		pg_eq=true;
    	else
    		pg_eq=false;
    	if((String)session.getAttribute("pg_gt")!=null)
    		pg_gt=true;
    	else
    		pg_gt=false;
    	if((String)session.getAttribute("gate_lt")!=null)
    		gate_lt=true;
    	else
    		gate_lt=false;
    	if((String)session.getAttribute("gate_eq")!=null)
    		gate_eq=true;
    	else
    		gate_eq=false;
    	if((String)session.getAttribute("gate_gt")!=null)
    		gate_gt=true;
    	else
    		gate_gt=false;

    	if(((String) session.getAttribute("x_percent")).length()!=0 &&!(((String)session.getAttribute("x_percent")).equals("\0")))
			xpercent=Float.parseFloat((String) session.getAttribute("x_percent"));
		if(((String) session.getAttribute("xii_percent")).length()!=0  &&!(((String)session.getAttribute("xii_percent")).equals("\0")))
			xiipercent=Float.parseFloat((String) session.getAttribute("xii_percent"));
		if(((String) session.getAttribute("grad_percent")).length()!=0  &&!(((String)session.getAttribute("grad_percent")).equals("\0")))
			gradpercent=Float.parseFloat((String) session.getAttribute("grad_percent"));
		if(((String) session.getAttribute("pg_percent")).length()!=0  &&!(((String)session.getAttribute("pg_percent")).equals("\0")))
			pgpercent=Float.parseFloat((String) session.getAttribute("pg_percent"));
		if(((String)session.getAttribute("gate_score")).length()!=0  &&!(((String)session.getAttribute("gate_score")).equals("\0")))
			gate_score=Float.parseFloat((String) session.getAttribute("gate_score"));
		try{
		if(session.getAttribute("date_from")!=null)
    	date_from=LocalDate.parse((String)session.getAttribute("date_from"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));}catch(Exception e){date_from=null;}
		try{
		if(session.getAttribute("date_upto")!=null)
    	date_upto=LocalDate.parse((String)session.getAttribute("date_upto"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));}catch(Exception e){date_upto=null;}
    	
	}

	void Display()
	{
		System.out.println(name+"\n"+enrollno+"\n"+email+"\n"+gender+"\n"+category+"\n"+"Phy Disabled: "+pd+"\n"+f_dob+"\n");//+dob);
    	System.out.println(phdStream+"\n"+XBoard+"\n"+XIIBoard+"\n"+gradDegree+"\n"+pgDegree+"\n"+gradDepartment+"\n"+pgDepartment+"\n"+ug_university+"\n"+pg_university+"\n"+ug_state+"\n"+pg_state);
		System.out.println(x_gt+"\n"+xii_eq+"\n"+grad_lt+"\n"+pg_lt+"\n"+gate_eq+"\n");

    	System.out.println("\n"+"date from--"+date_from+"\n"+"date upto---"+date_upto);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		isname=false;isenrollno=false;isemail=false;iscategory=false;isgender=false;ispd=false;isdob=false;isphdStream=false;isgradDegree=false;ispgDegree=false;isXBoard=false;isXIIBoard=false;isgradDepartment=false;ispgDepartment=false;isug_university=false;ispg_university=false;isug_state=false;ispg_state=false;isxpercent=false;isxiipercent=false;isgradpercent=false;ispgpercent=false;isgate_score=false;isdate_from=false;isdate_upto=false;
		x_gt=false;x_eq=false;x_lt=false;xii_gt=false;xii_eq=false;xii_lt=false;grad_gt=false;grad_eq=false;grad_lt=false;pg_gt=false;pg_eq=false;pg_lt=false;gate_gt=false;gate_eq=false;gate_lt=false;
		main.clear();filtered.clear();cands.clear();
   
    	takevalues(request,response);
    	HttpSession session=request.getSession(false);
    	try {
			readfiles();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println((String)session.getAttribute("date_from")+" "+(String)session.getAttribute("date_upto"));
        Display();
        System.out.println("Inside submit filter Enroll no = "+(String)session.getAttribute("enrollno"));
        is(request,response);
        validate();
        System.out.println("Calling display");
        System.out.println((String)session.getAttribute("email"));	
        System.out.println(session.getAttribute("email")+" "+isemail);
        PrintWriter pw=response.getWriter();
		pw.print("<table style=\"width:50%\"><tr><td><b><u>Enroll no</u></b></td><td><u><b>Name</u></b></td><td><u><b>Link</u></b><td></tr>");
        for (int i=0;i<main.size();i++){
        	//System.out.println(main.get(i).Name+" "+main.get(i).Email);
        	pw.print("<tr><td>"+main.get(i).Enrollno+"</td><td>"+main.get(i).Name+"</td><td><form name=\"myform\" action=\"update\" method=\"GET\" ><input type=\"submit\" name =\"act\" value=\""+main.get(i).Enrollno+"\"></form></</td></tr>");
        }
//        for (int i=0;i<filtered.size();i++){
//        	System.out.println(filtered.get(i).Name);
//        }
        //System.out.println("\n\n\n\n\n\n\n");
        //System.out.println((String)session.getAttribute("name"));

	}

}
