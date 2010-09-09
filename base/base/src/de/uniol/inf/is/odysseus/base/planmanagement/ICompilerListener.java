package de.uniol.inf.is.odysseus.base.planmanagement;

public interface ICompilerListener {

	public void parserBound(String parserID);
	public void rewriteBound();
	public void transformationBound();
	
}
