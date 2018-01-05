package com.mitter.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SerAndDeserServlet
 */
@WebServlet("/store.do")
public class SerAndDeserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SerAndDeserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter pw=response.getWriter();
		String method=request.getParameter("method");
		String resp="";
		System.out.println(method);
		
		if(method==null)
		{
			resp="Method can't be null";
		}
		else if("Serialize".equals(method))
		{
			StoreManagerServlet.serializeStoreManager();
			resp="StoreManager Successfully "+method;
		}else if("Clear".equals(method))
		{
			StoreManagerServlet.clear();
			StoreManagerServlet.serializeStoreManager();
			resp="StoreManager Successfully "+method;
		}else
		{
			StoreManagerServlet.deserializeStoreManager();
			resp="StoreManager Successfully "+method;
		}
		pw.write(resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
