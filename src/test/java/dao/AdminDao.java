package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import bean.User;

@Repository
public class AdminDao {
	//JdbcTemplate的包装类，提供命名占位符，而非？号方式
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	//NamedParameterJdbcTemplate的包装类
	@Autowired
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	public int countOfName(String name){
		String sql="select count(1) from admin_info where admin_name=:admin_name";
		
		SqlParameterSource sps=new MapSqlParameterSource("admin_name", name);
		
		return namedParameterJdbcTemplate.queryForInt(sql, sps);
	}
	
	public int countOfName(User user){
		String sql="select count(1) from admin_info where admin_name=:adminName";
		
		SqlParameterSource sps=new BeanPropertySqlParameterSource(user);
		
		return namedParameterJdbcTemplate.queryForInt(sql, sps);
	}
	
	public List<User> findUser(String name){
		String sql="select * from admin_info where admin_name=:name";
		
		//定义一个RowMapper
		ParameterizedRowMapper<User> mapper=new ParameterizedRowMapper<User>() {	
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user=new User();
				user.setAdminId(rs.getString("admin_id"));
				user.setRealName(rs.getString("real_name"));
				user.setValidTime(rs.getDate("valid_time"));
				
				return user;
			}
		};
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("name", name);
		
		return this.namedParameterJdbcTemplate.query(sql, paramMap, mapper);
		//return this.simpleJdbcTemplate.query(sql, mapper, name);
	}
	
	public void tt(){
		
	}
	
	public static void main(String[] args) {
		AdminDao ad= (AdminDao) SpringFactory.getBean("adminDao");
		int n= ad.countOfName("admin");
		System.out.println(n);
		
		User user=new User();
		user.setAdminName("who");
		n=ad.countOfName(user);
		System.out.println(n);
		
		List<User> list=ad.findUser("who");
		System.out.println(list);
	}
}
