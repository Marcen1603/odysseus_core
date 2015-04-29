package de.uniol.inf.is.odysseus.condition.conditionql.parser;

import java.io.Serializable;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.condition.conditionql.parser.enums.ConditionAlgorithm;

public class AlgorithmQueryInfo implements Serializable {

	private static final long serialVersionUID = -2709655991225995154L;
	private ConditionAlgorithm algorithm;
	private HashMap<String, String> parameters;

	public AlgorithmQueryInfo(ConditionAlgorithm algorithm, HashMap<String, String> parameters) {
		super();
		this.algorithm = algorithm;
		this.parameters = parameters;
	}

	public ConditionAlgorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(ConditionAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public HashMap<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}

}
