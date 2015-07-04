package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Course;
import bean.School;
import bean.Select;
import bean.Student;
import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;

/**
 * Servlet implementation class AddSchoolServlet
 */
@WebServlet("/clearData")
public class ClearData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ClearData() {
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

		DataAccessInterface<Student> studentDAC = DACFactory.getInstance()
				.createDAC(Student.class);
		DataAccessInterface<Course> courseDAC = DACFactory.getInstance()
				.createDAC(Course.class);
		DataAccessInterface<School> schoolDAC = DACFactory.getInstance()
				.createDAC(School.class);
		DataAccessInterface<Select> selectDAC = DACFactory.getInstance()
				.createDAC(Select.class);

		studentDAC.beginTransaction();
		courseDAC.beginTransaction();
		schoolDAC.beginTransaction();
		selectDAC.beginTransaction();

		Condition<Student> studentCondition = new Condition<Student>() {
			@Override
			public boolean assertBean(Student student) {
				return true;
			}

		};
		studentDAC.deleteByCondition(studentCondition);

		Condition<Course> courseCondition = new Condition<Course>() {
			@Override
			public boolean assertBean(Course course) {
				return true;
			}

		};
		courseDAC.deleteByCondition(courseCondition);
		
		Condition<School> schoolCondition = new Condition<School>() {
			@Override
			public boolean assertBean(School school) {
				return true;
			}

		};
		schoolDAC.deleteByCondition(schoolCondition);

		Condition<Select> selectCondition = new Condition<Select>() {
			@Override
			public boolean assertBean(Select select) {
				return true;
			}

		};
		selectDAC.deleteByCondition(selectCondition);

		studentDAC.commit();
		courseDAC.commit();
		schoolDAC.commit();
		selectDAC.commit();
	}

}
