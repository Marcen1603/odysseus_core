package de.uniol.inf.is.odysseus.ruleengine.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerSystem {
	
	private static Logger logger = LoggerFactory.getLogger("transformation");
	
	public enum Accuracy{
		ERROR,
		WARN,
		INFO,
		DEBUG,		
		TRACE
	}
	
	public static void printlog(String message){
		printlog(Accuracy.DEBUG, message);
	}
	
	public static void printlog(Accuracy accuracy, String message){
		switch (accuracy) {
		case TRACE:
			//logger.trace(message);
			//break;
		case DEBUG:
			logger.debug(message);
			break;
		case INFO:
			logger.info(message);
			break;
		case WARN:
			logger.warn(message);
			break;
		case ERROR:
			logger.error(message);
			break;

		default:
			break;
		}				
	}
}
