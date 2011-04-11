package de.uniol.inf.is.odysseus.sparql.parser.helper;

public interface INode {

	public boolean isVariable();
	public boolean isLiteral();
	public boolean isIRI();
	public boolean isBlankNode();
	public String getName();
	public void setName(String name);
}
