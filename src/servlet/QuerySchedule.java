package servlet;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import bean.Course;
import bean.Schedule;
import bean.Time;
import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class QuerySchedule
 */
@WebServlet("/querySchedule")
public class QuerySchedule extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuerySchedule() {
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
		String studentId = json.getString("studentId");
System.out.println(studentId);		
		
		JSONObject object =new JSONObject();
		DataAccessInterface<Schedule> scheduleDAC = DACFactory.getInstance().createDAC(Schedule.class);
		
		Condition<Schedule> condition = new Condition<Schedule>() {
			@Override
			public boolean assertBean(Schedule schedule) {
				
				return schedule.getStudentId().equals(studentId);
			}
		};
		if(scheduleDAC.selectByCondition(condition).size() == 0){
			object.put("success", false);
			object.put("failReason", "学号不存在");
		}
		
		else{
			object.put("success", true);
			
			JSONObject course =new JSONObject();
			JSONObject time =new JSONObject();
			for(Schedule s : scheduleDAC.selectByCondition(condition)){
				int size = s.getCourses().size();
				for(int i = 0; i<size; i++){
					
					course.put("courseId", s.getCourses().get(i).getCourseId());
					course.put("scholName", s.getCourses().get(i).getSchoolName());
					course.put("courseName", s.getCourses().get(i).getCourseName());
					course.put("teacherName", s.getCourses().get(i).getTeacherName());
					course.put("credit", s.getCourses().get(i).getCredit());
					course.put("location", s.getCourses().get(i).getLocation());
					course.put("time", time);
					time.put("weekday", s.getCourses().get(i).getTime().getWeekday());
					time.put("period", s.getCourses().get(i).getTime().getPeriod());
					course.put("capacity", s.getCourses().get(i).getCapacity());	
					object.put("", course);
					time.clear();
					course.clear();
				}
			}
			
		}
		response.setContentType("textml;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");	   
		JSONArray jsonArray = JSONArray.fromObject(object);
		response.getWriter().print(jsonArray.toString());		 
	}
	}

