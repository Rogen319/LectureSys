package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");

		//��������Json��ʽ�ַ���
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

		//����Jason
		School school = (School)JSON.parseObject(sb.toString(),School.class);
		String schoolName = school.getSchoolName();
		
	    try {
			Producer p = new Producer("addSchoolConflictionProducer","G4-addSchoolConfliction","TagAll",schoolName);
		} catch (MQClientException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    
		
/*
		//����add�ķ���ֵ��json��ʽ���ؽ����Ϣ
		PrintWriter out = response.getWriter();
		JSONObject object = new JSONObject();
		DataAccessInterface<School> schoolDAC = DACFactory.getInstance().createDAC(School.class);
		schoolDAC.beginTransaction();

		//�ж������Ժϵ�����Ƿ����
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
			object.put("failReason", "Ժϵ�����Ѵ���");
		}

		System.out.println(object.toString());
		out.write(object.toString());
		out.flush();
		out.close();

		System.out.println(schoolDAC.commit());*/
	}

}
