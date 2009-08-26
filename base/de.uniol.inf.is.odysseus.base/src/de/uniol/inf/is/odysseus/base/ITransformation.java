package de.uniol.inf.is.odysseus.base;

public interface ITransformation {
	public IPhysicalOperator transform(ILogicalOperator op, TransformationConfiguration config) throws TransformationException;
}
