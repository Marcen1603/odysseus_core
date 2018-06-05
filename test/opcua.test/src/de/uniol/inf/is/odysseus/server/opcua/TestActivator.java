package de.uniol.inf.is.odysseus.server.opcua;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestActivator implements BundleActivator {

	private final Logger log = LoggerFactory.getLogger(TestActivator.class);

	static {
		// Configure silly Log4J
		ConsoleAppender console = new ConsoleAppender();
		String PATTERN = "%r [%t] %-5p %c - %m%n";
		console.setLayout(new PatternLayout(PATTERN));
		console.setThreshold(Level.INFO);
		console.activateOptions();
		org.apache.log4j.Logger.getRootLogger().addAppender(console);
	}

	private static BundleContext context;

	@Override
	public void start(BundleContext ctx) throws Exception {
		log.info("Starting test bundle ({})...", ctx.getClass().getSimpleName());
		context = ctx;
	}

	@Override
	public void stop(BundleContext ctx) throws Exception {
		log.info("Stopping test bundle ({})...", ctx.getClass().getSimpleName());
		context = null;
	}

	public static BundleContext getContext() {
		return context;
	}
}