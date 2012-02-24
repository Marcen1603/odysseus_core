package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public interface IDataSourceObserverListener<T> {

	public void streamElementRecieved( DataSourceObserverSink<T> sender, T element, int port );
	public void punctuationElementRecieved( DataSourceObserverSink<T> sender, PointInTime punctuation, int port );
}
