package filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = {"/servlet1"})
public class MyFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		PrintWriter pw = resp.getWriter();
		ServletContext sc = req.getServletContext();
		String form = req.getParameter("forms");
		
		if(form.equals("form1")) {
			
			String fname = req.getParameter("fnameVar");
			String lname = req.getParameter("lnameVar");
			sc.setAttribute("fnameFilter", fname);
			sc.setAttribute("lnameFilter", lname);
			RequestDispatcher dispatch = req.getRequestDispatcher("index2.html");
			dispatch.forward(req, resp);	
		}else if(form.equals("form2")) {
			String school = req.getParameter("schoolVar");
			sc.setAttribute("schoolFilter", school);
			
			String fnameFilter = (String)sc.getAttribute("fnameFilter");
			String lnameFilter = (String)sc.getAttribute("lnameFilter");
			String schoolFilter = (String)sc.getAttribute("schoolFilter");
			
			if(fnameFilter.equals("") || lnameFilter.equals("") || schoolFilter.equals("") ) {
				pw.println("All fields are mandatory...Please fill all fields");
			}else {
				chain.doFilter(req, resp);
			}
			
		}
		
		
	}

}
