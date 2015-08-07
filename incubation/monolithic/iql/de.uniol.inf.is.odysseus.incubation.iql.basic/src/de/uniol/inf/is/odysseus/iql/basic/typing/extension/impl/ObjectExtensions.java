package de.uniol.inf.is.odysseus.iql.basic.typing.extension.impl;


import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;

public class ObjectExtensions implements IIQLTypeExtensions {
	
	
	public static void info(Object obj, String text) {
		 LoggerFactory.getLogger(obj.getClass()).info(text);
	}
	
	public static void debug(Object obj, String text) {
		 LoggerFactory.getLogger(obj.getClass()).debug(text);
	}
	
	public static void warn(Object obj, String text) {
		 LoggerFactory.getLogger(obj.getClass()).warn(text);
	}
	
	public static void error(Object obj, String text) {
		 LoggerFactory.getLogger(obj.getClass()).error(text);
	}
	
	public static void print(Object obj, String text) {
		 System.out.print(text);
	}
	
	@Override
	public Class<?> getType() {
		return Object.class;
	}		
	
}
