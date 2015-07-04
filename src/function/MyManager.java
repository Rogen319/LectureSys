package function;

import java.util.HashMap;

public class MyManager {
	
	private static long count = 0;
	private static HashMap<Long, String> map = new HashMap<Long, String>();
	
	public static synchronized long  dispatchID(){	
		return count++;
	}

	public static synchronized String getMap(long id) {
		return map.get(id);
	}

	public static synchronized void setMap(long id,String status) {
		map.put(id, status);
	}
	
	
}
