package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bean.Course;
import bean.Drop;
import bean.Select;
import bean.Student;
import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;

/**
 * Servlet implementation class AddSchoolServlet
 */
@WebServlet("/dropCourse")
public class DropCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DropCourse() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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
System.out.println(sb.toString());		
		Drop drop= (Drop) JSON.parseObject(sb.toString(), Drop.class);
		String studentId = drop.getStudentId();
		String courseId = drop.getCourseId();

		// 根据add的返回值以json格式返回结果信息
		PrintWriter out = response.getWriter();
		JSONObject object = new JSONObject();
		DataAccessInterface<Student> studentDAC = DACFactory.getInstance()
				.createDAC(Student.class);
		DataAccessInterface<Course> courseDAC = DACFactory.getInstance()
				.createDAC(Course.class);
		DataAccessInterface<Select> selectDAC = DACFactory.getInstance()
				.createDAC(Select.class);
		studentDAC.beginTransaction();
		courseDAC.beginTransaction();
		selectDAC.beginTransaction();

		// 判断输入的学生id是否存在
		Condition<Student> studentCondition = new Condition<Student>() {

			@Override
			public boolean assertBean(Student student) {

				return student.getStudentId().equals(studentId);
			}

		};

		// 判断输入的院系id是否存在
		Condition<Course> courseCondition = new Condition<Course>() {

			@Override
			public boolean assertBean(Course course) {
				if (course.getCourseId().equals(courseId)) {
					return true;
				}
				return false;
			}

		};

		//判断学生是否选过该课程
		Condition<Select> selectCondition = new Condition<Select>() {

			@Override
			public boolean assertBean(Select select) {
				if (select.getStudentId().equals(studentId)
						&& select.getCourseId().equals(courseId)) {
					return true;
				}
				return false;
			}

		};
		if (studentDAC.selectByCondition(studentCondition).size() == 1) {
			if (courseDAC.selectByCondition(courseCondition).size() == 1) {
				if (selectDAC.selectByCondition(selectCondition).size() == 1) {
					selectDAC.deleteByCondition(selectCondition);
				} else {
					object.put("success", false);
					object.put("failReason", "学生未选课");
				}
			} else {
				object.put("success", false);
				object.put("failReason", "课程不存在");
			}
		} else {
			object.put("success", false);
			object.put("failReason", "学生不存在");
		}

		// System.out.println(object.toString());
		out.write(object.toString());
		out.flush();
		out.close();

		studentDAC.commit();
		courseDAC.commit();
		selectDAC.commit();

	}

}
