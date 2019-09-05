package com.cgiser.moka.manager.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.cgiser.moka.model.LegionLevel;

public class BeanUtils {
	public static Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}
	public static Object setFieldValueByName(String fieldName, Object o,Object[] vals) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String setter = "set" + firstLetter + fieldName.substring(1);
			Class[] classes = new Class[vals.length];
			for(int i=0;i<vals.length;i++){
				classes[i] = vals[i].getClass();
			}
			Method method = o.getClass().getMethod(setter, classes);
			Object value = method.invoke(o, vals);
			return value;
		} catch (Exception e) {
			return null;
		}
	}
	public static Object setFieldValueByName(String fieldName, Object o,Object[] vals,Class[] classes) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String setter = "set" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(setter, classes);
			Object value = method.invoke(o, vals);
			return value;
		} catch (Exception e) {
			return null;
		}
	}
	public static void main(String[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		LegionLevel legionLevel = new LegionLevel();
		Method method = legionLevel.getClass().getMethod("setTech1",new Class[]{int.class});
		Object value = method.invoke(legionLevel, new Object[]{1});
	}
}
