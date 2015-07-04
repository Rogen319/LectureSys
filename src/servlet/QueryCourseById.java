package servlet;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bean.Course;
import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;

/**
 * Servlet implementation class QueryCourseById
 */
@WebServlet("/queryCourseById")
public class QueryCourseById extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryCourseById() {
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
		
		JSONObject json = JSON.parseObject(sb.toString());
		String courseId = json.getString("courseId");
System.out.println(courseId);		
		JSONObject object = new JSONObject();
		DataAccessInterface<Course> courseDAC = DACFactory.getInstance().createDAC(Course.class);
		Condition<Course> condition = new Condition<Course>() {
			@Override
			public boolean assertBean(Course course) {
				
				return course.getCourseId().equals(courseId);
			}
		};
		if(courseDAC.selectByCondition(condition).size() == 0){
			object.put("success", false);
			object.put("failReason", "null");
		}
		else{
			object.put("success", true);
			JSONObject time =new JSONObject();
			for(Course c : courseDAC.selectByCondition(condition)){
				object.put("courseId", c.getCourseId());
				object.put("scholName", c.getSchoolName());
				object.put("courseName", c.getCourseName());
				object.put("teacherName", c.getTeacherName());
				object.put("credit", c.getCredit());
				object.put("location", c.getLocation());
				object.put("time", time);
				time.put("weekday", c.getTime().getWeekday());
				time.put("period", c.getTime().getPeriod());
				object.put("capacity", c.getCapacity());	
			}
			
		}
		response.setContentType("textml;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");	   
		JSONArray jsonArray = JSONArray.fromObject(object);
		response.getWriter().print(jsonArray.toString());		 
	}

}
