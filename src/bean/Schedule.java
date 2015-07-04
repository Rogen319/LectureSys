package bean;

import java.util.ArrayList;

public class Schedule {
	private String studentId;
	private ArrayList<Course> courses;
	public Schedule(String studentId, ArrayList<Course> courses) {
		super();
		this.studentId = studentId;
		this.courses = courses;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public ArrayList<Course> getCourses() {
		return courses;
	}
	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}

}
