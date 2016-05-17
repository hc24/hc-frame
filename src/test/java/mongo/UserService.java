package mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.hc24.frame.spring.SpringFactory;
import com.hc24.frame.spring.dao.bean.Bean;
import com.hc24.frame.spring.dao.bean.DataGridModel;
import com.hc24.frame.spring.dao.bean.Page;
import com.hc24.frame.spring.service.BeanService;
import com.hc24.frame.util.StringUtil;

@Service
public class UserService extends BeanService<String> {

	public Page<Bean> query(Bean param,DataGridModel dgm){
		
		List<Bson> bsonL=new ArrayList<Bson>();
		if(!StringUtil.isEmpty(param.getString("username"))){
			bsonL.add(eq("username", param.getString("username")));
		}
		
		Bson filter=and(bsonL);
		return getMongoTemplate().queryBeanByPage(filter, dgm.getRows(), dgm.getPage(), getTable());
	}
	
	/**
	 * 增长年龄
	 * @param id
	 */
	public void incAge(String id){
		
		getMongoTemplate().update(eq("_id", new ObjectId(id)), 
				new Document("$inc", new Document("age", 1)), 
				getTable());
	}
	
	@Override
	protected String getTable() {
		return "users";
	}

	
	public static void main(String[] args) {
		Bean user=new Bean();
		user.set("username", "周恩来");
		user.set("sex", "伟人");
		user.set("age", 74);
		user.set("remark", "鞠躬尽瘁死而后已");
		
		UserService us= (UserService) SpringFactory.getBean("userService");
		//新增
		us.save(user);
		
		//查询
		Bean param=new Bean();
		param.set("username", "科比");
		DataGridModel dgm=new DataGridModel();
		dgm.setRows(10);
		dgm.setPage(1);
		Page<Bean> page= us.query(param, dgm);
		System.out.println(page.getRows());
		
		//更新
		Bean newUser=new Bean();
		newUser.set("_id", "569cb07fcc83e30708b74eda");
		newUser.set("age", 100);
		us.update(newUser);
		
		//更新$inc
		us.incAge("569cb07fcc83e30708b74eda");
		
 		Bean bean=us.getById("569cb07fcc83e30708b74eda");
 		System.out.println("更新后的文档是："+bean);
	}
}
