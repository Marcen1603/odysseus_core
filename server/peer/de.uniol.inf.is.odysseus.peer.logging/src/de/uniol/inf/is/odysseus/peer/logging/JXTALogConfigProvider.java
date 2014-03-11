package de.uniol.inf.is.odysseus.peer.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;


public class JXTALogConfigProvider {

	private static final String LOGACTIVE_SYS_PROPERTY = "peer.log";
	private static final Logger LOG = LoggerFactory.getLogger(JXTALogConfigProvider.class);
	
	private JXTALogConfigProvider() {
	}
	
	public static boolean isLogging() {
		try {
			String rendevousAddress = System.getProperty(LOGACTIVE_SYS_PROPERTY);
			if (!Strings.isNullOrEmpty(rendevousAddress)) {
				return Boolean.valueOf(rendevousAddress);
			}

			rendevousAddress = OdysseusConfiguration.get(LOGACTIVE_SYS_PROPERTY);
			if (!Strings.isNullOrEmpty(rendevousAddress)) {
				return Boolean.valueOf(rendevousAddress);
			}
		} catch (Throwable t) {
			LOG.error("Could not determine if this peer is logging", t);
		}
		return false;
	}
}
