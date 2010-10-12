package de.uniol.inf.is.odysseus.planmanagement.configuration;

import java.util.ArrayList;

public class ValueChangeHandler<T extends ISetting<?>> implements IValueChangeHandler<T> {

	/**
	 * List of objects which are informed if an entry value changes.
	 */
	private ArrayList<IValueChangeListener<T>> changeListener = new ArrayList<IValueChangeListener<T>>();
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.configuration.IValueChangeHandler#addValueChangeListener(de.uniol.inf.is.odysseus.planmanagement.configuration.IValueChangeListener)
	 */
	@Override
	public void addValueChangeListener(IValueChangeListener<T> listener) {
		this.changeListener.add(listener);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.configuration.IValueChangeHandler#removeValueChangeListener(de.uniol.inf.is.odysseus.planmanagement.configuration.IValueChangeListener)
	 */
	@Override
	public void removeValueChangeListener(IValueChangeListener<T> listener) {
		this.changeListener.remove(listener);
	}
	
	/**
	 * Sends a setting changed event to all registered listeners.
	 * 
	 * @param newEntry Entry which changed. 
	 */
	protected void settingChanged(T newEntry) {
		for (IValueChangeListener<T> listener : this.changeListener) {
			listener.settingChanged(newEntry);
		}
	}
	

}
