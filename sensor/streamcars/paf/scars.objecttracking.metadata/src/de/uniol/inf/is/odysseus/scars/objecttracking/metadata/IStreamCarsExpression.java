package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public interface IStreamCarsExpression {
	
	public String getExpression();
	
	public List<IStreamCarsExpressionVariable> getVariables();
	
	public Object getValue();
	
	public double getDoubleValue();
	
	public IStreamCarsExpressionVariable getTarget();
	
	public void evaluate();
	
	public void init(SDFAttributeList...schemata);
	
	public void replaceVaryingIndex(SDFAttributeList schema, int index);
	
	public void replaceVaryingIndex(SDFAttributeList schema, int index, boolean copy);
	
	public void bind(String variable, Object value);
	
	public void bind(IStreamCarsExpressionVariable variable, Object value);
	

	
	
}
