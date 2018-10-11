package de.uniol.inf.is.odysseus.rcp.logging.save;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.logging.ILogSaver;

public final class LogSaverRegistry {

	private final Map<String, Class<? extends ILogSaver>> saverMap = Maps.newHashMap();
	
	public void register( Class<? extends ILogSaver> logSaverClass, String name ) {
		// Preconditions.checkNotNull(logSaverClass, "Logsaver class must not be null!");
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Logsaver name must not be null or empty!");
		
		ILogSaver instance = createInstance(logSaverClass);
		if( instance == null ) {
			return;
		}
		
		synchronized( saverMap ) {
			saverMap.put(name, logSaverClass);
		}
	}

	public void unregister(String name) {
		synchronized( saverMap) {
			saverMap.remove(name);
		}
	}
	
	public Collection<String> getRegisteredSaverNames() {
		synchronized( saverMap ) {
			return Lists.newArrayList(saverMap.keySet());
		}
	}
	
	public Optional<? extends ILogSaver> createLogSaverInstance( String name ) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Name of log saver must not be null or empty!");
		
		synchronized( saverMap ) {
			Class<? extends ILogSaver> saverClass = saverMap.get(name);
			if( saverClass != null ) {
				return Optional.fromNullable(createInstance(saverClass));
			}
			
			return Optional.absent();
		}
	}
	
	private static <T> T createInstance(Class<? extends T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			System.err.println("Could not create an instance of class '" + clazz.getName() + "' (default constructor available?)");
			e.printStackTrace(System.err);
			
			return null;
		}
	}
}
