package de.uniol.inf.is.odysseus.query.transformation.operator.java;

public class SelectTransformationOperator extends AbstractTransformationOperator{
	
  private final String name = "Select";
  private final String programmLanguage = "Java";
  
  
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getProgrammLanguage() {
		return programmLanguage;
	}
	
}
