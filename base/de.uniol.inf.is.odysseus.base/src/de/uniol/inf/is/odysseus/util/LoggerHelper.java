package de.uniol.inf.is.odysseus.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**Dieses hier ist ein Hack damit das Drools-Plugin keine Fehler liefert!*/
public class LoggerHelper {

	private static Map<String, LoggerHelper> instances = new HashMap<String, LoggerHelper>();
	private Logger _logger = null;
	
	private LoggerHelper(String name){
		_logger = LoggerFactory.getLogger(name);
	}
	
	public static synchronized LoggerHelper getInstance(String loggerName){
		if(instances.get(loggerName) == null){
			try {
				instances.put(loggerName, new LoggerHelper(loggerName));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instances.get(loggerName);
	}
	
	public void error(String message) {
		if (_logger != null) {
			_logger.error(message);
		}
	}

	public void info(String info) {
		if (_logger != null) {
			_logger.info(info);
		}
	}

	public boolean isInfoEnabled() {
		if (_logger != null) {
			return _logger.isInfoEnabled();
		} else {
			return false;
		}
	}
	
	public void warn(String message){
		if (_logger != null) {
			_logger.warn(message);
		}
	}
	
	public void debug(String message){
		if (_logger != null) {
			_logger.debug(message);
		}
	}

}
