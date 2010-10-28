package de.uniol.inf.is.odysseus.ruleengine.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerSystem {

	private static Logger defaultlogger = LoggerFactory.getLogger("ruleengine");
	private static Accuracy defaultAccuracy = Accuracy.DEBUG;
	private static boolean showAllAsDebug = false;

	public enum Accuracy {
		ERROR, WARN, INFO, DEBUG, TRACE
	}

	public static void printlog(String name, String message) {
		printlog(message, LoggerFactory.getLogger(name));
	}

	public static void printlog(String name, Accuracy accuracy, String message) {
		printlog(accuracy, message, LoggerFactory.getLogger(name));

	}

	// defaults

	public static void printlog(Accuracy accuracy, String message) {
		printlog(accuracy, message, defaultlogger);
	}

	public static void printlog(String message) {
		printlog(message, defaultlogger);
	}

	// mappers

	private static void printlog(String message, Logger thelogger) {
		printlog(defaultAccuracy, message, thelogger);
	}

	private static void printlog(Accuracy accuracy, String message, Logger currentlogger) {
		if (showAllAsDebug) {
			currentlogger.debug("["+accuracy + "] " + message);
		} else {
			switch (accuracy) {
			case TRACE:
				currentlogger.trace(message);
				break;
			case DEBUG:
				currentlogger.debug(message);
				break;
			case INFO:
				currentlogger.info(message);
				break;
			case WARN:
				currentlogger.warn(message);
				break;
			case ERROR:
				currentlogger.error(message);
				break;

			default:
				break;
			}
		}
	}
}
