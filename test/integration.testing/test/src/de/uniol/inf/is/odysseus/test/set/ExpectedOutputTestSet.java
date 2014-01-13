package de.uniol.inf.is.odysseus.test.set;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class ExpectedOutputTestSet extends QueryTestSet {
	
	private List<Pair<String, String>> expectedOutput;

	public List<Pair<String, String>> getExpectedOutput() {
		return expectedOutput;
	}

	public void setExpectedOutput(List<Pair<String, String>> expectedOutput) {
		this.expectedOutput = expectedOutput;
	}
}
