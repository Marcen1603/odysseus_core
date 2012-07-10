/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.test;

import java.lang.reflect.Method;

public class TestUtil {

	public static Object invoke( String methodName, Class<?> clazz, Object... parameters ) throws Throwable {
		try {
			Class<?>[] parameterClasses = getClasses(parameters);
			Method m = getMethod(methodName, clazz, parameterClasses);
			
			m.setAccessible(true);
			
			return m.invoke(m, parameters);
		} catch( Exception ex ) {
			if( ex.getCause() != null ) {
				throw ex.getCause();
			} else {
				throw ex;
			}
		}		
	}
	
	public static Object invoke( String methodName, Object object, Object... parameters ) throws Throwable {
		try {
			Class<?>[] parameterClasses = getClasses(parameters);
			Method m = getMethod(methodName, object.getClass(), parameterClasses);
			m.setAccessible(true);
			
			return m.invoke(object, parameters);
		} catch( Exception ex ) {
			if( ex.getCause() != null ) {
				throw ex.getCause();
			} else {
				throw ex;
			}
		}
	}

	private static Class<?>[] getClasses(Object[] parameters) {
		Class<?>[] classes = new Class<?>[parameters.length];
		for( int i = 0; i < classes.length; i++ ) {
			if( parameters[i] != null ) {
				classes[i] = parameters[i].getClass();
			} else {
				classes[i] = Object.class;
			}
		}
		return classes;
	}
	
	private static Method getMethod( String name, Class<?> clazz, Class<?>[] parameterTypes ) throws Exception {
		try {
			return clazz.getDeclaredMethod(name, parameterTypes);
		} catch( NoSuchMethodException ignored ) {
			
			// Happens with null-parameters
			Method[] methods = clazz.getDeclaredMethods();
			
			Method foundMethod = null;
			for( int i = 0; i < methods.length; i++ ) {
				if( methods[i].getName().equals(name)) {
					if( foundMethod != null ) {
						throw new Exception("Method " + name + " of class " + clazz + " is ambiguous to invoke it easily.");
					}
					foundMethod = methods[i];
				}
			}
			if( foundMethod == null ) {
				throw new NoSuchMethodException(name + " of class " + clazz + " not found!");
			}
			
			return foundMethod;
		}
	}
}
