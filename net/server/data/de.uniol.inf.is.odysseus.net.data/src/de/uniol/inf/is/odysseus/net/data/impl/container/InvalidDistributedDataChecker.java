package de.uniol.inf.is.odysseus.net.data.impl.container;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;
import de.uniol.inf.is.odysseus.net.util.RepeatingJobThread;

public class InvalidDistributedDataChecker extends RepeatingJobThread {

	private static final String CHECK_INTERVAL_CONFIG_KEY = "net.dd.checkinterval";
	private static final long DEFAULT_CHECK_INTERVAL_MILLIS = 30 * 1000; 
	
	private final LocalDistributedDataContainer container;
	
	public InvalidDistributedDataChecker(LocalDistributedDataContainer container) {
		super(determineCheckInterval(), "Invalid Distributed Data Checker");
		
		Preconditions.checkNotNull(container, "container must not be null!");

		this.container = container;
	}
	
	private static long determineCheckInterval() {
		String checkIntervalStr = OdysseusNetConfiguration.get(CHECK_INTERVAL_CONFIG_KEY, "" + DEFAULT_CHECK_INTERVAL_MILLIS);
		try {
			return Long.valueOf(checkIntervalStr);
		} catch( Throwable t ) {
			return DEFAULT_CHECK_INTERVAL_MILLIS;
		}
	}

	@Override
	public void doJob() {
		container.checkForInvalidDistributedData();
	}
}
