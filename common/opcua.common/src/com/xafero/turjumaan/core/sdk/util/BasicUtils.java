/*******************************************************************************
 * Copyright 2016 Georg Berendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.xafero.turjumaan.core.sdk.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

/**
 * The basic utilities.
 */
public final class BasicUtils {

	/** The Constant JAVA_LIBRARY_PATH. */
	private static final String JAVA_LIBRARY_PATH = "java.library.path";

	/**
	 * Instantiates a new basic utilities.
	 */
	private BasicUtils() {
	}

	/**
	 * Convert a string to URI without an exception.
	 *
	 * @param uri
	 *            the URI as string
	 * @return the real URI
	 */
	public static URI toURI(String uri) {
		try {
			return new URI(uri);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the class from its qualified name or not.
	 *
	 * @param className
	 *            the class name
	 * @return the class or nothing
	 */
	public static Class<?> getClassOrNot(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	/**
	 * Gets the field from its name or not.
	 *
	 * @param clazz
	 *            the class
	 * @param fieldName
	 *            the field name
	 * @return the field or nothing
	 */
	public static Field getFieldOrNot(Class<?> clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	/**
	 * Gets the value of a field.
	 *
	 * @param obj
	 *            the object
	 * @param fieldName
	 *            the field name
	 * @return the field's value
	 */
	public static Object get(Object obj, String fieldName) {
		try {
			Field field = getFieldOrNot(obj.getClass(), fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	/**
	 * Gets the method by its name or not.
	 *
	 * @param clazz
	 *            the class
	 * @param methodName
	 *            the method name
	 * @param args
	 *            the arguments
	 * @return the method or nothing
	 */
	public static Method getMethodOrNot(Class<?> clazz, String methodName, Object... args) {
		try {
			// If no arguments, it's simple
			if (args == null || args.length == 0)
				try {
					return clazz.getMethod(methodName);
				} catch (NoSuchMethodException nsme) {
					Method meth = clazz.getDeclaredMethod(methodName);
					meth.setAccessible(true);
					return meth;
				}
			// Get internal method
			Class<?>[] types = new Class<?>[args.length];
			for (int i = 0; i < types.length; i++)
				types[i] = args[i].getClass();
			Method meth;
			try {
				meth = clazz.getDeclaredMethod(methodName, types);
				meth.setAccessible(true);
				return meth;
			} catch (NoSuchMethodException nsme) {
				for (Method m : clazz.getDeclaredMethods())
					if (m.getName().equals(methodName) && m.getParameterTypes().length == args.length)
						return m;
				throw nsme;
			}
		} catch (NoSuchMethodException | SecurityException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	/**
	 * Invoke a method just by providing strings.
	 *
	 * @param className
	 *            the class name
	 * @param methodName
	 *            the method name
	 * @return the result object
	 */
	public static Object invoke(String className, String methodName) {
		try {
			return getMethodOrNot(getClassOrNot(className), methodName).invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	/**
	 * Invoke a method with arguments.
	 *
	 * @param obj
	 *            the object
	 * @param methodName
	 *            the method name
	 * @param args
	 *            the arguments
	 * @return the result object
	 */
	public static Object invoke(Object obj, String methodName, Object... args) {
		try {
			return getMethodOrNot(obj.getClass(), methodName, args).invoke(obj, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	/**
	 * Gets the enumeration by its name and number.
	 *
	 * @param enumType
	 *            the enumeration type
	 * @param ordinal
	 *            the ordinal
	 * @return the enumeration
	 */
	public static Enum<?> getEnum(String enumType, int ordinal) {
		return getEnum(getClassOrNot(enumType), ordinal);
	}

	/**
	 * Gets the enumeration by its class and number.
	 *
	 * @param enumType
	 *            the enumeration type
	 * @param ordinal
	 *            the ordinal
	 * @return the enumeration
	 */
	private static Enum<?> getEnum(Class<?> enumType, int ordinal) {
		return (Enum<?>) enumType.getEnumConstants()[ordinal];
	}

	/**
	 * Convert an iterable to a list.
	 *
	 * @param <T>
	 *            the generic type
	 * @param items
	 *            the items
	 * @return the list
	 */
	public static <T> List<T> toList(Iterable<T> items) {
		List<T> list = new LinkedList<T>();
		for (T item : items)
			list.add(item);
		return list;
	}

	/**
	 * Delete a folder.
	 *
	 * @param folder
	 *            the folder
	 */
	public static void deleteFolder(File folder) {
		if (!folder.exists())
			return;
		for (File file : folder.listFiles())
			if (file.isDirectory())
				deleteFolder(file);
			else
				file.delete();
		folder.delete();
	}

	/**
	 * Gets the native library path.
	 *
	 * @return the native library path
	 */
	public static String getNativeLibraryPath() {
		return System.getProperty(JAVA_LIBRARY_PATH);
	}

	/**
	 * Sets the native library path.
	 *
	 * @param path
	 *            the new native library path
	 */
	public static void setNativeLibraryPath(File path) {
		try {
			System.setProperty(JAVA_LIBRARY_PATH, path.getAbsolutePath());
			Field sysPath = ClassLoader.class.getDeclaredField("sys_paths");
			sysPath.setAccessible(true);
			sysPath.set(null, null);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new UnsupportedOperationException(e);
		}
	}
}