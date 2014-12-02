package com.mofang.framework.data.mysql.core.meta;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 
 * @author zhaodx
 *
 */
public class FieldConvertor
{
	private final static char SEPARATOR = '_';

	/**
	 * 驼峰转化下划线
	 * 
	 * @param s
	 * @return
	 */
	public static String toUnderScoreCase(String s)
	{
		if (s == null)
			return null;
		
		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			boolean nextUpperCase = true;
			if (i < (s.length() - 1))
				nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
			
			if ((i > 0) && Character.isUpperCase(c))
			{
				if (!upperCase || !nextUpperCase)
					sb.append(SEPARATOR);
				upperCase = true;
			}
			else 
				upperCase = false;

			sb.append(Character.toLowerCase(c));
		}
		return sb.toString();
	}

	/**
	 * 下划线转驼峰
	 * 
	 * @param s
	 * @return
	 */
	public static String toCamelCase(String s)
	{
		if (s == null)
			return null;
		
		s = s.toLowerCase();
		StringBuilder sb = new StringBuilder(s.length());
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if (c == SEPARATOR)
				upperCase = true;
			else if (upperCase)
			{
				sb.append(Character.toUpperCase(c));
				upperCase = false;
			}
			else
				sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 下划线转驼峰，首字母大写
	 * 
	 * @param s
	 * @return
	 */
	public static String toCapitalizeCamelCase(String s)
	{
		if (s == null)
			return null;
		
		s = toCamelCase(s);
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends
	 * GenricManager<Book>
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or <code>Object.class</code> if
	 *         cannot be determined
	 */
	public static Class<?> getSuperClassGenricType(Class<?> clazz)
	{
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends
	 * GenricManager<Book>
	 * 
	 * @param clazz
	 *            clazz The class to introspect
	 * @param index
	 *            the Index of the generic ddeclaration,start from 0.
	 */
	public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) throws IndexOutOfBoundsException 
	{
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType))
			return Object.class;

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0)
			return Object.class;
		if (!(params[index] instanceof Class<?>))
			return Object.class;
		
		return (Class<?>) params[index];
	}
}