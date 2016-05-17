package com.hc24.frame.spring.web.controller.config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * 将Date类型的数据在转化成yyy-MM-dd HH:mm:ss的字符串格式<br/>
 * 然后在你的POJO上找到Date属性的get方法<br/>
 * 
 * <pre>
 * &#064;JsonSerialize(using = CustomDateSerializer.class)
 * public Date getCreateAt() {
 * 	return createAt;
 * }
 * </pre>
 * 
 * @author hc24
 * @version 2012-1-14
 */
public class CustomDateSerializer extends JsonSerializer<Date> {
	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = formatter.format(value);
		jgen.writeString(formattedDate);
	}
}