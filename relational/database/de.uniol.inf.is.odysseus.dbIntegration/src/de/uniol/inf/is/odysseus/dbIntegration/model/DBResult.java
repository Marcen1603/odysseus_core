package de.uniol.inf.is.odysseus.dbIntegration.model;


import java.util.List;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * Die Klasse DBResult kapselt Informationen von Datenbankergebnissen.
 * 
 * @author crolfes
 *
 */
public class DBResult {
	
	List<RelationalTuple<?>> result; 
	RelationalTuple<?> inputTuple;
	
	public DBResult (List<RelationalTuple<?>> result, RelationalTuple<?> inputTuple) {
		this.result = result;
		this.inputTuple = inputTuple;
	}

	public List<RelationalTuple<?>> getResult() {
		return result;
	}

	public void setResult(List<RelationalTuple<?>> result) {
		this.result = result;
	}

	public RelationalTuple<?> getInputTuple() {
		return inputTuple;
	}

	public void setInputTuple(RelationalTuple<?> inputTuple) {
		this.inputTuple = inputTuple;
	}
	
	
}
