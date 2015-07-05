package function;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import rocket.Producer;
import bean.School;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.exception.MQClientException;

public class MyManager {
	
	private static long count = 0;
	private static HashMap<Long, String> map = new HashMap<Long, String>();
	public static Object lockObject = new Object();

	public static synchronized long  dispatchID(){	
		return count++;
	}

	public static synchronized String getMap(long id,String status) {
		if(status == null){
			return map.get(id);
		}
		else{
			map.put(id, status);
			return status;
		}
	}

	public static void dispatchWork (long id,HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("textml");

		//获得输入的json格式字符串
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

		} 
		catch (Exception e) {
		}

		//解析json
		School school = (School)JSON.parseObject(sb.toString(),School.class);
		String schoolName = school.getSchoolName();
		
	    try {
			Producer p = new Producer("addSchoolConflictionProducer","G4-addSchoolConfliction","TagAll",""+id,schoolName);
		} catch (MQClientException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(MyManager.getMap(id,null));
		
	   /* while(("0").equals(MyManager.getMap(id,null))){
	    	System.out.println(MyManager.getMap(id,null));
	    	try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    }*/
	    while(true){
	    	synchronized (MyManager.lockObject) {
	    		while(("0").equals(MyManager.getMap(id,null))){
	    			try {
	    				System.out.println("yes");
	    				(MyManager.lockObject).wait();
	    				System.out.println("yes1");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}

				System.out.println("yes1");
	    	}
	    	System.out.println(MyManager.getMap(id,null));
		    if(("1").equals(MyManager.getMap(id,null))){//表示没有冲突的情况
		    	try {
					Producer p2 = new Producer("addSchoolProducer","G4-addSchool","TagA",""+id,schoolName);
				} catch (MQClientException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    	PrintWriter out = response.getWriter();
		    	JSONObject object = new JSONObject();
		    	object.put("success", true);
				object.put("failReason", "");
				out.write(object.toString());
				out.flush();
				out.close();
		    	
		    }else if(("2").equals(MyManager.getMap(id,null))){//表示存在冲突，院系已经存在
		    	PrintWriter out = response.getWriter();
		    	JSONObject object = new JSONObject();
		    	object.put("success", false);
				object.put("failReason", "院系名称已存在");
				out.write(object.toString());
				out.flush();
				out.close();
		    }
	    }
	    
	}

	public static HashMap<Long, String> getMap() {
		return map;
	}

	public static void setMap(HashMap<Long, String> map) {
		MyManager.map = map;
	}

	/*public static synchronized void setMap(long id,String status) {
		map.put(id, status);
	}*/
	
	
}
