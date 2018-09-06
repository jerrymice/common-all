package com.github.jerrymice.common.entity.adpter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author tumingjian
 * 可以根据TableField为JSON字段生成注释内容
 * @param <T>  泛型基本类型
 */
public class JSONCommentTypeAdapter<T> extends TypeAdapter<T> {
	/**
	 *写入属性value值
	 * @param out  写入者
	 * @param value 要写入的原始值
	 * @throws IOException 抛出IO异常
	 * @see TypeAdapter
     */
    @Override
    public void write(JsonWriter out, T value) throws IOException {
    	try {
			Field field = out.getClass().getDeclaredField("deferredName");
			field.setAccessible(true);	
			String str = field.get(out).toString();
			String[] val = str.split("\\|");
			field.set(out, null);
			String name = val[0];
			if(val.length==1){
				out.name(name);
				out.value("null");
			}
			if(val.length>1){
				out.name(name);
				String desc = val[1];
				out.value(",//"+desc);
			}
			field.set(out, null);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	/**
	 *
	 * @param in 读取者
	 * @return   返回读取的内容
	 * @throws IOException 读取异常时抛出
	 * @see TypeAdapter
     */
    @Override
    public T read(JsonReader in) throws IOException {
    	return null;
        
    }
}
