package de.uniol.inf.is.odysseus.rcp.logging.activator;

import org.apache.log4j.Logger;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.rcp.ImageManager;
import de.uniol.inf.is.odysseus.rcp.logging.container.RCPLogAppender;
import de.uniol.inf.is.odysseus.rcp.logging.container.RCPLogContainer;
import de.uniol.inf.is.odysseus.rcp.logging.save.CSVLogSaver;
import de.uniol.inf.is.odysseus.rcp.logging.save.LogSaverRegistry;
import de.uniol.inf.is.odysseus.rcp.logging.save.PlainTextLogSaver;

public class RCPLoggingPlugIn extends AbstractUIPlugin {

	private static RCPLogContainer rcpLogContainer;
	private static RCPLogAppender rcpLogAppender;
	private static LogSaverRegistry logSaverRegistry;
	
	private static ImageManager imageManager;
	
	@Override
	public void start(BundleContext context) throws Exception {
		rcpLogContainer = new RCPLogContainer();
		rcpLogAppender = new RCPLogAppender(rcpLogContainer);
		
		logSaverRegistry = new LogSaverRegistry();
		logSaverRegistry.register(PlainTextLogSaver.class, PlainTextLogSaver.SAVER_NAME);
		logSaverRegistry.register(CSVLogSaver.class, CSVLogSaver.SAVER_NAME);
		
		Logger.getRootLogger().addAppender(rcpLogAppender);
		
		imageManager = new ImageManager(context.getBundle());
		imageManager.register("fatal", "icons/fatal.gif");
		imageManager.register("error", "icons/error.png");
		imageManager.register("warn", "icons/warning.png");
		imageManager.register("info", "icons/info.png");
		imageManager.register("debug", "icons/debug.png");
		imageManager.register("trace", "icons/trace.png");
		imageManager.register("throwable", "icons/throwable.png");
		
		imageManager.register("clear", "icons/clear.gif");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		imageManager.disposeAll();
		
		logSaverRegistry.unregister(PlainTextLogSaver.SAVER_NAME);
		logSaverRegistry.unregister(CSVLogSaver.SAVER_NAME);
		
		Logger.getRootLogger().removeAppender(rcpLogAppender);
		
		rcpLogContainer.clear();
		rcpLogAppender = null;
	}
	
	public static RCPLogContainer getLogContainer() {
		return rcpLogContainer;
	}
	
	public static ImageManager getImageManager() {
		return imageManager;
	}
	
	public static LogSaverRegistry getLogSaverRegistry() {
		return logSaverRegistry;
	}
}
