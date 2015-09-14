package de.uniol.inf.is.odysseus.recovery.querystate;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDatadictionaryProviderListener;

/**
 * Listener for new or removed {@code IDataDictionaries} for a certain
 * {@code ITenant}. For the {@code IDataDictionary}, a
 * {@code DataDictionaryListener} will be registered.
 * 
 * @author Michael Brand
 *
 */
public class DataDictionaryProviderListener implements IDatadictionaryProviderListener {

	/**
	 * The {@code IDataDictionary} if present.
	 */
	private Optional<IDataDictionary> mDD = Optional.absent();

	/**
	 * The listener for {@code DataDictionaryProviderListener#mDD}.
	 */
	private Optional<DataDictionaryListener> mDDListener = Optional.absent();

	/**
	 * Creates a new listener.
	 */
	public DataDictionaryProviderListener() {
		// Empty default constructor.
	}

	/**
	 * Creates a new listener.
	 * 
	 * @param dd
	 *            An {@code IDataDictionary}.
	 */
	public DataDictionaryProviderListener(IDataDictionary dd) {
		this();
		newDatadictionary(dd);
	}

	@Override
	public void newDatadictionary(IDataDictionary dd) {
		DataDictionaryListener listener = new DataDictionaryListener();
		dd.addListener(listener);
		dd.addSinkListener(listener);
		this.mDD = Optional.of(dd);
		this.mDDListener = Optional.of(listener);
	}

	@Override
	public void removedDatadictionary(IDataDictionary dd) {
		if (this.mDD.isPresent() && this.mDD.get().equals(dd) && this.mDDListener.isPresent()) {
			dd.removeListener(this.mDDListener.get());
			dd.removeSinkListener(this.mDDListener.get());
			this.mDD = Optional.absent();
			this.mDDListener = Optional.absent();
		}
	}

}
