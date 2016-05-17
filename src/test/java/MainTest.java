

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.hc24.frame.spring.dao.bean.Bean;

public class MainTest {

	public static void main(String[] args) {
		
		List<Object> list=new ArrayList<Object>();
		list.add("sss");
		list.add(123.00f);
		list.add(new Date());
		
		Bean bean=new Bean();
		bean.set("s", "sss").set("f", 123.00f).set("d", new Date());
		
		Iterator<String> it= bean.keySet().iterator();
		while(it.hasNext()){
			String key=it.next();
			list.add(bean.get(key));
		}
		
		System.out.println(list.toString());
		
	}
}
