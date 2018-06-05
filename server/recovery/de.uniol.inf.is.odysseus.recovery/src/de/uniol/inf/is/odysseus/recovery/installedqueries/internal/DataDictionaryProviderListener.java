package de.uniol.inf.is.odysseus.recovery.installedqueries.internal;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDatadictionaryProviderListener;
import de.uniol.inf.is.odysseus.recovery.installedqueries.IInstalledQueriesHandler;

/**
 * A listener for a {@link DataDictionaryProvider}.
 * @author Michael Brand
 *
 */
public class DataDictionaryProviderListener implements IDatadictionaryProviderListener {

	/**
	 * The provided {@link IDataDictionary}s and their listeners.
	 */
	private final Map<IDataDictionary, DataDictionaryListener> dictListeners = Maps.newHashMap();

	/**
	 * The {@link IInstalledQueriesHandler} that handles the backup.
	 */
	private IInstalledQueriesHandler handler;

	/**
	 * Creates a new listener.
	 * 
	 * @param handler
	 *            The {@link IInstalledQueriesHandler} that handles the backup.
	 */
	public DataDictionaryProviderListener(IInstalledQueriesHandler handler) {
		this.handler = handler;
	}

	@Override
	public void newDatadictionary(IDataDictionary dd) {
		DataDictionaryListener listener = new DataDictionaryListener(this.handler);
		dd.addListener(listener);
		dd.addSinkListener(listener);
		this.dictListeners.put(dd, listener);
	}

	@Override
	public void removedDatadictionary(IDataDictionary dd) {
		if (this.dictListeners.containsKey(dd)) {
			DataDictionaryListener listener = this.dictListeners.get(dd);
			dd.removeListener(listener);
			dd.removeSinkListener(listener);
			this.dictListeners.remove(dd);
		}

	}

}
