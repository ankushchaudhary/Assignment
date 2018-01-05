package com.mitter.servlets;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mitter.core.StoreManager;

/**
 * Servlet implementation class StoreManagerServlet
 */
///@WebServlet("/operation.do")
public class StoreManagerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Path filePath=null;
	static StoreManager sm=null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StoreManagerServlet() {
    
        super();

        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
    	   // Initialization code...
       	ServletContext context = getServletConfig().getServletContext();
       	//System.out.println("inside servlet constructor"+context.getContextPath());
        filePath=Paths.get(context.getRealPath("/WEB-INF/data.ser")); //getting path for storing map object
        System.out.println(filePath.toAbsolutePath().toString());
       	if(Files.exists(filePath))
       	{   
       		deserializeStoreManager(); // if file exists the restoring the Storemanager object
       	}else
       	{
       		try {
       		sm=new StoreManager();   // if not 
       		//System.out.println("file not exist");
  
       		}catch(Exception e)
       		{
       			e.printStackTrace();
       		}
       	}
    	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter pw=response.getWriter();
		String all=request.getParameter("all");
		JSONArray jsArray=null;
		String method,value;
		String resp="";
		if(all!=null)
		{
			System.out.println("all="+all);
			jsArray=sm.getAll();
		}else 
		{
			method=request.getParameter("method");
			value=request.getParameter("value");
			System.out.println("method= "+method+" and value "+value);
			
			try {
			if("get".equals(method))
			{
				jsArray=sm.get(value.split(","));
				
			}
		
		else if("query".equals(method)) {
			
			jsArray=sm.query(value);
		}else
		{
			
			jsArray=sm.queryGt(Float.parseFloat(value));
		}
		}catch(NumberFormatException e)
			{
			   e.printStackTrace();
			   resp="Please enter correct values for query! ";
			   pw.write(resp);
			}
			
		}
		
		pw.write(jsArray.toString());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw=response.getWriter();
		//pw.println("inside post");
		String key=request.getParameter("key");
		String value=request.getParameter("value");
		String type=request.getParameter("type");
		//System.out.println(key+"  :  "+value);
		if(key==null||value==null)
		{
			pw.write("Either key or value is null.");
			return;
		}
		String resp="";
		resp="Entry Successfully Added!";
		if("Number".equals(type))
		{
			float f=0;
			try {
			 f=	 Float.parseFloat(value);
			 sm.store(key,f);
			}catch(Exception e)
			{
				resp="String can't be converted in float,Please ! Enter Float.";
			}
			
		}else
		{
			sm.store(key, value);
		}
		
		pw.write(resp);
	}
	
	 public void destroy() {
		 serializeStoreManager();
	    }
	 
	 public static boolean serializeStoreManager()
	 {
		 boolean isSuccessful=true;
		 if(!Files.exists(filePath))
		 {	
			 try {
				Files.createFile(filePath);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 try(FileOutputStream fos = new FileOutputStream(filePath.toAbsolutePath().toString());ObjectOutputStream oos = new ObjectOutputStream(fos);) {
				 	oos.writeObject(sm);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				isSuccessful=false;
				e.printStackTrace();
			}
		 }
		 return isSuccessful;
	 }
	 
	 public static void clear()
	 {
		 sm.clear();
	 }
	 
	 public static boolean  deserializeStoreManager()
	 {
		 boolean isSuccessful=true;
		 try(FileInputStream fos = new FileInputStream(filePath.toAbsolutePath().toString());ObjectInputStream ois = new ObjectInputStream(fos);) {
			 sm = (StoreManager)ois.readObject();
			 sm.printAll();
		} catch (IOException |ClassNotFoundException e) {
			// TODO Auto-generated catch block
			isSuccessful=false;
			e.printStackTrace();
		}
		 
		return isSuccessful;
		
		 
	 }

}
