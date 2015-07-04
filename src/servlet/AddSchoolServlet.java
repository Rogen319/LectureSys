package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Manager;

import rocket.Producer;
import bean.School;
import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

import function.MyManager;

/**
 * Servlet implementation class AddSchoolServlet
 */
@WebServlet("/addSchoolInfo")
public class AddSchoolServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddSchoolServlet() {
		super();

		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get the id
		long id = MyManager.dispatchID();
		String status = "0";
		MyManager.setMap(id, status);
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("textml");

		//获得输入的Json格式字符串
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

		//解析Jason
		School school = (School)JSON.parseObject(sb.toString(),School.class);
		String schoolName = school.getSchoolName();
		
	    try {
			Producer p = new Producer("addSchoolConflictionProducer","G4-addSchoolConfliction","TagAll",""+id,schoolName);
		} catch (MQClientException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(MyManager.getMap(id));
	    while(("0").equals(MyManager.getMap(id))){
	    }
	    System.out.println(MyManager.getMap(id));
	    if(("1").equals(MyManager.getMap(id))){//表示没有冲突的情况
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
	    	
	    }else if(("2").equals(MyManager.getMap(id))){//表示存在冲突，院系已经存在
	    	PrintWriter out = response.getWriter();
	    	JSONObject object = new JSONObject();
	    	object.put("success", false);
			object.put("failReason", "院系名称已存在");
			out.write(object.toString());
			out.flush();
			out.close();
	    }
	    
	    
		
/*
		//根据add的返回值以json格式返回结果信息
		PrintWriter out = response.getWriter();
		JSONObject object = new JSONObject();
		DataAccessInterface<School> schoolDAC = DACFactory.getInstance().createDAC(School.class);
		schoolDAC.beginTransaction();

		//判断输入的院系名称是否存在
		Condition<School> condition = new Condition<School>() {
			@Override
			public boolean assertBean(School school) {
				return school.getSchoolName().equals(schoolName);
			}
		};
		if(schoolDAC.selectByCondition(condition).size() == 0 && schoolDAC.add(school)){
			object.put("success", true);
			object.put("failReason", "");
		}
		else{
			object.put("success", false);
			object.put("failReason", "院系名称已存在");
		}

		System.out.println(object.toString());
		out.write(object.toString());
		out.flush();
		out.close();

		System.out.println(schoolDAC.commit());*/
	}

}
