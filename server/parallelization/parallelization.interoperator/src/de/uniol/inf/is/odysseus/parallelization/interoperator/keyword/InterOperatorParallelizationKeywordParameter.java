package de.uniol.inf.is.odysseus.parallelization.interoperator.keyword;

import de.uniol.inf.is.odysseus.script.parser.parameter.ICustomPatternKeywordParameter;

public enum InterOperatorParallelizationKeywordParameter implements ICustomPatternKeywordParameter{
	OPERATORID("id", 0, false, "<id or (firstId:secondId:allowSemanticChange) multiple ids comma seperated and no blank>"),
	DEGREE("degree", 1, false),
	BUFFERSIZE("buffersize", 2, false),
	STRATEGY("strategy", 3, true),
	FRAGMENTATION("fragment", 4, true);
	
	private InterOperatorParallelizationKeywordParameter(String name, int position, boolean isOptional, String valuePattern){
		this.name = name;
		this.position = position;
		this.isOptional = isOptional;
		this.valuePattern = valuePattern;
	}
	
	private InterOperatorParallelizationKeywordParameter(String name, int position, boolean isOptional){
		this.name = name;
		this.position = position;
		this.isOptional = isOptional;
		this.valuePattern = "";
	}
	
	
	private String name;
	private Integer position;
	private boolean isOptional;
	private String valuePattern;
	

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getPosition() {
		return this.position;
	}

	@Override
	public boolean isOptional() {
		return this.isOptional;
	}

	@Override
	public String getValuePattern() {
		return this.valuePattern;
	}

}
