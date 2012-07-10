/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.ruleengine.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerSystem {

	private static Logger defaultlogger = LoggerFactory.getLogger("ruleengine");
	private static Accuracy defaultAccuracy = Accuracy.DEBUG;
	private static Accuracy minAccuracy = Accuracy.TRACE;
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

	private static void printlog(Accuracy accuracy, String message,
			Logger currentlogger) {
		if (accuracy.ordinal() < minAccuracy.ordinal()) {
			if (showAllAsDebug) {
				currentlogger.debug("[" + accuracy + "] " + message);
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
}
