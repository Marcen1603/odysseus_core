package de.uniol.inf.is.odysseus.rcp.logging.save;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.logging.ILogSaver;
import de.uniol.inf.is.odysseus.rcp.logging.activator.RCPLoggingPlugIn;

public class LogSaverBinder {

	// called by OSGi-DS
	public static void bindLogSaver( ILogSaver saver ) {
		Optional<String> optSaverName = tryDetermineName(saver);
		if( optSaverName.isPresent() ) {
			String saverName = optSaverName.get();
			RCPLoggingPlugIn.getLogSaverRegistry().register(saver.getClass(), saverName);
		} else {
			System.err.println("Log saver '" + saver + "' not bound");
		}
	}
	
	private static Optional<String> tryDetermineName(ILogSaver saver) {
		try {
			String name = saver.getName();
			if( !Strings.isNullOrEmpty(name)) {
				return Optional.of(name);
			}
		} catch( Throwable t ) {
			System.err.println("Could not determine name of log saver '" + saver + "'");
			t.printStackTrace(System.err);
		}
		
		return Optional.absent();
	}

	// called by OSGi-DS
	public static void unbindLogSaver( ILogSaver saver ) {
		Optional<String> optSaverName = tryDetermineName(saver);
		if( optSaverName.isPresent() ) {
			RCPLoggingPlugIn.getLogSaverRegistry().unregister(optSaverName.get());
		}
	}
}
