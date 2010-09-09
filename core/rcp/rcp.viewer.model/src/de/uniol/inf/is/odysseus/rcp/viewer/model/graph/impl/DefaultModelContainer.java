package de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IModelDataContainer;

/**
 * Standardimplementierung der IModelDataContainer-Schnittstelle. Sie stellt
 * den Inhalt eines Knotens dar.
 * 
 * @author Timo Michelsen
 *
 * @param <T>
 */
public class DefaultModelContainer<T> implements IModelDataContainer<T>{

	private T content;
	
	/**
	 * Konstruktor, welcher mit dem gegebenen Inhalt eine
	 * DefaultModelContainer-Instanz erzuegt. null als Inhalt
	 * ist erlaubt.
	 * 
	 * @param content Inhalt des DefaultModelContainer (auch null erlaubt)
	 */
	public DefaultModelContainer( T content ) {
		this.content = content;
	}
	
	@Override
	public T getContent() {
		return content;
	}

}
