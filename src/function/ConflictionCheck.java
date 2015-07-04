package function;

import bean.School;
import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;

public class ConflictionCheck {
	
	public boolean addSchoolConfliction(String schoolName){
		boolean isExist = false;
		DataAccessInterface<School> schoolDAC = DACFactory.getInstance().createDAC(School.class);
		schoolDAC.beginTransaction();
		Condition<School> condition = new Condition<School>() {
			@Override
			public boolean assertBean(School school) {
				return school.getSchoolName().equals(schoolName);
			}
		};
		if(schoolDAC.selectByCondition(condition).size() != 0){
			isExist = true;
		}
		return isExist;
	}
}
