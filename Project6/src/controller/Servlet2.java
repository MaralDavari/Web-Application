package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(urlPatterns = {"/servlet2"},initParams = {@WebInitParam(name="initp", value="THIS IS MY INIT")})
public class Servlet2 extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, 
			HttpServletResponse resp) throws ServletException, IOException {
		
		PrintWriter pw =resp.getWriter();
		HttpSession mysession = req.getSession();
		String form = req.getParameter("forms");
		String fname = null;
		String lname = null;
		String school =null;
		
		String fnameFromSession = null;
		String lnameFromSession = null;
		String schoolFromSession = null;
		
		ResultSet rs = null;
		PreparedStatement pst =null;
		
		if(form.equals("form1")) {
			fname = req.getParameter("fnameVar");
			lname = req.getParameter("lnameVar");
			if(fname != null && lname != null) {
				mysession.setAttribute("fnameSession", fname);
				mysession.setAttribute("lnameSession", lname);
			}else {
				mysession.setAttribute("fnameSession", getServletContext().getAttribute("fnameFilter"));;
				mysession.setAttribute("lnameSession", getServletContext().getAttribute("lnameFilter"));;	
			}
			
			resp.sendRedirect("index2.html");
		}else if(form.equals("form2")) {
			school = req.getParameter("schoolVar");
			if(school != null) {
				mysession.setAttribute("schoolSession", school);
			}else {
				mysession.setAttribute("schoolSession", getServletContext().getAttribute("schoolFilter"));;
			}
			fnameFromSession = (String)mysession.getAttribute("fnameSession");
			lnameFromSession = (String)mysession.getAttribute("lnameSession");
			schoolFromSession = (String)mysession.getAttribute("schoolSession");
			
			
			Connection conn = null;
		    pst = null;
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "student", "oracle");
				String query ="insert into shinshintable values(?,?,?)";
				pst = conn.prepareStatement(query);
				pst.setString(1, fnameFromSession);
				pst.setString(2, lnameFromSession);
				pst.setString(3, schoolFromSession);
				int count = pst.executeUpdate();
							pst.executeUpdate("commit");
							
				if(count != 0) {
					pw.println(fnameFromSession + "'s record has been added to DB successfully");
				}else{
					pw.println("Could not add to DB!!");
				}
							
							
			}catch(Exception e) {
				e.printStackTrace();
				pw.println("This is comming from catch - COULD NOT ADD TO DB");
			}
		}
		
		
		
		
		try {
			String myContext = getServletContext().getInitParameter("mypub");
			String myInit = getServletConfig().getInitParameter("initp");
			pw.println("Current Session: " + fnameFromSession + " " + lnameFromSession + " " + schoolFromSession) ;
			pw.println("My Init Param: " + myInit);
			pw.println("My Context Param: " + myContext);
			String query = "select * from shinshintable";
			 rs =pst.executeQuery(query);
			while(rs.next()) {
				String firstNameFromDB = rs.getString(1);
				String lastNameFromDB = rs.getString(2);
				String schoolFromDB = rs.getString(3);
				
				pw.println("First Name: " + firstNameFromDB + 
						" Last Name: " + lastNameFromDB + " School: " + schoolFromDB);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
