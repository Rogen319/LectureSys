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
import bean.Select;
import bean.Student;
import bean.Time;
import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class AddselectServlet
 */
@WebServlet("/selectCourse")
public class SelectCourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int courseCredit = 0;
	int sumCredit = 0;
	int courseCapacity = 0;
	int sumCapacity = 0;
	Time courseTime;
	boolean timeConflict = false;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SelectCourseServlet() {
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

		//��������Jason��ʽ�ַ���
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
		Select select = (Select)JSON.parseObject(sb.toString(),Select.class);
		//System.out.println(select.getCreditRequirement() + "    " + select.getselectName());
		String studentId = select.getStudentId();
		String courseId = select.getCourseId();
		//����add�ķ���ֵ��json��ʽ���ؽ����Ϣ
		PrintWriter out=response.getWriter();
		JSONObject object =new JSONObject();
		DataAccessInterface<Student> studentDAC = DACFactory.getInstance().createDAC(Student.class);
		DataAccessInterface<Course> courseDAC = DACFactory.getInstance().createDAC(Course.class);
		DataAccessInterface<Select> selectDAC = DACFactory.getInstance().createDAC(Select.class);
		studentDAC.beginTransaction();
		courseDAC.beginTransaction();
		
		//�ж������Ժϵ�����Ƿ����
		Condition<Student> studentCondition = new Condition<Student>() {

			@Override
			public boolean assertBean(Student student) {
				
				return student.getStudentId().equals(studentId);
			}
			
		};
		Condition<Course> courseCondition = new Condition<Course>() {

			@Override
			public boolean assertBean(Course course) {
				if(course.getCourseId().equals(courseId)){
					courseCredit = course.getCredit();
					courseCapacity = course.getCapacity();
					courseTime = course.getTime();
					return true;
				}
				return false;
			}
			
		};
		Condition<Select> selectCondition = new Condition<Select>() {

			@Override
			public boolean assertBean(Select select) {
				if(select.getStudentId().equals(studentId)){
					sumCredit += select.getCredit();	
					if(select.getTime().getPeriod() == courseTime.getPeriod() && select.getTime().getWeekday() == courseTime.getWeekday()){
						timeConflict= true;
					}
				}
				if(select.getCourseId().equals(courseId)){
					sumCapacity += 1;
				}
				return false;
			}
			
		};
		if(studentDAC.selectByCondition(studentCondition).size() == 1){
			if(courseDAC.selectByCondition(courseCondition).size() == 1){
				selectDAC.selectByCondition(selectCondition);//������ѧ�ֺ���ѡ������
				if(sumCredit + courseCredit <= 30){
					if(sumCapacity <= courseCapacity){
						if(!timeConflict){
							select.setCredit(courseCredit);
							select.setTime(courseTime);
							selectDAC.add(select);
						}
						else{
							object.put("success", false);
							object.put("failReason", "ѡ��ʱ��ص��ͻ");
						}
					}
					else{
						object.put("success", false);
						object.put("failReason", "ѡ����������");
					}
				}
				else{
					object.put("success", false);
					object.put("failReason", "ѧ������");
				}
			}
			else{
				object.put("success", false);
				object.put("failReason", "�γ̲�����");
			}
		}
		else{
			object.put("success", false);
			object.put("failReason", "ѧ��������");
		}

		//System.out.println(object.toString());
		out.write(object.toString());
		out.flush();
		out.close();

		studentDAC.commit();
		courseDAC.commit();
		selectDAC.commit();
	}

}

