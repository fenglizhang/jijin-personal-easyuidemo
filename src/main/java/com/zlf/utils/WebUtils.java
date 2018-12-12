package com.zlf.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {
	public static Map getParamAsMap(HttpServletRequest request){
		Map map=new HashMap();
		Map map2 = request.getParameterMap();
		Iterator iterator = map2.keySet().iterator();
		while (iterator.hasNext()) {
			String key =  (String) iterator.next();
			String[] value=(String[])map2.get(key);
			if(value !=null && value.length>1){
				map.put(key, value);
			}else if(value!=null && value.length==1){
				map.put(key, value[0]);
			}
			
		}
		return map;
	}
}
