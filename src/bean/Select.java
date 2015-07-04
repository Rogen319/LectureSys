package bean;

public class Select {
	private String courseId;
	private String studentId;
	private int credit;
	private Time time;
	public Select(String courseId, String studentId, int credit, Time time) {
		super();
		this.courseId = courseId;
		this.studentId = studentId;
		this.credit = credit;
		this.time = time;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
}
