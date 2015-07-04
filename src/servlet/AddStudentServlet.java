package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Student;
import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class AddStudentServlet
 */
@WebServlet("/addStudentInfo")
public class AddStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddStudentServlet() {
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

		//获得输入的Jason格式字符串
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
		Student student = (Student)JSON.parseObject(sb.toString(),Student.class);
		String studentId = student.getStudentId();
		String schoolName = student.getSchoolName();

		//根据add的返回值以json格式返回结果信息
		PrintWriter out = response.getWriter();
		JSONObject object = new JSONObject();
		DataAccessInterface<Student> studentDAC = DACFactory.getInstance().createDAC(Student.class);
		DataAccessInterface<School> schoolDAC = DACFactory.getInstance().createDAC(School.class);
		studentDAC.beginTransaction();

		//判断输入的学生id是否存在
		Condition<Student> condition1 = new Condition<Student>() {

			@Override
			public boolean assertBean(Student student) {

				return student.getStudentId().equals(studentId);
			}

		};

		Condition<School> condition2 = new Condition<School>() {

			public boolean assertBean(School school) {

				return school.getSchoolName().equals(schoolName);
			}		
		};

		if(!(studentDAC.selectByCondition(condition1).size() == 0)){
			object.put("success", false);
			object.put("failReason", "学号已存在");

		}
		else if((schoolDAC.selectByCondition(condition2).size() == 0)){
			object.put("success", false);
			object.put("failReason", "院系不存在");
		}
		else{
			studentDAC.add(student);
			object.put("success", true);
			object.put("failReason", "");
		}

		System.out.println(object.toString());
		out.write(object.toString());
		out.flush();
		out.close();

		System.out.println(studentDAC.commit());
	}

}
