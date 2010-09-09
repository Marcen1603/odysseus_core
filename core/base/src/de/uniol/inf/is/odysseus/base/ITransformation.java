package de.uniol.inf.is.odysseus.base;

import java.util.ArrayList;

public interface ITransformation {
	public ArrayList<IPhysicalOperator> transform(ILogicalOperator op, TransformationConfiguration config) throws TransformationException;
}
