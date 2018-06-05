package de.uniol.inf.is.odysseus.rcp.logging.view.util;

import java.util.HashMap;

import org.apache.log4j.Level;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.rcp.ImageManager;
import de.uniol.inf.is.odysseus.rcp.logging.activator.RCPLoggingPlugIn;

public final class LevelImageMap extends HashMap<Level, Image> {

	private static final long serialVersionUID = -5907670177779265856L;

	public LevelImageMap() {
		ImageManager manager = RCPLoggingPlugIn.getImageManager();
		
		put(Level.FATAL, manager.get("fatal"));
		put(Level.ERROR, manager.get("error"));
		put(Level.WARN, manager.get("warn"));
		put(Level.INFO, manager.get("info"));
		put(Level.DEBUG, manager.get("debug"));
		put(Level.TRACE, manager.get("trace"));
	}
}
