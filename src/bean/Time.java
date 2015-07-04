package bean;

public class Time {
	private int weekday;
	private int period;
	public Time(){
		
	}
	public Time(int nw,int wp){
		weekday = nw;
		period = wp;
	}
	public int getWeekday() {
		return weekday;
	}
	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	
}
