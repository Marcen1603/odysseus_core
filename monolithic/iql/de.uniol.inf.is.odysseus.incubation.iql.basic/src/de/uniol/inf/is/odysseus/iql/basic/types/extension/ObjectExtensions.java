package de.uniol.inf.is.odysseus.iql.basic.types.extension;


import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.iql.basic.typing.extension.ExtensionMethod;
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
	
	@ExtensionMethod(ignoreFirstParameter=false)
	public static void print(Object text) {
		 System.out.print(text);
	}
	
	@ExtensionMethod(ignoreFirstParameter=false)
	public static void println(Object text) {
		 System.out.println(text);
	}
	
	@Override
	public Class<?> getType() {
		return Object.class;
	}		
	
}
