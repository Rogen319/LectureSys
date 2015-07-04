package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bean.Course;
import bean.Time;
import bean.WrapCourse;
import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class AddCourseServlet
 */
@WebServlet("/addCourseInfo")
public class AddCourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddCourseServlet() {
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
		// TODO Auto-generated method stub
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
		
		//����json��Ȼ����Course����
		Course course = JSON.parseObject(sb.toString(), WrapCourse.class).getCourse();
		
		Time time = course.getTime();
		String courseId = course.getCourseId();
		System.out.println("courseId="+courseId);		
		String teacherName = course.getTeacherName();	
		String location = course.getLocation();
				
		//����add�ķ���ֵ��json��ʽ���ؽ����Ϣ
		PrintWriter out=response.getWriter();
		JSONObject object =new JSONObject();
		DataAccessInterface<Course> courseDAC = DACFactory.getInstance().createDAC(Course.class);
		courseDAC.beginTransaction();

		//�ж������ѡ�κ��Ƿ����
		Condition<Course> condition1 = new Condition<Course>() {

			@Override
			public boolean assertBean(Course course) {

				return course.getCourseId().equals(courseId);
			}

		};

		Condition<Course> condition2 = new Condition<Course>() {

			@Override
			public boolean assertBean(Course course) {

				return course.getTime().getWeekday() == time.getWeekday()&&course.getTime().getPeriod()==time.getPeriod()&&course.getLocation().equals(location);
			}

		};

		Condition<Course> condition3 = new Condition<Course>() {

			@Override
			public boolean assertBean(Course course) {

				return course.getTeacherName().equals(teacherName)&&course.getTime().getWeekday()==time.getWeekday()&&course.getTime().getPeriod()==time.getPeriod();
			}

		};
		if(courseDAC.selectByCondition(condition1).size() != 0 ){
			object.put("success", false);
			object.put("failReason", "ѡ�κ��Ѵ���");
		}
		else if((courseDAC.selectByCondition(condition2).size() != 0 )){
			object.put("success", false);
			object.put("failReason", "ʱ��ص��ͻ");
		}
		else if((courseDAC.selectByCondition(condition3).size() != 0 )){
			object.put("success", false);
			object.put("failReason", "��ʦʱ���ͻ");
		}
		else{
			courseDAC.add(course);
			object.put("success", true);
			object.put("failReason", "");
		}

		System.out.println(object.toString());
		out.write(object.toString());
		out.flush();
		out.close();

		System.out.println(courseDAC.commit());
	}

}
