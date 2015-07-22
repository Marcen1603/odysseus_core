package de.uniol.inf.is.odysseus.parallelization.intraoperator.parameter;

import de.uniol.inf.is.odysseus.script.parser.parameter.IKeywordParameter;

public enum IntraOperatorParallelizationKeywordParameter implements IKeywordParameter{
	OPERATORID("id", 0, false),
	DEGREE_OF_PARALLELIZATION("degree", 1, false),
	BUFFERSIZE("buffersize", 2, true);

	private String name;
	private int position;
	private boolean isOptional;

	private IntraOperatorParallelizationKeywordParameter(String name, int position, boolean isOptional){
		this.name = name;
		this.position = position;
		this.isOptional = isOptional;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public boolean isOptional() {
		return isOptional;
	}

}
