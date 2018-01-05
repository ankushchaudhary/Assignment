package com.mitter.core;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class StoreManager implements Serializable{
	HashMap<String,Float> floatMap=null; // this is for storing String and FLoat .
	HashMap<String,String> stringMap=null;// this is for storing String and String.
	
	public StoreManager()
	{
		floatMap=new HashMap<>();
		stringMap=new HashMap<>();
	}
	
	public boolean store(String key,Object value)
	{
		if(key==null)
			return false;
		if(value.getClass()  == String.class ) // condition for checking value type whether string or not..
		{
			stringMap.put(key, (String)value);
			if(floatMap.containsKey(key))  // checking if a key is already exist, if exist it should removed
				floatMap.remove(key);
			
			//System.out.println("print string");
		}
		else 
		{
			floatMap.put(key, new Float(value.toString()));
			if(stringMap.containsKey(key))
				stringMap.remove(key);
			//System.out.println("print number");
		}
		return true;
	}
	
	public JSONArray get(String... keys)
	{
		if(keys.length==0)
			return null;
		JSONArray jsArray=new JSONArray();
		for(String key:keys)
		{
			//System.out.println(key);
			JSONObject js=new JSONObject();
			
			
			if(floatMap.containsKey(key))
			{
				//result.add(floatMap.get(key));
				js.put("key", key);
				js.put("value", floatMap.get(key)+"");
				 jsArray.put(js);
			}else if(stringMap.containsKey(key))
			{
				//result.add(stringMap.get(key));
				js.put("key", key);
				js.put("value", stringMap.get(key));
				 jsArray.put(js);
			}
			
		}
		
		return jsArray;
	}
	
	public JSONArray getAll()
	{
		JSONArray jsArray=new JSONArray();
		
			//System.out.println(key);
			
			floatMap.entrySet().stream().forEach(me->{   // getting all floatmap entry here
				JSONObject js=new JSONObject();
				js.put("key", me.getKey());
				js.put("value", me.getValue()+"");
				jsArray.put(js);
			});  
			
			stringMap.entrySet().stream().forEach(me->{  // getting all stringmap entry here..
				JSONObject js=new JSONObject();
				js.put("key", me.getKey());
				js.put("value", me.getValue());
				jsArray.put(js);
			});
			
			
		
		return jsArray;
	}
	
	
	public JSONArray queryGt(float num)
	{
		JSONArray jsArray=new JSONArray();
		 floatMap.entrySet().stream().filter(n->n.getValue()>num).forEach(n->{ //filter entries if value>num
			 JSONObject js=new JSONObject();
			 js.put("key", n.getKey());
			 js.put("value", n.getValue()+"");
			 jsArray.put(js);
		 });
		 return jsArray;
	}
	
	public JSONArray query(String key)
	{
		JSONArray jsArray=new JSONArray();
		Pattern p=Pattern.compile(key);
		 stringMap.entrySet().stream().filter(n->p.matcher((CharSequence)n.getKey()).matches()).forEach(n->{ // filter which match to regex.
			 JSONObject js=new JSONObject();
			 js.put("key", n.getKey());
			 js.put("value", n.getValue());
			 jsArray.put(js);
		 });
		 return jsArray;
				
	}
	
	public boolean clear() //clearing map entries..
	{
		boolean isSuccessful=true;
		try {
			floatMap.clear();
			stringMap.clear();
		}catch(Exception e)
		{
			isSuccessful=false;
		}
		return isSuccessful;
	}
	
	public int size()
	{
		return floatMap.size()+stringMap.size();
	}
	
	public boolean removeKey(String key)
	{
		boolean isRemoved=true;
		try {
		if(floatMap.containsKey(key))
		{
			floatMap.remove(key);
		}else if(stringMap.containsKey(key))
		{
			stringMap.remove(key);
		}
		}
		catch(Exception e)
		{
			isRemoved=false;
		}
		return isRemoved;
	}
	
	public void printAll()
	{
		stringMap.entrySet().parallelStream().forEach(n->System.out.println(n.getKey()+" -- > "+n.getValue()));
		floatMap.entrySet().parallelStream().forEach(n->System.out.println(n.getKey()+" -- > "+n.getValue()));
	}

}
