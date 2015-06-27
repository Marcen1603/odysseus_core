package de.uniol.inf.is.odysseus.multithreaded.keyword;

import de.uniol.inf.is.odysseus.multithreaded.keyword.helper.IKeywordParameter;

public enum ParallelizationKeywordParameter implements IKeywordParameter{
	PARALLELIZATION_TYPE("type", 0, false),
	DEGREE_OF_PARALLELIZATION("degree", 1, false), 
	BUFFERSIZE("buffersize", 2, false), 
	OPTIMIZATION("optimization", 3, true);

	
	private ParallelizationKeywordParameter(String name, int position, boolean isOptional){
		this.name = name;
		this.position = position;
		this.isOptional = isOptional;
	}
	
	private String name;
	private Integer position;
	private boolean isOptional;
	

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
	
	public static ParallelizationKeywordParameter getParameterByName(String parallelizationParameter) {
		for (ParallelizationKeywordParameter parameter : ParallelizationKeywordParameter.values()){
			if (parameter.name().equalsIgnoreCase(parallelizationParameter)){
				return parameter;
			}
		}
		return null;
	}
}