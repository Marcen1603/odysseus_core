package de.uniol.inf.is.odysseus.dbIntegration.simpleCache.service;

import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * ResultKey ist eine Klasse, welche zum Erstellen von identifizierenden Objekten dient.
 * Dazu wird aus einem RelationalTupel und einer Datenbankanfrage ein Hashwert erzeugt.
 * @author crolfes
 *
 */
public class ResultKey {

	private RelationalTuple<?> inputStreamHash;
	private DBQuery queryHash;
	
	
	public ResultKey(RelationalTuple<?> relevantStreamElement, DBQuery query) {
		super();
		this.inputStreamHash = relevantStreamElement;
		this.queryHash = query;
	}


	public RelationalTuple<?> getInputStreamHash() {
		return inputStreamHash;
	}


	public DBQuery getQueryHash() {
		return queryHash;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((inputStreamHash == null) ? 0 : inputStreamHash.hashCode());
		result = prime * result
				+ ((queryHash == null) ? 0 : queryHash.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResultKey other = (ResultKey) obj;
		if (inputStreamHash == null) {
			if (other.inputStreamHash != null)
				return false;
		} else if (!inputStreamHash.equals(other.inputStreamHash))
			return false;
		if (queryHash == null) {
			if (other.queryHash != null)
				return false;
		} else if (!queryHash.equals(other.queryHash))
			return false;
		return true;
	}
	
	
}
